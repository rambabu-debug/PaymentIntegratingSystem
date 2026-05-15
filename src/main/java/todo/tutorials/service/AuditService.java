package todo.tutorials.service;

import com.google.cloud.firestore.Firestore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import todo.tutorials.common.ApiException;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Audit Service for logging all transactions and activities to Firebase
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final ObjectProvider<Firestore> firestoreProvider;
    private static final String AUDIT_LOGS_COLLECTION = "audit_logs";
    private static final String TRANSACTIONS_COLLECTION = "transactions";
    private static final String USERS_COLLECTION = "users";

    /**
     * Log transaction to Firebase
     */
    public void logTransaction(String userId, String transactionId, String type, Map<String, Object> details) {
        Firestore firestore = firestoreProvider.getIfAvailable();
        if (firestore == null) {
            log.info("Firebase unavailable - skipping transaction audit for {}", transactionId);
            return;
        }

        try {
            Map<String, Object> auditLog = Map.of(
                    "userId", userId,
                    "transactionId", transactionId,
                    "type", type,
                    "details", details,
                    "timestamp", System.currentTimeMillis(),
                    "createdAt", new java.util.Date()
            );

            String documentId = transactionId + "_" + System.currentTimeMillis();
            firestore.collection(AUDIT_LOGS_COLLECTION).document(documentId).set(auditLog).get();
            log.info("Audit log created: {} for transaction: {}", documentId, transactionId);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            log.error("Failed to log transaction: {}", e.getMessage());
        }
    }

    /**
     * Log user activity
     */
    public void logUserActivity(String userId, String action, Map<String, Object> details) {
        Firestore firestore = firestoreProvider.getIfAvailable();
        if (firestore == null) {
            log.info("Firebase unavailable - skipping user activity log for {}", userId);
            return;
        }

        try {
            Map<String, Object> activityLog = Map.of(
                    "userId", userId,
                    "action", action,
                    "details", details,
                    "timestamp", System.currentTimeMillis(),
                    "createdAt", new java.util.Date()
            );

            String documentId = userId + "_" + action + "_" + System.currentTimeMillis();
            firestore.collection(AUDIT_LOGS_COLLECTION).document(documentId).set(activityLog).get();
            log.info("User activity logged: {} for user: {}", action, userId);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            log.error("Failed to log user activity: {}", e.getMessage());
        }
    }

    /**
     * Store transaction in Firebase
     */
    public void storeTransaction(String transactionId, Map<String, Object> transactionData) {
        Firestore firestore = firestoreProvider.getIfAvailable();
        if (firestore == null) {
            log.info("Firebase unavailable - skipping transaction storage for {}", transactionId);
            return;
        }

        try {
            transactionData.put("storedAt", System.currentTimeMillis());
            firestore.collection(TRANSACTIONS_COLLECTION).document(transactionId).set(transactionData).get();
            log.info("Transaction stored in Firebase: {}", transactionId);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("Failed to store transaction: " + e.getMessage());
        }
    }

    /**
     * Store user profile in Firebase
     */
    public void storeUserProfile(String userId, Map<String, Object> userData) {
        Firestore firestore = firestoreProvider.getIfAvailable();
        if (firestore == null) {
            log.info("Firebase unavailable - skipping user profile storage for {}", userId);
            return;
        }

        try {
            userData.put("storedAt", System.currentTimeMillis());
            firestore.collection(USERS_COLLECTION).document(userId).set(userData).get();
            log.info("User profile stored in Firebase: {}", userId);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("Failed to store user profile: " + e.getMessage());
        }
    }
}

