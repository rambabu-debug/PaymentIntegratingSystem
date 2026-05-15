# Payment Integration System - Testing Guide

## Quick Start

### 1. Run the Application
```powershell
cd 'C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem'
.\run-local.ps1
```
App will start on an available port (default 8080).

---

## Data Storage Architecture

### Current Setup (Running Now)
```
┌─────────────────────────────────────────────────────┐
│         Payment Integration System                  │
├─────────────────────────────────────────────────────┤
│  API Requests (Register/Login/Payments)             │
│           ↓                                         │
│  ┌─────────────────────────────────────┐           │
│  │    H2 In-Memory Database            │           │
│  │  ✓ Users (email, password, role)    │ ENABLED  │
│  │  ✓ Merchant Profiles                │          │
│  │  ✓ Payment Transactions              │          │
│  └─────────────────────────────────────┘           │
│           ↓ (if Firebase enabled)                   │
│  ┌─────────────────────────────────────┐           │
│  │    Firebase Realtime DB             │           │
│  │  • User Profiles (audit)             │ DISABLED │
│  │  • Audit Logs                        │ BY DEFAULT
│  │  • Transaction History               │          │
│  └─────────────────────────────────────┘           │
└─────────────────────────────────────────────────────┘
```

### Data Flow

#### Registration
```
POST /api/auth/register
  ↓
1. Check if email exists (H2)
2. Create User entity → save to H2
3. If MERCHANT: Create MerchantProfile → save to H2
4. If Firebase enabled: Store user profile in Firebase
5. Log registration activity (Firebase if enabled)
6. Return JWT token
```

#### Login
```
POST /api/auth/login
  ↓
1. Authenticate credentials (H2)
2. Generate JWT token
3. If Firebase enabled: Log login activity
4. Return JWT token
```

#### Payment Processing
```
POST /api/payments/process (protected - requires JWT)
  ↓
1. Validate JWT token
2. Process payment via Stripe
3. Save transaction to H2
4. If Firebase enabled: Store transaction in Firebase
5. Return transaction result
```

---

## Where Your Data Is Stored

### H2 Database (PRIMARY - Always On)
- **Type**: In-memory relational database
- **Connection**: `jdbc:h2:mem:paymentdb`
- **Tables**:
  - `users` — User accounts (email, password hash, role, enabled)
  - `merchant_profiles` — Merchant details (business name, user reference)
  - `transactions` — Payment transactions (stripe ID, amount, status, etc.)

**Access H2 Console:**
```
1. Open browser: http://localhost:8080/h2-console
2. JDBC URL: jdbc:h2:mem:paymentdb
3. Username: sa
4. Password: (leave blank)
5. Click Connect
```

**View Data in H2:**
```sql
-- Check users
SELECT id, email, role, enabled FROM users;

-- Check merchant profiles
SELECT id, user_id, business_name FROM merchant_profiles;

-- Check transactions
SELECT id, stripe_transaction_id, amount, status FROM transactions;
```

### Firebase (SECONDARY - Disabled by Default)
- **Type**: NoSQL cloud database
- **Status**: Currently DISABLED (firebase.enabled: false)
- **Collections** (when enabled):
  - `users/{userId}` — User profile backup
  - `audit_logs/{logId}` — Login/registration activity
  - `transactions/{transactionId}` — Payment transaction history

**Enable Firebase:**
```powershell
# 1. Download service account JSON from Firebase Console
#    (Project Settings → Service Accounts → Generate Private Key)

# 2. Run app with Firebase enabled
$env:FIREBASE_ENABLED='true'
$env:GOOGLE_APPLICATION_CREDENTIALS='C:\path\to\serviceAccountKey.json'
.\run-local.ps1
```

**View Firebase Data:**
```
1. Go to Firebase Console: https://console.firebase.google.com/
2. Select project: "paymentintegratinsystem"
3. Realtime Database → View user profiles, transactions, audit logs
4. Firestore → View audit_logs, transactions collections
```

---

## Testing with Postman

### Import Collection
1. Open Postman
2. Click "Import" (top left)
3. Select `POSTMAN_COLLECTION.json` from project root
4. Click Import

### Test Sequence

