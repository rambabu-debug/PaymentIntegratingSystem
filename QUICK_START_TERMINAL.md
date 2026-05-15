# 🚀 QUICK START GUIDE - RUN YOUR APPLICATION

## ⚡ FASTEST WAY TO RUN

**Double-click this file:**
```
START_APPLICATION.bat
```

This will automatically:
1. ✅ Kill any existing processes (clean start)
2. ✅ Start Backend on port 7070
3. ✅ Start Frontend on port 5000
4. ✅ Install npm dependencies if needed
5. ✅ Open both in separate windows

---

## 📱 THEN OPEN YOUR BROWSER

```
http://localhost:5000
```

---

## 🔧 ALTERNATIVE OPTIONS

### Option 1: Start Backend Only
```
start-backend-clean.bat
```
- Backend runs on: `http://localhost:7070`
- Useful for: API testing with Postman

### Option 2: Start Frontend Only
```
start-frontend-clean.bat
```
- Frontend runs on: `http://localhost:5000`
- Useful for: Frontend development (backend must be running)

---

## 📝 WHAT TO DO FIRST TIME

1. **Double-click:** `START_APPLICATION.bat`
2. **Wait 10 seconds** for both services to start
3. **Open browser:** `http://localhost:5000`
4. **Register a new account** with:
   ```json
   {
     "name": "Rambo",
     "email": "ramask8179@gmail.com",
     "password": "Rambo@12345",
     "role": "merchant",
     "businessName": "mystore"
   }
   ```
5. **Login** and start testing payments!

---

## 🌐 AVAILABLE ENDPOINTS

| Service | URL | Purpose |
|---------|-----|---------|
| Frontend UI | http://localhost:5000 | Your application interface |
| Backend API | http://localhost:7070 | REST API endpoints |
| H2 Database Console | http://localhost:7070/h2-console | Database viewer |

---

## ⚙️ SYSTEM CONFIGURATION

### Backend Settings
- **Port:** 7070
- **Database:** H2 (in-memory)
- **Authentication:** JWT (token-based)
- **Payment Gateway:** Stripe (Test Mode)

### Frontend Settings
- **Port:** 5000
- **Framework:** React + Vite
- **API Proxy:** `/api` → `http://localhost:7070`

### Security Keys (Environment Variables)
- **JWT Secret:** Set via `APP_JWT_SECRET`
- **Stripe Secret Key:** Set via `APP_STRIPE_SECRET_KEY`
- **Firebase Project:** Set via `FIREBASE_PROJECT_ID`

> **Note:** Configure these in environment variables or .env file. See LOCAL_DEVELOPMENT_SETUP.md for quick setup.

---

## 🛠️ TROUBLESHOOTING

### "Port already in use" error?
The `START_APPLICATION.bat` automatically kills all existing processes. Just run it again!

### "npm not found" error?
The scripts automatically add Node.js to PATH. Make sure Node.js is installed at:
```
C:\Program Files\nodejs
```

### Can't see frontend UI?
1. Make sure backend started first (wait 8 seconds)
2. Check both terminal windows are open
3. Try: `http://localhost:5000` in browser

### Want to stop the application?
1. Go to backend window and press `Ctrl+C`
2. Go to frontend window and press `Ctrl+C`
3. Both services will stop

---

## 📊 API TESTING WITH POSTMAN

Use the Postman collection included: `POSTMAN_COLLECTION.json`

**Available Endpoints:**
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login
- `POST /api/payments/create` - Create payment
- `GET /api/payments` - Get all payments
- `POST /api/webhooks/stripe` - Stripe webhook

---

## 💡 QUICK TIPS

- Both services will show logs in their windows
- Frontend has hot-reload (changes auto-refresh)
- Backend shows SQL queries and JWT validations
- Use browser DevTools (F12) to debug frontend
- Check `http://localhost:7070/h2-console` to view database

---

## 🎉 YOU'RE ALL SET!

Everything is configured and ready to go. Just:
1. Double-click `START_APPLICATION.bat`
2. Open browser to `http://localhost:5000`
3. Register and start testing!

**Happy coding! 🚀**

