# Payment Integration System - Complete Setup Guide

A secure, production-ready payment integration platform built with Spring Boot, Stripe, Firebase, and JWT authentication.

## 🎯 Project Overview

This is a comprehensive merchant payment platform that enables:
- **Secure Authentication**: JWT-based auth with role-based access control (ADMIN, MERCHANT, CLIENT)
- **Stripe Payment Processing**: Seamless integration with Stripe payment gateway (Test mode ready)
- **Firebase Backend**: Real-time data storage and audit logging
- **Transaction Management**: Complete transaction lifecycle with status tracking
- **Webhook Handling**: Automatic payment status synchronization with Stripe events
- **Comprehensive Logging**: All transactions and activities logged to Firebase

## 📋 Prerequisites

- **Java 17+** (Spring Boot 3.3.4 requires Java 17)
- **Gradle 8.x** (included with wrapper)
- **Stripe Account**: Sign up at https://stripe.com
- **Firebase Project**: Create at https://firebase.google.com
- **Postman**: For API testing (optional but recommended)

## 🔧 Installation Steps

### Step 1: Clone/Download Project

```bash
cd C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem
```

### Step 2: Configure Firebase

1. **Go to Firebase Console**: https://console.firebase.google.com
2. **Create a New Project**:
   - Project Name: "PaymentIntegrationSystem"
   - Enable Google Analytics (optional)
3. **Create Service Account**:
   - Go to Project Settings → Service Accounts
   - Click "Generate New Private Key"
   - Save as `serviceAccountKey.json`
4. **Enable Firestore**:
   - Go to Firestore Database
   - Create database in test mode (for development)
   - Start Collection with name "users", "transactions", "audit_logs"

### Step 3: Set Up Environment Variables

Create a `.env` file in the project root or set system environment variables:

```bash
# Firebase Configuration
FIREBASE_PROJECT_ID=your-firebase-project-id

# Database Configuration (H2 for dev, PostgreSQL for prod)
DB_URL=jdbc:h2:mem:paymentdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
DB_USERNAME=sa
DB_PASSWORD=
DB_DRIVER=org.h2.Driver

# Server Configuration
SERVER_PORT=8080

# JWT Configuration
JWT_SECRET=your-super-secret-jwt-key-at-least-32-characters-long-change-this!
JWT_EXPIRATION_MS=3600000

# Stripe Configuration
STRIPE_SECRET_KEY=sk_test_your_stripe_test_key_here
STRIPE_WEBHOOK_SECRET=whsec_your_stripe_webhook_secret_here
```

### Step 4: Set Google Cloud Credentials

For Firebase integration to work:

**Windows:**
```powershell
$env:GOOGLE_APPLICATION_CREDENTIALS="C:\path\to\serviceAccountKey.json"
```

**Linux/Mac:**
```bash
export GOOGLE_APPLICATION_CREDENTIALS="/path/to/serviceAccountKey.json"
```

Or place `serviceAccountKey.json` in the project root and it will be auto-detected.

### Step 5: Build the Project

```bash
# Using Gradle wrapper
./gradlew.bat build

# Or with clean rebuild
./gradlew.bat clean build
```

### Step 6: Run the Application

```bash
# Using Gradle
./gradlew.bat bootRun

# Or run the built JAR
java -jar build/libs/PaymentIntegrationSystem-1.0.0.jar

# With environment variables
set STRIPE_SECRET_KEY=sk_test_xxx && set JWT_SECRET=your-secret && ./gradlew.bat bootRun
```

The application will start on `http://localhost:8080`

## 📱 API Endpoints

### Authentication Endpoints

#### Register as Merchant
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "merchant@example.com",
  "password": "SecurePassword123!",
  "role": "MERCHANT",
  "businessName": "My Store"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "merchant@example.com",
  "role": "MERCHANT"
}
```

#### Register as Client
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "client@example.com",
  "password": "SecurePassword123!",
  "role": "CLIENT"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "merchant@example.com",
  "password": "SecurePassword123!"
}
```

#### Get Current User
```http
GET /api/auth/me
Authorization: Bearer {token}
```

### Payment Endpoints

#### Create Payment Intent
```http
POST /api/payments/intents
Authorization: Bearer {token}
Content-Type: application/json

{
  "amount": 5000,
  "currency": "USD",
  "description": "Customer Purchase",
  "orderReference": "ORDER-123456"
}
```