#### Test 1: Register Merchant
```
POST /api/auth/register
{
  "email": "merchant@example.com",
  "password": "SecurePassword@123",
  "role": "merchant",
  "businessName": "My Online Store"
}
```
**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "merchant@example.com",
  "role": "MERCHANT"
}
```
**✓ Data saved to H2 `users` and `merchant_profiles` tables**

#### Test 2: Verify Data in H2
1. Open http://localhost:8080/h2-console
2. Run: `SELECT * FROM users;`
3. Should see your registered merchant
4. Run: `SELECT * FROM merchant_profiles;`
5. Should see "My Online Store"

#### Test 3: Login
```
POST /api/auth/login
{
  "email": "merchant@example.com",
  "password": "SecurePassword@123"
}
```
**Response:** JWT token
**✓ Token stored in H2 session (if configured)**

#### Test 4: Register Client
```
POST /api/auth/register
{
  "email": "client@example.com",
  "password": "ClientPassword@123",
  "role": "client"
}
```
**✓ Data saved to H2**

#### Test 5: Stripe Webhook (Test)
```
POST /api/webhooks/stripe
{
  "id": "evt_test_succeeded",
  "type": "charge.succeeded",
  "data": {
    "object": {
      "id": "ch_test_1234",
      "amount": 5000,
      "currency": "usd",
      "status": "succeeded"
    }
  }
}
```
**✓ Webhook processed (no auth required)**

---

## SQL Queries to Verify Data

### Check All Users
```sql
SELECT * FROM users;
```

### Check Merchant Profiles
```sql
SELECT 
  mp.id, 
  mp.business_name, 
  u.email 
FROM merchant_profiles mp 
JOIN users u ON mp.user_id = u.id;
```

### Check Transactions
```sql
SELECT * FROM transactions;
```

### Count Records
```sql
SELECT COUNT(*) as user_count FROM users;
SELECT COUNT(*) as merchant_count FROM merchant_profiles;
SELECT COUNT(*) as transaction_count FROM transactions;
```

---

## Environment Variables (Currently Set)

```
APP_STRIPE_SECRET_KEY = sk_test_...
APP_STRIPE_WEBHOOK_SECRET = whsec_...
APP_JWT_SECRET = *** CONFIGURED ***
APP_JWT_EXPIRATION_MS = 3600000
SERVER_PORT = [auto-detected: 8080-9000]
FIREBASE_ENABLED = false (set to true to enable)
GOOGLE_APPLICATION_CREDENTIALS = (empty, required to enable Firebase)
```

---

## How to Use Firebase (Optional)

If you want to enable Firebase for audit logging and backups:

### Step 1: Get Firebase Credentials
1. Go to https://console.firebase.google.com/
2. Select "paymentintegratinsystem" project
3. Go to Project Settings (gear icon) → Service Accounts tab
4. Click "Generate New Private Key" → saves JSON to your Downloads folder

### Step 2: Run with Firebase Enabled
```powershell
$env:FIREBASE_ENABLED='true'
$env:GOOGLE_APPLICATION_CREDENTIALS='C:\Users\ramba\Downloads\serviceAccountKey.json'
.\run-local.ps1
```

### Step 3: Verify Firebase is Working
- Check console logs for "Firebase initialized with project ID: paymentintegratinsystem"
- Register a user → check Firebase Console Realtime Database → `users` collection
- Login → check `audit_logs` collection

---

## Troubleshooting

### Port Already in Use
The `run-local.ps1` script auto-detects available ports. If it still fails:
```powershell
# Find which process is using the port
netstat -aon | findstr :8080

# Kill it (replace XXXX with PID)
taskkill /PID XXXX /F

# Or specify a different port
.\run-local.ps1 -serverPort 3000
```

### H2 Console Not Loading
- Make sure app is running
- URL should be: `http://localhost:8080/h2-console` (adjust port if different)
- JDBC URL must be: `jdbc:h2:mem:paymentdb`

### Firebase Not Storing Data
- Check if `FIREBASE_ENABLED` is set to `true`
- Verify `GOOGLE_APPLICATION_CREDENTIALS` path is correct
- Check app logs for Firebase initialization messages
- Default is disabled for easy local testing

### JWT Token Errors
- Tokens expire after 1 hour (JWT_EXPIRATION_MS=3600000)
- Use fresh token from login response
- Add header: `Authorization: Bearer <token>`

---

## Summary

✅ **Currently Running**: H2 database only (all user data stored locally in-memory)
✅ **API Available**: Register, Login, Stripe webhooks
✅ **Data Location**: H2 console at http://localhost:8080/h2-console
⚠️ **Firebase**: Optional, requires service account JSON and FIREBASE_ENABLED=true

**Next Steps:**
1. Import POSTMAN_COLLECTION.json into Postman
2. Test Register, Login, and check data in H2 console
3. (Optional) Enable Firebase for production-ready audit trails

