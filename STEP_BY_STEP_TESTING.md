# Step-by-Step Testing Walkthrough

Complete guide to test your payment system and see data being stored.

---

## Setup (Do This First)

### 1. Verify App Is Running
```powershell
# If not running, start it
cd 'C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem'
.\run-local.ps1
```

Look for logs like:
```
✓ Found available port: 8080
Tomcat started on port(s): 8080 (http)
Started Main in X.XXX seconds
```

### 2. Keep Terminal Open
Leave the terminal running. You'll see logs as you test.

---

## Test 1: Import Postman Collection

### Step 1: Open Postman
- Launch Postman app
- Click "Import" (top left)

### Step 2: Select Collection File
- Browse to: `C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem`
- Select: `POSTMAN_COLLECTION.json`
- Click "Import"

### Step 3: Verify Import
- Left sidebar should show folders:
  - ✓ Authentication
  - ✓ Database Check
  - ✓ Stripe Webhooks

---

## Test 2: Register a Merchant

### Step 1: Open Request
- In Postman → Collections → Authentication
- Click: "Register Merchant"

### Step 2: Check Body
Body should show:
```json
{
  "email": "merchant@example.com",
  "password": "SecurePassword@123",
  "role": "merchant",
  "businessName": "My Online Store"
}
```

### Step 3: Change Email (Optional)
Change to a unique email to avoid conflicts:
```json
{
  "email": "mystore@test.com",
  "password": "MyStore@123",
  "role": "merchant",
  "businessName": "My Awesome Store"
}
```

### Step 4: Send Request
- Click "Send" button
- Wait for response

### Step 5: Check Response
Status should be `200 OK`

Response body:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "mystore@test.com",
  "role": "MERCHANT"
}
```

### Step 6: Copy Token
- Click on `token` value in response
- Copy the entire token (long string starting with `eyJ...`)
- Save it for later use

### ✓ Success Indicator
- Green `200` status
- Response contains token and email
- Check terminal logs for: `User registered successfully: mystore@test.com`

---

## Test 3: Verify Data in H2 Database

### Step 1: Open H2 Console
- Open browser
- Go to: `http://localhost:8080/h2-console`

### Step 2: Login to H2
- JDBC URL: `jdbc:h2:mem:paymentdb`
- Username: `sa`
- Password: (leave blank)
- Click "Connect"

### Step 3: View Users Table
- In right panel, look for "USERS" table
- Click on it
- See all columns and data

### Step 4: Run SQL Query
- Click on "SQL" tab at top
- Run query:
```sql
SELECT * FROM users;
```

### Expected Result
Your merchant should appear:
```
ID | EMAIL              | PASSWORD (HASH)  | ROLE     | ENABLED
1  | mystore@test.com   | $2a$10$... (hash)| MERCHANT | true
```

### Step 5: Check Merchant Profile
```sql
SELECT mp.*, u.email 
FROM merchant_profiles mp 
JOIN users u ON mp.user_id = u.id;
```

### Expected Result
```
ID | USER_ID | BUSINESS_NAME         | EMAIL
1  | 1       | My Awesome Store      | mystore@test.com
```

### ✓ Success Indicator
- Can see your user record
- Can see merchant profile with correct business name
- Email matches what you registered

---

## Test 4: Register a Client (Different Role)

### Step 1: Open Request
- Collections → Authentication
- Click: "Register Client"

### Step 2: Check Body
```json
{
  "email": "client@example.com",
  "password": "ClientPassword@123",
  "role": "client"
}
```

### Step 3: Change Email
```json
{
  "email": "buyer@test.com",
  "password": "Buyer@123",
  "role": "client"
}
```

### Step 4: Send Request
- Click "Send"
- Status should be `200 OK`

### Step 5: Verify in H2
- Go back to H2 console
- Run:
```sql
SELECT * FROM users;
```

### Expected Result
Two users now:
```
ID | EMAIL              | ROLE     | ENABLED
1  | mystore@test.com   | MERCHANT | true
2  | buyer@test.com     | CLIENT   | true
```

### ✓ Success Indicator
- Both users visible in USERS table
- Merchant has a merchant_profiles record
- Client does NOT have merchant_profiles record

---

## Test 5: Login (Get JWT Token)

### Step 1: Open Request
- Collections → Authentication
- Click: "Login"

### Step 2: Check Body
```json
{
  "email": "mystore@test.com",
  "password": "MyStore@123"
}
```

### Step 3: Send Request
- Click "Send"
- Status should be `200 OK`

### Step 4: Copy Token
- Response shows new JWT token
- Copy it (same as registration token, but new)

