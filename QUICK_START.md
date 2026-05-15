# Quick Start Guide - Payment Integration System

## ⚡ 5-Minute Setup

### 1. Prerequisites Check
```bash
# Check Java version
java -version  # Should be 17+

# Check Gradle
./gradlew.bat --version
```

### 2. Set Environment Variables (Windows)
```powershell
$env:STRIPE_SECRET_KEY = "sk_test_51234567890"
$env:JWT_SECRET = "your-super-secret-key-at-least-32-chars-long"
$env:FIREBASE_PROJECT_ID = "1:673864344737:web:c0692f8334991b44a77ce0"
```

### 3. Run Application
```bash
cd C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem
.\gradlew.bat bootRun
```

Your application is now running at `http://localhost:8080`

---

## 🚀 Test the API in 3 Steps

### Step 1: Register a Merchant
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "merchant@example.com",
    "password": "SecurePassword123!",
    "role": "MERCHANT",
    "businessName": "My Store"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "merchant@example.com",
  "role": "MERCHANT"
}
```

### Step 2: Create a Payment Intent
```bash
curl -X POST http://localhost:8080/api/payments/intents \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -d '{
    "amount": 5000,
    "currency": "USD",
    "description": "Test Payment",
    "orderReference": "ORDER-001"
  }'
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

### Step 3: Get Payment Status
```bash
curl http://localhost:8080/api/payments/1 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 📱 Using Postman

1. **Import Collection**
   - Open Postman
   - File → Import
   - Select `postman/PaymentIntegrationSystem_Updated.postman_collection.json`

2. **Set Token Variable**
   - Copy token from Register response
   - Go to Postman Environments
   - Set `{{token}}` variable

3. **Test Endpoints**
   - Click "Send" on each request
   - View responses and modify as needed

---

## 🔑 Stripe Setup (Test Mode)

### Get Your Test Keys
1. Go to https://dashboard.stripe.com
2. Click "Developers" → "API Keys"
3. Copy the "Secret Key" (starts with `sk_test_`)
4. Set in environment: `STRIPE_SECRET_KEY=sk_test_xxx`

### Use Test Card
- Card Number: `4242 4242 4242 4242`
- Expiry: Any future date (e.g., 12/25)
- CVC: Any 3 digits (e.g., 123)

---

## 🔥 Firebase Setup

### Create Firebase Project
1. Go to https://console.firebase.google.com
2. Create new project
3. Copy Project ID
4. Set in environment: `FIREBASE_PROJECT_ID=your-id`

### Download Service Account Key
1. Project Settings → Service Accounts
2. Generate New Private Key
3. Save as `serviceAccountKey.json` in project root

### Enable Firestore
1. Go to Firestore Database
2. Create database in test mode
3. Collections auto-created: users, transactions, audit_logs

---

## 📁 Project Structure

```
📦 PaymentIntegrationSystem
├── 📄 README.md                    # Overview
├── 📄 SETUP_GUIDE.md              # Detailed setup
├── 📄 API_DOCUMENTATION.md        # API reference
├── 📄 DEPLOYMENT_GUIDE.md         # Production deployment
├── 📄 QUICK_START.md              # This file
├── 🔐 .env.example                # Environment template
├── 📦 build.gradle                # Dependencies
├── 📦 gradle/wrapper/             # Gradle wrapper
├── 📁 src/main/
│   ├── java/todo/tutorials/
│   │   ├── Main.java              # Application entry point
│   │   ├── auth/                  # Authentication
│   │   ├── payment/               # Payment processing
│   │   ├── webhook/               # Stripe webhooks
│   │   ├── config/                # Spring configuration
│   │   ├── model/                 # Database entities
│   │   ├── repository/            # Data access
│   │   ├── service/               # Business logic
│   │   └── common/                # Utilities
│   └── resources/
│       └── application.yml        # Application config
├── 📁 postman/                    # Postman collections
└── 📦 Dockerfile                  # Docker build
```

---

## 🔧 Common Configuration

### Change Port
**File:** `src/main/resources/application.yml`
```yaml
server:
  port: 9090
```

### Enable Database Logging
**File:** `src/main/resources/application.yml`
```yaml
spring:
  jpa:
    show-sql: true
```

### Change JWT Expiration (Default: 1 hour)
**File:** `src/main/resources/application.yml`
```yaml
app:
  jwt:
    expiration-ms: 7200000  # 2 hours
```

---

## 🐛 Troubleshooting

### Issue: "Cannot find Firebase credentials"
**Solution:**
```powershell
# Set path to service account key
$env:GOOGLE_APPLICATION_CREDENTIALS = "C:\path\to\serviceAccountKey.json"
```

### Issue: "Stripe key is invalid"
**Solution:**
- Verify key starts with `sk_test_` (not `pk_`)
- Check key hasn't been copied incorrectly
- Regenerate key if needed

### Issue: "Port 8080 already in use"
**Solution:**
```powershell
# Kill process on port 8080
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Or use different port
$env:SERVER_PORT = "8081"
```

### Issue: "Build fails with 'cannot find symbol'"
**Solution:**
```bash
./gradlew.bat clean build --refresh-dependencies
```

---

## 📊 Monitoring

### Check Application Health
```bash
curl http://localhost:8080/api/auth/me
```

### View Application Logs
```bash
# When running with bootRun
# Logs appear in terminal

# When running as JAR
tail -f logs/app.log
```

### Monitor Firebase
1. Go to Firebase Console
2. Firestore → Collections
3. View documents being created in real-time

---

## 🚀 Next Steps

1. **Frontend Integration**
   - Use `clientSecret` from payment intent
   - Integrate with Stripe Elements/Payment Element
   - Handle webhooks for status updates

2. **Database Persistence**
   - Switch from H2 to PostgreSQL for production
   - Set up automated backups

3. **Production Deployment**
   - See `DEPLOYMENT_GUIDE.md`
   - Switch to Stripe live keys
   - Enable HTTPS

4. **Monitoring & Alerts**
   - Set up CloudWatch/DataDog
   - Configure error notifications
   - Monitor transaction success rates

---

## 📚 Documentation Links

- [Complete Setup Guide](SETUP_GUIDE.md)
- [API Documentation](API_DOCUMENTATION.md)
- [Deployment Guide](DEPLOYMENT_GUIDE.md)
- [Stripe Documentation](https://stripe.com/docs)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Firebase Documentation](https://firebase.google.com/docs)

---

## 💡 Pro Tips

1. **Use Postman Environment Variables**
   - Avoid hardcoding tokens
   - Switch between dev/prod easily

2. **Monitor Firebase Firestore**
   - Real-time data synchronization
   - Audit trail for transactions

3. **Test Stripe Webhooks Locally**
   - Use Stripe CLI for local testing
   - Simulate different payment scenarios

4. **Enable Debug Logging**
   - Set log level to DEBUG
   - Easier troubleshooting

---

**Start coding now!** 🎉

For detailed information, see [SETUP_GUIDE.md](SETUP_GUIDE.md)

