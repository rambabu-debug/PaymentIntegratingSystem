# 🧪 TESTING THE REGISTRATION FIX

## Quick Test (5 minutes)

### Step 1: Start the Application
```
Double-click: START_APPLICATION.bat
```

Wait for both windows to show:
- Backend: "Started Main in X seconds"
- Frontend: "VITE v..." with "Local: http://localhost:5001"

### Step 2: Open Browser
```
http://localhost:5001
```

### Step 3: Test Registration
1. Click **"Create Account"** button
2. Fill in the form:
   ```
   Full Name: Rambo
   Email: ramask8179@gmail.com
   Password: Rambo@12345
   Account Type: Merchant (Seller)
   Business Name: mystore
   ```
3. Click **"Create Account"** button

### Step 4: Expected Result
✅ You should see the **Dashboard** page appear!

---

## Advanced Test (With Debugging)

### If Registration Still Fails:

#### Step 1: Open Developer Tools
```
Press F12 in browser
```

#### Step 2: Go to Network Tab
- Click **Network** tab
- Clear any previous requests

#### Step 3: Attempt Registration Again
1. Fill form as above
2. Click "Create Account"
3. Look for request named **`register`** in Network tab

#### Step 4: Check the Request
Click on the **`register`** request:
- Check **Headers** tab
- Should show: `POST http://localhost:7070/api/auth/register`
- Should have: `Content-Type: application/json`
- Should have: `Authorization: Bearer ...` (if logged in)

#### Step 5: Check the Response
- Go to **Response** tab
- Should show either:
  - ✅ Success with token: `{"token":"...","email":"...","role":"merchant"}`
  - ❌ Error message if something failed

#### Step 6: Check for CORS Errors
- Go to **Console** tab
- Look for red errors like:
  - `Access-Control-Allow-Origin`
  - `CORS error`
  - `Failed to fetch`

---

## Testing with Postman

### Step 1: Register via Postman
If you want to verify the backend is working independently:

```
URL: POST http://localhost:7070/api/auth/register

Body (JSON):
{
  "name": "Rambo",
  "email": "ramask8179@gmail.com",
  "password": "Rambo@12345",
  "role": "merchant",
  "businessName": "mystore"
}

Headers:
Content-Type: application/json
```

### Step 2: Expected Postman Response
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "ramask8179@gmail.com",
  "role": "merchant"
}
```

✅ If this works, backend is fine!

---

## Troubleshooting Guide

### Problem 1: "Registration Failed" - No Error Details
**Possible Cause:** Network request not reaching backend

**Solution:**
1. Check if backend is running: `http://localhost:7070/h2-console` should work
2. Check Network tab in DevTools - is the request being sent?
3. Restart both applications

### Problem 2: "CORS error" in Console
**Possible Cause:** CORS not enabled on backend

**Solution:**
1. Verify `SecurityConfig.java` was updated with CORS
2. Check that file has the `corsConfigurationSource()` method
3. Rebuild: `gradlew.bat build`
4. Restart backend

### Problem 3: "401 Unauthorized" Response
**Possible Cause:** JWT validation failing

**Solution:**
1. This shouldn't happen on registration (it's public)
2. Clear browser cache: `Ctrl+Shift+Delete`
3. Restart browser

### Problem 4: Frontend on Port 5000 Instead of 5001
**Solution:** The actual port doesn't matter - just use whatever port shows up

---

## Quick Diagnostic Checklist

- [ ] Backend running on port 7070?
  - Test: `http://localhost:7070` should load without error
- [ ] Frontend running on port 5001 or 5000?
  - Test: `http://localhost:5001` should load the login page
- [ ] Network request going to 7070?
  - Check: DevTools → Network → POST request URL
- [ ] Response has token?
  - Check: DevTools → Network → Response tab shows `"token":"..."`
- [ ] Page redirects to dashboard?
  - Should see dashboard content after registration

---

## Success Indicators

✅ Registration works when you see:
1. Form accepts your input
2. Loading spinner shows briefly
3. Page redirects to dashboard
4. Dashboard shows "Merchant Dashboard" or similar content

---

## What to Do If Still Failing

Provide me with:
1. Screenshot of browser error message
2. Console error (F12 → Console → red error text)
3. Network request response (F12 → Network → click request → Response tab)
4. Backend logs (what your backend console shows)

---

## Quick Links

- **Frontend:** http://localhost:5001
- **Backend API:** http://localhost:7070
- **Database:** http://localhost:7070/h2-console
- **Postman Collection:** `POSTMAN_COLLECTION.json`

---

**Now go test it! Good luck! 🚀**