**Response:**
```json
{
  "transactionId": 1,
  "paymentIntentId": "pi_1234567890",
  "clientSecret": "pi_1234567890_secret_xxx",
  "status": "CREATED"
}
```

**Amount Notes:**
- Amount is in cents (5000 = $50.00)
- Minimum amount: 50 cents (0.50 USD)
- Use test card: `4242 4242 4242 4242` with any future expiry date

#### Get Payment Details
```http
GET /api/payments/{transactionId}
Authorization: Bearer {token}
```

### Webhook Endpoint

#### Stripe Webhook
```http
POST /api/webhooks/stripe
Stripe-Signature: {signature}
Content-Type: application/json

{
  "type": "payment_intent.succeeded",
  "data": {
    "object": {...}
  }
}
```

## 🧪 Testing with Postman

1. **Import Collection**:
   - Open Postman
   - Click "Import"
   - Select `postman/PaymentIntegrationSystem_Updated.postman_collection.json`

2. **Set Variables**:
   - Click "Environments" → Create new
   - Add variables:
     - `token`: Your JWT token (get from login response)
     - `stripe_signature`: Your Stripe webhook signature

3. **Test Flow**:
   - Register Merchant
   - Login to get token
   - Set token in environment
   - Create Payment Intent
   - Test webhook (optional)

## 💳 Stripe Test Cards

| Card Number | Type | Result |
|------------|------|--------|
| 4242 4242 4242 4242 | Visa | Success |
| 5555 5555 5555 4444 | Mastercard | Success |
| 3782 822463 10005 | Amex | Success |
| 4000 0000 0000 0002 | Visa | Declined |
| 4000 0025 0000 3155 | Visa | Requires Auth |

## 🔑 Stripe Configuration

### Get Your Keys

1. **Go to Stripe Dashboard**: https://dashboard.stripe.com
2. **Developers → API Keys**:
   - Copy "Secret Key" (starts with `sk_test_`)
   - Copy "Publishable Key" (starts with `pk_test_`)
3. **Set Webhook**:
   - Developers → Webhooks → Add Endpoint
   - URL: `https://yourapp.com/api/webhooks/stripe`
   - Events: `payment_intent.succeeded`, `payment_intent.payment_failed`, `payment_intent.processing`, `payment_intent.canceled`
   - Copy "Signing Secret"

### Configure in application.yml

```yaml
app:
  stripe:
    secret-key: sk_test_your_key_here
    webhook-secret: whsec_your_secret_here
```

## 🔥 Firebase Configuration

### Collection Structure

#### users
```json
{
  "userId": "1",
  "email": "merchant@example.com",
  "businessName": "My Store",
  "role": "MERCHANT",
  "registeredAt": 1704067200000,
  "storedAt": 1704067200000
}
```

#### transactions
```json
{
  "id": "1",
  "merchantId": "1",
  "amount": 5000,
  "currency": "usd",
  "orderReference": "ORDER-123456",
  "stripePaymentIntentId": "pi_1234567890",
  "status": "SUCCEEDED",
  "createdAt": 1704067200000,
  "storedAt": 1704067200000
}
```

#### audit_logs
```json
{
  "userId": "1",
  "transactionId": "1",
  "type": "PAYMENT_INTENT_CREATED",
  "details": {
    "amount": 5000,
    "currency": "usd",
    "orderReference": "ORDER-123456"
  },
  "timestamp": 1704067200000,
  "createdAt": "2024-01-01T12:00:00Z"
}
```

## 🔐 Security Features

- ✅ **JWT Authentication**: Stateless, token-based auth
- ✅ **Password Encoding**: BCrypt with salt rounds
- ✅ **Role-Based Access Control**: ADMIN, MERCHANT, CLIENT
- ✅ **CORS Support**: Configurable cross-origin requests
- ✅ **Input Validation**: Comprehensive request validation
- ✅ **HTTPS Ready**: SSL/TLS support
- ✅ **PCI Compliance**: Never storing card details
- ✅ **Audit Logging**: All transactions logged to Firebase

## 📊 Database Schema

### Users Table
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role ENUM('ADMIN', 'MERCHANT', 'CLIENT') NOT NULL,
  enabled BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Merchant Profiles Table
