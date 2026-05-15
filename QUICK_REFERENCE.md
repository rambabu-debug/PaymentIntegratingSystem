# Quick Reference Card

## Where Is My Data?

```
┌─────────────────────────────────────────┐
│  YOUR DATA IS STORED IN:                │
├─────────────────────────────────────────┤
│ 1. H2 In-Memory Database (PRIMARY)     │
│    • User credentials                  │
│    • Merchant profiles                 │
│    • Payment transactions              │
│                                         │
│ 2. Firebase (OPTIONAL - Disabled)      │
│    • User profile backups              │
│    • Audit logs (logins, registrations)│
│    • Transaction history               │
└─────────────────────────────────────────┘
```

## Access Your Data

### Option 1: H2 Database Console (Easy)
```
1. Open: http://localhost:8080/h2-console
2. JDBC URL: jdbc:h2:mem:paymentdb
3. Username: sa
4. Password: (blank)
5. Click Connect
6. Run SQL queries
```

**Quick SQL:**
```sql
-- See all users
SELECT * FROM users;

-- See merchant stores
SELECT mp.business_name, u.email FROM merchant_profiles mp 
JOIN users u ON mp.user_id = u.id;

-- Count total users
SELECT COUNT(*) FROM users;
```

### Option 2: Firebase Console (Optional - Requires Enable)
```
1. Go to: https://console.firebase.google.com/
2. Select: paymentintegratinsystem
3. Go to: Realtime Database
4. View: users, audit_logs, transactions collections
```

**To Enable Firebase:**
```powershell
# Set these env vars
$env:FIREBASE_ENABLED='true'
$env:GOOGLE_APPLICATION_CREDENTIALS='C:\path\to\serviceAccountKey.json'

# Restart app
.\run-local.ps1
```

---

## Run Application

### Basic
```powershell
cd 'C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem'
.\run-local.ps1
```
✓ Auto-detects available port (8080-9000)

### Custom Port
```powershell
.\run-local.ps1 -serverPort 3000
```

### Custom Secrets
```powershell
.\run-local.ps1 `
  -jwtSecret 'YourNewSecret' `
  -serverPort 8081
```

---

## Test API with Postman

### 1. Import Collection
```
Postman → Import → POSTMAN_COLLECTION.json
```

### 2. Register
```
POST /api/auth/register

Body:
{
  "email": "merchant@example.com",
  "password": "SecurePassword@123",
  "role": "merchant",
  "businessName": "My Store"
}

Response: {"token": "...", "email": "...", "role": "MERCHANT"}
```

### 3. Check H2 Console
```
http://localhost:8080/h2-console
Query: SELECT * FROM users;
```

### 4. Login
```
POST /api/auth/login

Body:
{
  "email": "merchant@example.com",
  "password": "SecurePassword@123"
}

Response: {"token": "...", "email": "...", "role": "MERCHANT"}
```

---

## Endpoints

### Public (No JWT Required)
```
POST /api/auth/register     → Register user
POST /api/auth/login        → Get JWT token
POST /api/webhooks/stripe   → Receive Stripe events
```

### Protected (JWT Required)
```
Add header: Authorization: Bearer <token>
```

---

## Where To Set Secrets

### Option 1: Environment Variables (Recommended)
```powershell
$env:APP_STRIPE_SECRET_KEY = 'sk_test_...'
$env:APP_STRIPE_WEBHOOK_SECRET = 'whsec_...'
$env:APP_JWT_SECRET = 'YourSecretKey'
$env:FIREBASE_ENABLED = 'true'  # or false
$env:GOOGLE_APPLICATION_CREDENTIALS = 'C:\path\to\serviceAccountKey.json'

.\run-local.ps1
```

### Option 2: application.yml (Not Recommended for Secrets)
```yaml
# src/main/resources/application.yml
app:
  jwt:
    secret: YourSecretKey
  stripe:
    secret-key: sk_test_...
    webhook-secret: whsec_...
firebase:
  enabled: true
  credentials-path: C:\path\to\serviceAccountKey.json
```

### Option 3: run-local.ps1 Parameters
```powershell
.\run-local.ps1 `
  -jwtSecret 'NewSecret' `
  -stripeSecret 'sk_test_...' `
  -stripeWebhook 'whsec_...'
```

---

## Stripe Test Cards

Your app is in test mode. Use these cards:

| Card | Number | Use |
|------|--------|-----|
| Visa | 4242 4242 4242 4242 | Any CVC, any future date |
| Visa (decline) | 4000 0000 0000 0002 | Test failed payment |
| MasterCard | 5555 5555 5555 4444 | Any CVC, any future date |

CVC: Any 3+ digits
Expiry: Any future date (MM/YY)

---

## Current Status

```
✅ Application: Running
✅ Port: 8080 (or auto-detected)
✅ Database: H2 In-Memory
✅ Auth: JWT Enabled
✅ Stripe: Test Mode
✅ Firebase: Disabled (optional)
```

---

## Common Issues

### Port In Use
```powershell
# Auto-handled by run-local.ps1
# Or specify port:
.\run-local.ps1 -serverPort 3000
```

### Can't Access H2 Console
```
Check URL: http://localhost:8080/h2-console
Check app is running
If port changed: http://localhost:<port>/h2-console
```

### Firebase Credentials Not Found
```powershell
# Download from Firebase Console:
# Project Settings → Service Accounts → Generate Private Key
# Then set env var with correct path
$env:GOOGLE_APPLICATION_CREDENTIALS='C:\full\path\to\serviceAccountKey.json'
```

---

## Next Steps

1. ✅ **App Running** → You're here!
2. 📝 **Test with Postman** → Import POSTMAN_COLLECTION.json
3. 💾 **View Data** → Open http://localhost:8080/h2-console
4. 🔐 **Enable Firebase** → (Optional) Follow TESTING_GUIDE.md
5. 🚀 **Go Live** → Update to Stripe live keys

---

## Files in Project

```
POSTMAN_COLLECTION.json  ← Import into Postman
TESTING_GUIDE.md         ← Detailed testing walkthrough
DATA_STORAGE_GUIDE.md    ← Data architecture explained
QUICK_REFERENCE.md       ← This file
run-local.ps1            ← Start application
application.yml          ← Configuration
```

---

**Everything is ready. Start testing now!** 🚀

