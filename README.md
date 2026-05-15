# Payment Integration System - Complete Backend Platform

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17%2B-brightgreen)](https://www.oracle.com/java/)
[![Stripe](https://img.shields.io/badge/Stripe-Payment%20Gateway-blue)](https://stripe.com)
[![Firebase](https://img.shields.io/badge/Firebase-Firestore%20%26%20Auth-orange)](https://firebase.google.com)
[![JWT](https://img.shields.io/badge/JWT-Authentication-lightblue)](https://jwt.io)

A production-ready, secure payment integration platform enabling merchants and clients to process payments seamlessly with Stripe, backed by Firebase for real-time data and comprehensive audit logging.

## 🎯 Key Features

### 🔐 Security & Authentication
- **JWT-Based Authentication**: Stateless, secure token-based access
- **Role-Based Access Control**: ADMIN, MERCHANT, and CLIENT roles
- **BCrypt Password Encryption**: Industry-standard password hashing
- **CORS Support**: Configurable cross-origin requests
- **Input Validation**: Comprehensive request validation

### 💳 Payment Processing
- **Stripe Integration**: Full payment gateway integration (Test & Live modes)
- **Payment Intent Creation**: Create and manage payment intents
- **Multiple Payment Methods**: Cards, net banking, and more
- **Transaction Status Tracking**: Real-time payment status updates
- **Webhook Handling**: Automatic synchronization with Stripe events

### 🔥 Firebase Backend
- **Real-Time Data Storage**: Store and retrieve user and transaction data
- **Audit Logging**: Complete transaction and activity audit trail
- **Firestore Synchronization**: All data synced with Firebase
- **Collections Management**: Dedicated collections for users, transactions, and audit logs

## 🔧 Tech Stack
- **Language**: Java 17+
- **Framework**: Spring Boot 3.3.4
- **Authentication**: JWT (JJWT 0.11.5)
- **Database**: H2 (Dev), PostgreSQL (Prod)
- **Payment Gateway**: Stripe API
- **Backend Storage**: Firebase Firestore
- **Security**: Spring Security
- **ORM**: Spring Data JPA
- **Build Tool**: Gradle 8.x

## ⚡ Quick Start

### 1. Prerequisites
```bash
# Check Java version
java -version  # Should be 17+
```

### 2. Environment Setup
```powershell
$env:STRIPE_SECRET_KEY = "sk_test_your_key"
$env:JWT_SECRET = "your-secret-key-at-least-32-chars"
$env:FIREBASE_PROJECT_ID = "your-firebase-id"
```

### 3. Run Application
```powershell
cd PaymentIntegrationSystem
./gradlew.bat bootRun
```

API available at: `http://localhost:8080`

## 📚 Complete Documentation

| Document | Purpose |
|----------|---------|
| **[QUICK_START.md](QUICK_START.md)** | 5-minute setup with curl examples |
| **[SETUP_GUIDE.md](SETUP_GUIDE.md)** | Detailed installation & configuration |
| **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** | Complete API reference |
| **[DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md)** | Production deployment guide |

## 🚀 API Endpoints

### Authentication
```
POST   /api/auth/register     - Register user
POST   /api/auth/login        - Login user
GET    /api/auth/me           - Get current user
```

### Payments
```
POST   /api/payments/intents  - Create payment intent
GET    /api/payments/{id}     - Get payment details
```

### Webhooks
```
POST   /api/webhooks/stripe   - Stripe webhook handler
```

## 🧪 Test with Postman

1. **Import Collection**:
   ```
   postman/PaymentIntegrationSystem_Updated.postman_collection.json
   ```

2. **Register and Login** to get JWT token

3. **Create Payment Intent**:
   ```json
   {
     "amount": 5000,
     "currency": "USD",
     "description": "Test Payment",
     "orderReference": "ORDER-001"
   }
   ```

## 💳 Stripe Test Cards

- **Success**: `4242 4242 4242 4242`
- **Declined**: `4000 0000 0000 0002`
- **Auth Required**: `4000 0025 0000 3155`

## 🐳 Docker Support

```bash
# Build image
docker build -t payment-integration-system .

# Run with docker-compose
docker-compose up -d
```

## 📊 Database Schema

- **users**: User accounts with roles
- **merchant_profiles**: Merchant business information
- **payment_transactions**: Payment records with Stripe integration

## 🔥 Firebase Collections

- **users**: User profiles
- **transactions**: Transaction records
- **audit_logs**: Complete audit trail

## 🔒 Production Checklist

- [ ] Switch Stripe to live keys
- [ ] Update JWT secret
- [ ] Configure PostgreSQL database
- [ ] Enable HTTPS/SSL
- [ ] Set up database backups
- [ ] Configure Firebase security rules
- [ ] Enable monitoring and alerts

## 📁 Project Structure

```
PaymentIntegrationSystem/
├── src/main/java/todo/tutorials/
│   ├── auth/          # Authentication & JWT
│   ├── payment/       # Payment processing
│   ├── webhook/       # Stripe webhooks
│   ├── config/        # Spring configuration
│   ├── model/         # Database entities
│   ├── repository/    # Data access
│   ├── service/       # Business logic
│   └── common/        # Utilities
├── src/main/resources/
│   └── application.yml
├── build.gradle
├── Dockerfile
├── docker-compose.yml
└── .env.example
```

## 🐛 Common Issues

| Issue | Solution |
|-------|----------|
| Firebase connection failed | Set `GOOGLE_APPLICATION_CREDENTIALS` environment variable |
| Port 8080 already in use | Change port: `$env:SERVER_PORT = "8081"` |
| Stripe key invalid | Verify key format (starts with `sk_test_`) |
| Build fails | Run `./gradlew.bat clean build --refresh-dependencies` |

## 📞 Support & Resources

- [Stripe Documentation](https://stripe.com/docs)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Firebase Documentation](https://firebase.google.com/docs)
- [JWT Information](https://jwt.io)

## 📝 Notes

- Stripe is configured in **test mode** by default
- Switch to live keys only after validating the entire payment flow
- All transactions are logged to Firebase for audit purposes
- Firebase credentials required for full functionality

## 🎉 Getting Started

1. [5-Minute Quick Start](QUICK_START.md)
2. [Detailed Setup Guide](SETUP_GUIDE.md)
3. [API Documentation](API_DOCUMENTATION.md)
4. [Deployment Guide](DEPLOYMENT_GUIDE.md)

---

**Status**: Production Ready ✅
**Version**: 1.0.0
**Last Updated**: April 5, 2026
