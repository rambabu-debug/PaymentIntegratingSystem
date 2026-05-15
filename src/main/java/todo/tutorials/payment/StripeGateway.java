package todo.tutorials.payment;

public interface StripeGateway {
    StripePaymentIntent createPaymentIntent(long amount, String currency, String description, String orderReference);
}

