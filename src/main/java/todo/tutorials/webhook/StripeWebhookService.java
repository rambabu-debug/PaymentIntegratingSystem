package todo.tutorials.webhook;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.tutorials.model.PaymentStatus;
import todo.tutorials.payment.PaymentService;
import todo.tutorials.service.AuditService;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookService {

    private final PaymentService paymentService;
    private final AuditService auditService;

    @Value("${app.stripe.webhook-secret}")
    private String webhookSecret;

    /**
     * Process Stripe webhook events
     */
    @Transactional
    public void handleWebhookEvent(String payload, String sigHeader) {
        Event event = null;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            log.error("Webhook signature verification failed: {}", e.getMessage());
            throw new RuntimeException("Invalid webhook signature");
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;

        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        }

        // Handle different event types
        switch (event.getType()) {
            case "payment_intent.succeeded":
                handlePaymentIntentSucceeded((PaymentIntent) stripeObject);
                break;
            case "payment_intent.payment_failed":
                handlePaymentIntentFailed((PaymentIntent) stripeObject);
                break;
            case "payment_intent.canceled":
                handlePaymentIntentCanceled((PaymentIntent) stripeObject);
                break;
            case "payment_intent.processing":
                handlePaymentIntentProcessing((PaymentIntent) stripeObject);
                break;
            default:
                log.info("Unhandled event type: {}", event.getType());
        }
    }

    private void handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
        log.info("Payment succeeded: {}", paymentIntent.getId());
        String intentId = paymentIntent.getId();
        paymentService.updatePaymentStatusByIntentId(intentId, PaymentStatus.SUCCEEDED);

        Map<String, Object> details = new HashMap<>();
        details.put("intentId", intentId);
        details.put("amount", paymentIntent.getAmount());
        details.put("currency", paymentIntent.getCurrency());
        auditService.logTransaction("stripe", intentId, "PAYMENT_SUCCEEDED", details);
    }

    private void handlePaymentIntentFailed(PaymentIntent paymentIntent) {
        log.info("Payment failed: {}", paymentIntent.getId());
        String intentId = paymentIntent.getId();
        paymentService.updatePaymentStatusByIntentId(intentId, PaymentStatus.FAILED);

        Map<String, Object> details = new HashMap<>();
        details.put("intentId", intentId);
        details.put("amount", paymentIntent.getAmount());
        details.put("currency", paymentIntent.getCurrency());
        details.put("lastError", paymentIntent.getLastPaymentError() != null ? 
                paymentIntent.getLastPaymentError().getMessage() : "Unknown error");
        auditService.logTransaction("stripe", intentId, "PAYMENT_FAILED", details);
    }

    private void handlePaymentIntentCanceled(PaymentIntent paymentIntent) {
        log.info("Payment canceled: {}", paymentIntent.getId());
        String intentId = paymentIntent.getId();
        paymentService.updatePaymentStatusByIntentId(intentId, PaymentStatus.CANCELED);

        Map<String, Object> details = new HashMap<>();
        details.put("intentId", intentId);
        details.put("amount", paymentIntent.getAmount());
        details.put("currency", paymentIntent.getCurrency());
        auditService.logTransaction("stripe", intentId, "PAYMENT_CANCELED", details);
    }

    private void handlePaymentIntentProcessing(PaymentIntent paymentIntent) {
        log.info("Payment processing: {}", paymentIntent.getId());
        String intentId = paymentIntent.getId();
        paymentService.updatePaymentStatusByIntentId(intentId, PaymentStatus.PROCESSING);

        Map<String, Object> details = new HashMap<>();
        details.put("intentId", intentId);
        details.put("amount", paymentIntent.getAmount());
        details.put("currency", paymentIntent.getCurrency());
        auditService.logTransaction("stripe", intentId, "PAYMENT_PROCESSING", details);
    }
}

