package todo.tutorials.payment;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import todo.tutorials.common.ApiException;

@Component
public class StripeGatewayImpl implements StripeGateway {

    @Value("${app.stripe.secret-key}")
    private String stripeSecret;

    @PostConstruct
    void init() {
        Stripe.apiKey = stripeSecret;
    }

    @Override
    public StripePaymentIntent createPaymentIntent(long amount, String currency, String description, String orderReference) {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency.toLowerCase())
                .setDescription(description)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
                .putMetadata("orderReference", orderReference)
                .build();

        try {
            PaymentIntent intent = PaymentIntent.create(params);
            return new StripePaymentIntent(intent.getId(), intent.getClientSecret(), intent.getStatus());
        } catch (StripeException e) {
            throw new ApiException("Failed to create payment intent: " + e.getMessage());
        }
    }
}

