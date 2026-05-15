package todo.tutorials.webhook;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {

    private final StripeWebhookService stripeWebhookService;

    @PostMapping("/stripe")
    public ResponseEntity<Map<String, String>> handleStripeWebhook(@RequestBody String payload,
                                                                    @RequestHeader("Stripe-Signature") String signatureHeader) {
        try {
            stripeWebhookService.handleWebhookEvent(payload, signatureHeader);
            log.info("Stripe webhook processed successfully");
            return ResponseEntity.ok(Map.of("message", "Webhook processed"));
        } catch (Exception e) {
            log.error("Error processing webhook: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", "Webhook processing failed: " + e.getMessage()));
        }
    }
}

