# 📝 Local Development Secrets Setup

## Quick Start (Choose One Method)

### ⚡ Fastest Way - PowerShell Command

Copy and paste this into PowerShell, replacing values with your actual secrets:

```powershell
$env:APP_JWT_SECRET = 'your-jwt-secret-key'
$env:APP_STRIPE_SECRET_KEY = 'sk_test_your_stripe_key'
$env:APP_STRIPE_WEBHOOK_SECRET = 'whsec_your_webhook_secret'
.\run-local.ps1
```

**That's it!** The app will start with your secrets.

---

### 📄 Second Option - Create .env File

1. **Create file** in project root:
   ```
   .env
   ```

2. **Add your secrets:**
   ```
   APP_JWT_SECRET=your-jwt-secret-key
   APP_STRIPE_SECRET_KEY=sk_test_your_stripe_key
   APP_STRIPE_WEBHOOK_SECRET=whsec_your_webhook_secret
   FIREBASE_PROJECT_ID=your-firebase-project-id
   SERVER_PORT=8080
   FIREBASE_ENABLED=false
   ```

3. **Run:**
   ```powershell
   .\run-local.ps1
   ```

**Note:** `.env` is in `.gitignore` - it won't be committed!

---

## 🔐 Where to Get Your Secrets

### Stripe Secret Key
1. Go to: https://dashboard.stripe.com/apikeys
2. Make sure "Viewing test data" is ON
3. Copy the **Secret Key** (starts with `sk_test_`)
4. Paste into `APP_STRIPE_SECRET_KEY`

### Stripe Webhook Secret
1. Go to: https://dashboard.stripe.com/webhooks
2. Click your webhook endpoint
3. Copy **Signing secret** (starts with `whsec_`)
4. Paste into `APP_STRIPE_WEBHOOK_SECRET`

### JWT Secret
- Can be any string (minimum 32 characters recommended)
- Example: `MySecureJWTKey123456789012345678`
- Store in `APP_JWT_SECRET`

---

## ✅ Verify It's Working

After setting secrets, you should see:
```
✓ Found available port: 8080
Tomcat started on port(s): 8080 (http)
Started Main in X.XXX seconds
```

If you see errors about missing environment variables, go back to the step above.

---

## 🚫 NEVER Do This

- ❌ Don't hardcode secrets in code
- ❌ Don't share secrets in messages/chat
- ❌ Don't commit `.env` file to GitHub
- ❌ Don't paste real keys into documentation

---

## ✨ Your .env File

Copy this template to `.env` and fill in your real values:

```
# COPY THIS TO .env AND FILL IN YOUR VALUES
# This file is in .gitignore - won't be committed to GitHub

APP_JWT_SECRET=your-secret-here-min-32-chars
APP_STRIPE_SECRET_KEY=sk_test_your_actual_key
APP_STRIPE_WEBHOOK_SECRET=whsec_your_actual_secret
FIREBASE_PROJECT_ID=your-firebase-id
SERVER_PORT=8080
FIREBASE_ENABLED=false
APP_JWT_EXPIRATION_MS=3600000
```

---

**Questions?** See: SECRETS_MANAGEMENT.md for detailed guide.