```sql
CREATE TABLE merchant_profiles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL UNIQUE,
  business_name VARCHAR(255) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

### Payment Transactions Table
```sql
CREATE TABLE payment_transactions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  merchant_id BIGINT NOT NULL,
  amount BIGINT NOT NULL,
  currency VARCHAR(10) NOT NULL,
  order_reference VARCHAR(255) NOT NULL UNIQUE,
  stripe_payment_intent_id VARCHAR(255) NOT NULL UNIQUE,
  client_secret VARCHAR(255) NOT NULL,
  status ENUM('CREATED', 'REQUIRES_ACTION', 'SUCCEEDED', 'FAILED', 'CANCELED', 'PROCESSING'),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (merchant_id) REFERENCES users(id)
);
```

## 🚀 Moving to Production

### Before Going Live:

1. **Update Stripe Keys**:
   ```bash
   STRIPE_SECRET_KEY=sk_live_your_live_key_here
   STRIPE_WEBHOOK_SECRET=whsec_your_live_secret_here
   ```

2. **Update Database**:
   - Replace H2 with PostgreSQL or MySQL
   - Configure production credentials

3. **Secure Secrets**:
   - Use AWS Secrets Manager or similar
   - Never commit `.env` files
   - Rotate JWT secrets regularly

4. **Configure HTTPS**:
   ```yaml
   server:
     ssl:
       key-store: classpath:keystore.p12
       key-store-password: ${SSL_PASSWORD}
       key-store-type: PKCS12
   ```

5. **Enable CORS for Frontend**:
   ```yaml
   cors:
     allowed-origins: https://yourfrontend.com
     allowed-methods: GET,POST,PUT,DELETE
   ```

6. **Update Stripe Webhook URL**:
   - Change from localhost to production URL
   - Update signing secret

## 📝 Project Structure

```
PaymentIntegrationSystem/
├── src/main/java/todo/tutorials/
│   ├── Main.java                    # Entry point
│   ├── auth/                        # Authentication
│   │   ├── AuthController.java
│   │   ├── AuthService.java
│   │   ├── CustomUserDetailsService.java
│   │   ├── JwtService.java
│   │   └── dto/
│   ├── config/                      # Configuration
│   │   ├── SecurityConfig.java
│   │   ├── FirebaseConfig.java
│   │   └── JwtAuthenticationFilter.java
│   ├── model/                       # Entities
│   │   ├── User.java
│   │   ├── MerchantProfile.java
│   │   ├── PaymentTransaction.java
│   │   ├── PaymentStatus.java
│   │   └── Role.java
│   ├── payment/                     # Payment processing
│   │   ├── PaymentController.java
│   │   ├── PaymentService.java
│   │   ├── StripeGateway.java
│   │   ├── StripeGatewayImpl.java
│   │   ├── StripePaymentIntent.java
│   │   └── dto/
│   ├── webhook/                     # Webhook handling
│   │   ├── StripeWebhookController.java
│   │   └── StripeWebhookService.java
│   ├── repository/                  # Data access
│   │   ├── UserRepository.java
│   │   ├── MerchantProfileRepository.java
│   │   └── PaymentTransactionRepository.java
│   ├── service/                     # Business logic
│   │   ├── FirebaseService.java
│   │   └── AuditService.java
│   └── common/                      # Utilities
│       ├── ApiException.java
│       └── GlobalExceptionHandler.java
├── src/main/resources/
│   └── application.yml
├── build.gradle
└── postman/
    └── PaymentIntegrationSystem_Updated.postman_collection.json
```

## 🐛 Troubleshooting

### Issue: Firebase Connection Failed
**Solution**: 
- Verify `GOOGLE_APPLICATION_CREDENTIALS` is set
- Check Firebase project ID matches configuration
- Ensure service account has Firestore permissions

### Issue: Stripe Key Invalid
**Solution**:
- Verify key format (sk_test_... or sk_live_...)
- Ensure key is for correct environment
- Check key hasn't been rotated

### Issue: JWT Token Expired
**Solution**:
- Token expires after 1 hour by default
- Get new token by logging in again
- Increase `JWT_EXPIRATION_MS` if needed

### Issue: Payment Intent Failed
**Solution**:
- Use valid test card number
- Ensure amount is at least $0.50
- Check Stripe account is in test mode
- Verify webhook secret for production

## 📞 Support & Resources

- **Stripe Documentation**: https://stripe.com/docs
- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **Firebase Docs**: https://firebase.google.com/docs
- **JWT Specification**: https://tools.ietf.org/html/rfc7519

## 📄 License

This project is provided as-is for educational and commercial use.

---

**Last Updated**: April 5, 2026
**Spring Boot Version**: 3.3.4
**Java Version**: 17+

