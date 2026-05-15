package todo.tutorials.payment;

public class StripePaymentIntent {

    private final String id;
    private final String clientSecret;
    private final String status;

    public StripePaymentIntent(String id, String clientSecret, String status) {
        this.id = id;
        this.clientSecret = clientSecret;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getStatus() {
        return status;
    }
}

