# Payment Integration System - Complete Setup

A secure merchant payment platform built with Spring Boot, Stripe, JWT authentication, and optional Firebase integration.

## Quick Answer: Where Is My Data?

### Currently (Right Now - Running)
✅ **H2 In-Memory Database** - All user/payment data is stored here
- User credentials (email, password, role)
- Merchant profiles (business name, etc.)
- Payment transactions
- Access: http://localhost:8080/h2-console

### Optional (Firebase - Disabled by Default)
⚠️ **Firebase** - Can also store audit logs and backups if enabled
- Requires service account credentials
- Enable with: `$env:FIREBASE_ENABLED='true'`

---

## What's Running?

```
✓ Spring Boot Application on http://localhost:8080
✓ H2 Database Console at http://localhost:8080/h2-console
✓ JWT Authentication (secure token-based auth)
✓ Stripe Integration (in test mode)
✓ Postman Collection for API testing
```

---

## Quick Start - Test Your API

### 1. Import Postman Collection
```
File → Import → Select: POSTMAN_COLLECTION.json
```

### 2. Test Register
```
POST /api/auth/register
{
  "email": "merchant@example.com",
  "password": "SecurePassword@123",
  "role": "merchant",
  "businessName": "My Online Store"
}
```

### 3. Check Data in H2 Console
```
1. Open: http://localhost:8080/h2-console
2. JDBC URL: jdbc:h2:mem:paymentdb
3. Username: sa
4. Password: (leave blank)
5. Run: SELECT * FROM users;
```

---

## Database Tables

### Users Table
| Column | Type | Purpose |
|--------|------|---------|
| id | BIGINT | Primary key |
| email | VARCHAR | Login email (unique) |
| password | VARCHAR | Encrypted password |
| role | VARCHAR | MERCHANT or CLIENT |
| enabled | BOOLEAN | Account active |

### Merchant Profiles Table
| Column | Type | Purpose |
|--------|------|---------|
| id | BIGINT | Primary key |
| user_id | BIGINT | Foreign key to users |
| business_name | VARCHAR | Merchant store name |

### Transactions Table
| Column | Type | Purpose |
|--------|------|---------|
| id | BIGINT | Primary key |
| stripe_transaction_id | VARCHAR | Stripe charge ID |
| amount | BIGINT | Amount in cents |
| currency | VARCHAR | USD, INR, etc. |
| status | VARCHAR | succeeded, failed, pending |

---

## Environment Configuration

### Currently Set
```properties
SERVER_PORT=8080 (auto-detected if in use)
APP_STRIPE_SECRET_KEY=*** CONFIGURED ***
APP_STRIPE_WEBHOOK_SECRET=*** CONFIGURED ***
APP_JWT_SECRET=*** CONFIGURED ***
APP_JWT_EXPIRATION_MS=3600000
FIREBASE_ENABLED=false
```

### To Change Secrets (Before Running)
Edit `run-local.ps1` or set environment variables:
```powershell
$env:APP_JWT_SECRET='YourNewSecretKey'
$env:APP_STRIPE_SECRET_KEY='sk_test_your_key'
.\run-local.ps1
```

### To Enable Firebase
```powershell
# 1. Download service account JSON from Firebase Console
# 2. Run with Firebase enabled
$env:FIREBASE_ENABLED='true'
$env:GOOGLE_APPLICATION_CREDENTIALS='C:\path\to\serviceAccountKey.json'
.\run-local.ps1
```

---

## API Endpoints

### Authentication
| Method | Endpoint | Purpose | Auth |
|--------|----------|---------|------|
| POST | /api/auth/register | Register user | None |
| POST | /api/auth/login | Get JWT token | None |

### Webhooks
| Method | Endpoint | Purpose | Auth |
|--------|----------|---------|------|
| POST | /api/webhooks/stripe | Receive Stripe events | None |

### Protected Endpoints (Require JWT)
Add header: `Authorization: Bearer <token>`

