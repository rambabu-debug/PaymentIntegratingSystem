package todo.tutorials.payment.dto;

import todo.tutorials.model.PaymentStatus;

public class PaymentIntentResponse {

    private Long transactionId;
    private String paymentIntentId;
    private String clientSecret;
    private PaymentStatus status;

    public PaymentIntentResponse(Long transactionId, String paymentIntentId, String clientSecret, PaymentStatus status) {
        this.transactionId = transactionId;
        this.paymentIntentId = paymentIntentId;
        this.clientSecret = clientSecret;
        this.status = status;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public String getPaymentIntentId() {
        return paymentIntentId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}

