package todo.tutorials.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.tutorials.common.ApiException;
import todo.tutorials.model.PaymentStatus;
import todo.tutorials.model.PaymentTransaction;
import todo.tutorials.model.Role;
import todo.tutorials.model.User;
import todo.tutorials.payment.dto.CreatePaymentIntentRequest;
import todo.tutorials.payment.dto.PaymentIntentResponse;
import todo.tutorials.repository.PaymentTransactionRepository;
import todo.tutorials.service.AuditService;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final StripeGateway stripeGateway;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final AuditService auditService;

    @Transactional
    public PaymentIntentResponse createPaymentIntent(User merchant, CreatePaymentIntentRequest request) {
        if (merchant.getRole() != Role.MERCHANT && merchant.getRole() != Role.ADMIN) {
            throw new ApiException("Only merchants can create payment intents");
        }

        paymentTransactionRepository.findByOrderReferenceAndMerchant(request.getOrderReference(), merchant)
                .ifPresent(existing -> {
                    throw new ApiException("Order reference already exists for this merchant");
                });

        StripePaymentIntent stripeIntent = stripeGateway.createPaymentIntent(
                request.getAmount(),
                request.getCurrency(),
                request.getDescription(),
                request.getOrderReference());

        PaymentTransaction tx = new PaymentTransaction();
        tx.setMerchant(merchant);
        tx.setAmount(request.getAmount());
        tx.setCurrency(request.getCurrency().toLowerCase());
        tx.setOrderReference(request.getOrderReference());
        tx.setStripePaymentIntentId(stripeIntent.getId());
        tx.setClientSecret(stripeIntent.getClientSecret());
        tx.setStatus(mapStripeStatus(stripeIntent.getStatus()));

        PaymentTransaction saved = paymentTransactionRepository.save(tx);

        // Store transaction in Firebase
        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("id", saved.getId());
        transactionData.put("merchantId", merchant.getId());
        transactionData.put("amount", saved.getAmount());
        transactionData.put("currency", saved.getCurrency());
        transactionData.put("orderReference", saved.getOrderReference());
        transactionData.put("stripePaymentIntentId", saved.getStripePaymentIntentId());
        transactionData.put("status", saved.getStatus().name());
        transactionData.put("createdAt", System.currentTimeMillis());
        auditService.storeTransaction(saved.getId().toString(), transactionData);

        // Log transaction creation
        Map<String, Object> details = new HashMap<>();
        details.put("merchantId", merchant.getId());
        details.put("amount", saved.getAmount());
        details.put("currency", saved.getCurrency());
        details.put("orderReference", saved.getOrderReference());
        auditService.logTransaction(merchant.getId().toString(), saved.getId().toString(), "PAYMENT_INTENT_CREATED", details);

        log.info("Payment intent created: {} for merchant: {}", stripeIntent.getId(), merchant.getEmail());
        return new PaymentIntentResponse(saved.getId(), saved.getStripePaymentIntentId(), saved.getClientSecret(), saved.getStatus());
    }

    @Transactional
    public void updatePaymentStatusByIntentId(String intentId, PaymentStatus status) {
        PaymentTransaction tx = paymentTransactionRepository.findByStripePaymentIntentId(intentId)
                .orElseThrow(() -> new ApiException("Payment intent not found in merchant records"));
        
        PaymentStatus oldStatus = tx.getStatus();
        tx.setStatus(status);
        paymentTransactionRepository.save(tx);

        // Log status update in Firebase
        Map<String, Object> details = new HashMap<>();
        details.put("intentId", intentId);
        details.put("oldStatus", oldStatus.name());
        details.put("newStatus", status.name());
        auditService.logTransaction(tx.getMerchant().getId().toString(), tx.getId().toString(), "PAYMENT_STATUS_UPDATED", details);

        log.info("Payment status updated: {} from {} to {}", intentId, oldStatus, status);
    }

    public PaymentIntentResponse getPayment(Long id, User currentUser) {
        PaymentTransaction tx = paymentTransactionRepository.findById(id)
                .orElseThrow(() -> new ApiException("Transaction not found"));

        if (currentUser.getRole() != Role.ADMIN && !tx.getMerchant().getId().equals(currentUser.getId())) {
            throw new ApiException("You are not allowed to access this transaction");
        }

        return new PaymentIntentResponse(tx.getId(), tx.getStripePaymentIntentId(), tx.getClientSecret(), tx.getStatus());
    }

    private PaymentStatus mapStripeStatus(String stripeStatus) {
        return switch (stripeStatus) {
            case "succeeded" -> PaymentStatus.SUCCEEDED;
            case "processing" -> PaymentStatus.PROCESSING;
            case "canceled" -> PaymentStatus.CANCELED;
            case "requires_action", "requires_confirmation", "requires_payment_method" -> PaymentStatus.REQUIRES_ACTION;
            default -> PaymentStatus.CREATED;
        };
    }
}