---

## Files You Got

```
├── POSTMAN_COLLECTION.json        → Import into Postman for testing
├── TESTING_GUIDE.md               → Complete testing walkthrough
├── run-local.ps1                  → Start app with auto-port detection
├── find-available-port.ps1        → Find free ports
└── src/main/resources/
    └── application.yml            → Configuration (secrets, port, Firebase)
```

---

## Firebase Integration (Optional)

### Why Firebase?
- Audit trails (log every login, registration, payment)
- Data backup (replicate critical data to cloud)
- Real-time analytics
- Compliance logging

### How to Enable
1. Go to https://console.firebase.google.com/
2. Project: `paymentintegratinsystem`
3. Settings → Service Accounts → Generate Private Key
4. Set env var: `GOOGLE_APPLICATION_CREDENTIALS=C:\path\to\key.json`
5. Set env var: `FIREBASE_ENABLED=true`
6. Restart app: `.\run-local.ps1`

### Where Data Goes
- `users/` collection → User profiles (backup)
- `audit_logs/` collection → Login/registration activity
- `transactions/` collection → Payment transaction history

---

## Testing Workflow

### 1. Register a Merchant
```json
{
  "email": "store@example.com",
  "password": "StorePass@123",
  "role": "merchant",
  "businessName": "My Awesome Store"
}
```
→ Get JWT token in response

### 2. Check H2 Database
```sql
SELECT * FROM users;
SELECT * FROM merchant_profiles;
```

### 3. Login
```json
{
  "email": "store@example.com",
  "password": "StorePass@123"
}
```
→ Get new JWT token

### 4. (Optional) Test Stripe Webhook
```json
POST /api/webhooks/stripe
{
  "type": "charge.succeeded",
  "data": {
    "object": {
      "id": "ch_test_1234",
      "amount": 5000,
      "status": "succeeded"
    }
  }
}
```

---

## Stripe Integration (Test Mode)

Your app is configured for Stripe test mode. This means:
- ✅ No real money is charged
- ✅ Use test card: 4242 4242 4242 4242
- ✅ Any future date and any CVC work
- ✅ Perfect for development and testing

### When You're Ready for Live Mode
1. Update Stripe keys in `application.yml` or env vars
2. Change test keys to live keys (sk_live_...)
3. Update webhook endpoint in Stripe dashboard
4. Restart app

---

## Troubleshooting

### Port Already in Use
```powershell
# Auto-detect and use available port
.\run-local.ps1  # does this automatically

# Or specify port
.\run-local.ps1 -serverPort 3000
```

### Can't Access H2 Console
- Check URL: http://localhost:8080/h2-console
- Make sure app is still running
- If port changed: http://localhost:<your-port>/h2-console

### Firebase Not Working
- Set `FIREBASE_ENABLED=true`
- Verify credentials path exists
- Check app logs for Firebase initialization message

### JWT Token Expired
- Tokens valid for 1 hour by default
- Get new token from login endpoint
- Use in header: `Authorization: Bearer <token>`

---

## Next Steps

1. **Test the API** → Import POSTMAN_COLLECTION.json
2. **View Data** → Open http://localhost:8080/h2-console
3. **Read Full Guide** → Open TESTING_GUIDE.md
4. **Enable Firebase** → (Optional) Follow Firebase setup steps
5. **Go Live** → Update to Stripe live keys when ready

---

## Support

- 📖 **Full Documentation**: See TESTING_GUIDE.md
- 🔧 **Configuration**: Edit application.yml or use env vars
- 🚀 **Start App**: Run `.\run-local.ps1`
- 🧪 **Test**: Import POSTMAN_COLLECTION.json

---

**Your application is running and ready for testing!**

💾 **Data Storage**: H2 Database (local) + Optional Firebase (cloud)
🔐 **Security**: JWT tokens, encrypted passwords, Stripe PCI-compliant
🎯 **Status**: Development mode, ready for merchant testing

