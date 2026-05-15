package todo.tutorials.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import todo.tutorials.common.ApiException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseService {

    private final ObjectProvider<Firestore> firestoreProvider;

    /**
     * Save a document to Firestore
     */
    public <T> void save(String collection, String documentId, T data) {
        Firestore firestore = firestoreProvider.getIfAvailable();
        if (firestore == null) {
            log.info("Firebase unavailable - skipping save for {}/{}", collection, documentId);
            return;
        }

        try {
            firestore.collection(collection).document(documentId).set(data).get();
            log.info("Document saved: {} in collection: {}", documentId, collection);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("Failed to save document: " + e.getMessage());
        }
    }

    /**
     * Update a document in Firestore
     */
    public void update(String collection, String documentId, Map<String, Object> updates) {
        Firestore firestore = firestoreProvider.getIfAvailable();
        if (firestore == null) {
            log.info("Firebase unavailable - skipping update for {}/{}", collection, documentId);
            return;
        }

        try {
            firestore.collection(collection).document(documentId).update(updates).get();
            log.info("Document updated: {} in collection: {}", documentId, collection);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("Failed to update document: " + e.getMessage());
        }
    }

    /**
     * Get a document from Firestore
     */
    public <T> T get(String collection, String documentId, Class<T> valueType) {
        Firestore firestore = firestoreProvider.getIfAvailable();
        if (firestore == null) {
            log.info("Firebase unavailable - skipping get for {}/{}", collection, documentId);
            return null;
        }

        try {
            return firestore.collection(collection).document(documentId)
                    .get().get().toObject(valueType);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("Failed to retrieve document: " + e.getMessage());
        }
    }

    /**
     * Get all documents from a collection
     */
    public <T> List<T> getAll(String collection, Class<T> valueType) {
        Firestore firestore = firestoreProvider.getIfAvailable();
        if (firestore == null) {
            log.info("Firebase unavailable - skipping getAll for {}", collection);
            return List.of();
        }

        try {
            QuerySnapshot snapshot = firestore.collection(collection).get().get();
            return snapshot.toObjects(valueType);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("Failed to retrieve collection: " + e.getMessage());
        }
    }

    /**
     * Delete a document from Firestore
     */
    public void delete(String collection, String documentId) {
        Firestore firestore = firestoreProvider.getIfAvailable();
        if (firestore == null) {
            log.info("Firebase unavailable - skipping delete for {}/{}", collection, documentId);
            return;
        }

        try {
            firestore.collection(collection).document(documentId).delete().get();
            log.info("Document deleted: {} from collection: {}", documentId, collection);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("Failed to delete document: " + e.getMessage());
        }
    }

    /**
     * Check if document exists
     */
    public boolean exists(String collection, String documentId) {
        Firestore firestore = firestoreProvider.getIfAvailable();
        if (firestore == null) {
            log.info("Firebase unavailable - skipping exists check for {}/{}", collection, documentId);
            return false;
        }

        try {
            return firestore.collection(collection).document(documentId).get().get().exists();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * Add timestamp to data
     */
    public Map<String, Object> addTimestamp(Map<String, Object> data) {
        data.put("createdAt", System.currentTimeMillis());
        data.put("updatedAt", System.currentTimeMillis());
        return data;
    }
}

