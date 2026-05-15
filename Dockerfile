FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy gradle files
COPY gradlew .
COPY gradlew.bat .
COPY gradle gradle

# Copy source code
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Build the application
RUN chmod +x gradlew && ./gradlew build -x test

# Expose port
EXPOSE 8080

# Set environment variables (Read from build args or environment)
ENV SERVER_PORT=8080
ENV DB_URL=jdbc:h2:mem:paymentdb
ENV FIREBASE_PROJECT_ID=${FIREBASE_PROJECT_ID}
ENV STRIPE_SECRET_KEY=${STRIPE_SECRET_KEY}
ENV STRIPE_WEBHOOK_SECRET=${STRIPE_WEBHOOK_SECRET}
ENV JWT_SECRET=${JWT_SECRET}

# Run the application
CMD ["java", "-jar", "build/libs/PaymentIntegrationSystem-1.0.0.jar"]

