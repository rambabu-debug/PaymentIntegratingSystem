package todo.tutorials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.tutorials.model.PaymentTransaction;
import todo.tutorials.model.User;

import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    Optional<PaymentTransaction> findByStripePaymentIntentId(String stripePaymentIntentId);
    Optional<PaymentTransaction> findByOrderReferenceAndMerchant(String orderReference, User merchant);
}

