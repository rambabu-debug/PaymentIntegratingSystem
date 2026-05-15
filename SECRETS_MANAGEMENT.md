# 🔐 Secrets Management Guide

## Overview
This guide explains how to safely manage API keys and secrets in your Payment Integration System without accidentally committing them to GitHub.

---

## ✅ What's Protected

### Files in .gitignore (Never Committed)
- `.env` - Environment variables (local only)
- `.env.local` - Local environment overrides
- `*.key` - Private key files
- `serviceAccountKey.json` - Firebase credentials

### Documentation (No Real Secrets)
- `START_HERE_NOW.md` - Only placeholder values
- `setup_complete.md` - Only placeholder values
- `QUICK_REFERENCE.md` - Only placeholder values

---

## 🔧 How to Set Your Secrets

### Method 1: PowerShell Environment Variables (Recommended)

**Temporary (Current Session Only):**
```powershell
$env:APP_JWT_SECRET = 'your-actual-jwt-secret-key'
$env:APP_STRIPE_SECRET_KEY = 'sk_test_your_actual_stripe_key'
$env:APP_STRIPE_WEBHOOK_SECRET = 'whsec_your_actual_webhook_secret'
$env:FIREBASE_PROJECT_ID = 'your-firebase-project-id'
.\run-local.ps1
```

**Permanent (Add to PowerShell Profile):**
```powershell
# Edit your PowerShell profile
notepad $PROFILE

# Add these lines (they'll load every time you open PowerShell)
$env:APP_JWT_SECRET = 'your-secret'
$env:APP_STRIPE_SECRET_KEY = 'sk_test_...'
$env:APP_STRIPE_WEBHOOK_SECRET = 'whsec_...'
```

---

### Method 2: Create .env File (Local Development)

**Step 1: Create `.env` file in project root**
```
C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem\.env
```

**Step 2: Add your secrets:**
```bash
APP_JWT_SECRET=your-actual-jwt-secret-key
APP_STRIPE_SECRET_KEY=sk_test_your_actual_stripe_key
APP_STRIPE_WEBHOOK_SECRET=whsec_your_actual_webhook_secret
FIREBASE_PROJECT_ID=your-firebase-project-id
SERVER_PORT=8080
FIREBASE_ENABLED=false
```

**Step 3: Run the application:**
```powershell
.\run-local.ps1
```

**✅ The .env file is in .gitignore - won't be committed to GitHub**

---

### Method 3: Script Parameters

```powershell
.\run-local.ps1 `
  -jwtSecret 'your-secret' `
  -stripeSecret 'sk_test_...' `
  -stripeWebhook 'whsec_...' `
  -serverPort 8080
```

---

## 📋 Required Secrets

| Variable | Where to Get | Example |
|----------|-------------|---------|
| `APP_JWT_SECRET` | Generate yourself | Any string, min 32 chars |
| `APP_STRIPE_SECRET_KEY` | Stripe Dashboard | `sk_test_...` or `sk_live_...` |
| `APP_STRIPE_WEBHOOK_SECRET` | Stripe Webhooks | `whsec_...` |
| `FIREBASE_PROJECT_ID` | Firebase Console | `paymentintegratinsystem` |

---

## 🚀 Getting Your Stripe Keys

### Test Mode Keys (Development)

1. **Go to:** https://dashboard.stripe.com/apikeys
2. **Make sure:** "Viewing test data" is enabled (toggle at top)
3. **Copy:**
   - **Secret Key:** `sk_test_...` → `APP_STRIPE_SECRET_KEY`
   - **Publishable Key:** `pk_test_...` → Store in frontend env

### Live Mode Keys (Production)

1. **Same URL:** https://dashboard.stripe.com/apikeys
2. **Toggle:** Turn off "Viewing test data"
3. **Copy:**
   - **Secret Key:** `sk_live_...` → `APP_STRIPE_SECRET_KEY`
   - **Note:** Only switch to live when ready for real payments!

---

## 🔔 Getting Stripe Webhook Secret

1. **Go to:** https://dashboard.stripe.com/webhooks
2. **Create endpoint or click existing**
3. **Copy:** Signing secret (`whsec_...`)
4. **Set:** `APP_STRIPE_WEBHOOK_SECRET`

---

## 🔄 GitHub Safety Checklist

- ✅ `.env` file is in `.gitignore`
- ✅ `serviceAccountKey.json` is in `.gitignore`
- ✅ `*.key` files are in `.gitignore`
- ✅ Documentation files have NO real secrets
- ✅ `run-local.ps1` reads from environment variables
- ✅ `.env.example` shows template without real values

---

## ⚠️ Before You Push to GitHub

```powershell
# 1. Check what will be committed
git status

# 2. Make sure NO .env or key files are listed
# 3. If .env is listed, you need to fix .gitignore
git rm --cached .env  # Remove from tracking
git add .gitignore
git commit -m "Update .gitignore to exclude secrets"

# 4. Verify no secrets in code
git log -p --all -S "sk_test_" --oneline

# 5. If found, remove from history (hard rebase)
# Contact: use git-filter-branch or BFG tool
```

---

## 🛡️ Security Best Practices

### ✅ DO
- ✅ Use environment variables
- ✅ Keep `.env` in `.gitignore`
- ✅ Use different keys for test vs. production
- ✅ Rotate keys regularly
- ✅ Use strong JWT secrets (min 32 characters)

### ❌ DON'T
- ❌ Hardcode secrets in code
- ❌ Commit `.env` to GitHub
- ❌ Share secrets in documentation
- ❌ Use same keys for test and production
- ❌ Reuse secrets across projects

---

## 🔍 Example: Checking Your Setup

```powershell
# Verify environment variables are set
echo $env:APP_JWT_SECRET
echo $env:APP_STRIPE_SECRET_KEY

# These should show your actual values (not blank)
```

---

## 🐛 Troubleshooting

### "APP_STRIPE_SECRET_KEY is blank"
**Solution:** Set it before running the app
```powershell
$env:APP_STRIPE_SECRET_KEY = 'sk_test_...'
```

### ".env file not loading"
**Solution:** Make sure you created it in the right location
```
C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem\.env
```

### "Port already in use"
**Solution:** Let run-local.ps1 auto-detect or specify:
```powershell
.\run-local.ps1 -serverPort 9090
```

---

## 📚 Related Documentation

- **START_HERE_NOW.md** - Quick start guide (no secrets)
- **.env.example** - Template for environment variables
- **.gitignore** - Files excluded from GitHub
- **run-local.ps1** - Script that reads environment variables

---

## ✅ Summary

**Your secrets are safe because:**
1. ✅ All secret files are in `.gitignore`
2. ✅ Documentation never shows real secrets
3. ✅ `run-local.ps1` reads from environment variables
4. ✅ Application uses Spring's property resolution
5. ✅ `.env.example` shows the template (no real values)

**Ready to push to GitHub!** 🚀

