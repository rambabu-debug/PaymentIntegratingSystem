package todo.tutorials.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Value("${firebase.project-id}")
    private String projectId;

    @Value("${firebase.credentials-path:}")
    private String credentialsPath;

    @Bean
    @ConditionalOnProperty(name = "firebase.enabled", havingValue = "true")
    public Firestore firestore() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            GoogleCredentials credentials = loadCredentials();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setProjectId(projectId)
                    .build();
            FirebaseApp.initializeApp(options);
            log.info("Firebase initialized with project ID: {}", projectId);
        }
        return FirestoreClient.getFirestore();
    }

    private GoogleCredentials loadCredentials() throws IOException {
        if (credentialsPath != null && !credentialsPath.isBlank()) {
            try (InputStream inputStream = new FileInputStream(credentialsPath)) {
                log.info("Loading Firebase credentials from {}", credentialsPath);
                return GoogleCredentials.fromStream(inputStream);
            }
        }

        log.info("Loading Firebase credentials from Application Default Credentials");
        return GoogleCredentials.getApplicationDefault();
    }
}