### ✓ Success Indicator
- Successful login response
- New JWT token generated
- Token format: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`

---

## Test 6: Check Audit Logs (If Firebase Enabled)

### Only if you enabled Firebase:

### Step 1: Verify Firebase is Enabled
- Check app logs for "Firebase initialized"
- If not enabled, skip this test

### Step 2: Go to Firebase Console
- Open: https://console.firebase.google.com/
- Select: "paymentintegratinsystem"
- Go to: "Realtime Database"

### Step 3: View User Profiles
- Navigate to: `users` node
- Should see your merchant's profile:
```json
{
  "userId": 1,
  "email": "mystore@test.com",
  "businessName": "My Awesome Store",
  "role": "MERCHANT",
  "registeredAt": 1672531200000,
  "storedAt": 1672531200000
}
```

### Step 4: View Audit Logs
- Navigate to: `audit_logs` node
- Should see entries for:
  - USER_REGISTERED (when you registered)
  - USER_LOGIN (each time you logged in)

Example:
```json
{
  "userId": "1",
  "action": "USER_LOGIN",
  "details": {
    "email": "mystore@test.com",
    "role": "MERCHANT",
    "loginTime": 1672531200000
  },
  "timestamp": 1672531200000
}
```

### ✓ Success Indicator
- User profiles visible in Firebase
- Audit logs show your login/registration activity
- Data matches what you registered

---

## Test 7: Stripe Webhook (Bonus)

### Step 1: Open Request
- Collections → Stripe Webhooks
- Click: "Test Webhook (charge.succeeded)"

### Step 2: Check Body
Simulates a successful payment:
```json
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

### Step 3: Send Request
- Click "Send"
- Status should be `200 OK` or `202 Accepted`

### Step 4: Check App Logs
- Watch terminal running your app
- Should see webhook processing logs

### ✓ Success Indicator
- Webhook accepted (200/202 status)
- App logs show webhook processed
- No errors in response

---

## Summary: What You've Tested

| Test | What Happened | Data Location |
|------|---------------|---------------|
| 1. Register Merchant | User saved with role MERCHANT | H2: users, merchant_profiles |
| 2. H2 Console | View user data in database | H2 console at port 8080 |
| 3. Register Client | User saved with role CLIENT | H2: users only |
| 4. Login | New JWT token generated | JWT token in memory |
| 5. Firebase | User profile & audit logs | Firebase (if enabled) |
| 6. Webhook | Stripe event received | App logs |

---

## Complete Data Flow

```
1. You register merchant
   ↓
   ✓ Saved to H2 users table
   ✓ Saved to H2 merchant_profiles table
   ✓ (If Firebase enabled) Saved to Firebase users collection
   ✓ (If Firebase enabled) Activity logged to audit_logs
   
2. You login
   ↓
   ✓ Password verified against H2
   ✓ JWT token generated
   ✓ (If Firebase enabled) Login activity logged
   
3. Stripe webhook received
   ↓
   ✓ Webhook processed
   ✓ (If enabled) Transaction logged to Firebase
```

---

## Verify Everything Works

### Quick Checklist

- [ ] App running without errors
- [ ] Postman collection imported successfully
- [ ] Register merchant request succeeds (200 OK)
- [ ] JWT token received in response
- [ ] H2 console accessible (http://localhost:8080/h2-console)
- [ ] User visible in H2 users table
- [ ] Merchant profile visible in H2
- [ ] Register client request succeeds
- [ ] Two users visible in H2
- [ ] Login request succeeds
- [ ] Webhook request succeeds (200/202)

✅ If all checked → Your system is working perfectly!

---

## Next Steps

1. **Test Payment Processing** (when available)
   - Create payment request endpoint
   - Test with Stripe test card (4242 4242 4242 4242)

2. **Enable Firebase** (optional)
   - Download service account JSON from Firebase Console
   - Set FIREBASE_ENABLED=true
   - Restart app
   - Verify data in Firebase

3. **Production Deployment** (later)
   - Update Stripe keys to live mode
   - Update JWT secret to strong key
   - Configure real database (PostgreSQL recommended)
   - Deploy to cloud (AWS, Heroku, Azure, etc.)

4. **More Testing**
   - Test payment failure scenarios
   - Test JWT expiration
   - Test concurrent registrations
   - Load testing

---

## Troubleshooting During Testing

### Postman Says "Port 8080 Not Responding"
- Check if app is still running in terminal
- Verify port in request URL matches your app port
- If app crashed, check terminal for errors

### H2 Console Won't Load
- Make sure app is still running
- Try clearing browser cache
- Check URL: http://localhost:8080/h2-console (exact)
- If port changed, use correct port

### Registration Returns Error "Email Already Exists"
- Use a different email
- Or clear H2 database (restart app)

### Can't Copy JWT Token
- Click on token value
- Right-click → Copy
- Or manually select and copy

### Firebase Not Showing Data
- Check app logs for "Firebase initialized"
- If not initialized, Firebase is disabled
- To enable: Set FIREBASE_ENABLED=true + credentials path

---

**You're all set! Start testing now!** 🎉

