package todo.tutorials.payment;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todo.tutorials.model.User;
import todo.tutorials.payment.dto.CreatePaymentIntentRequest;
import todo.tutorials.payment.dto.PaymentIntentResponse;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/intents")
    public ResponseEntity<PaymentIntentResponse> createPaymentIntent(@AuthenticationPrincipal User merchant,
                                                                     @Valid @RequestBody CreatePaymentIntentRequest request) {
        return ResponseEntity.ok(paymentService.createPaymentIntent(merchant, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentIntentResponse> getPayment(@PathVariable Long id,
                                                            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(paymentService.getPayment(id, user));
    }
}

