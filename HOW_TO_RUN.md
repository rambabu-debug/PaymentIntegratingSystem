# How to Run Your Application

## Quick Start - One Command ✅

**Double-click this file to run everything:**
```
run-all.bat
```
This will start both backend and frontend in separate windows automatically.

---

## Or Run Separately

### Option 1: Start Backend Only
**File:** `run-backend.bat`
- Backend runs on: `http://localhost:7070`
- Database: In-memory H2 (auto-initialized)

### Option 2: Start Frontend Only  
**File:** `run-frontend.bat`
- Frontend runs on: `http://localhost:5000`
- **Note:** Backend must be running first!

---

## Access Your Application

Once both are running, open your browser and go to:

```
http://localhost:5000
```

---

## Configuration

### Backend (Spring Boot)
- **Port:** 7070
- **Database:** H2 (in-memory)
- **JWT Secret:** Set via environment variable `APP_JWT_SECRET`
- **Stripe Test Secret Key:** Set via environment variable `APP_STRIPE_SECRET_KEY`
- **Firebase Project ID:** Set via environment variable `FIREBASE_PROJECT_ID`

> **Note:** Configure secrets in environment variables or .env file. See SECRETS_MANAGEMENT.md for details.

### Frontend (React + Vite)
- **Port:** 5000
- **API Proxy:** Automatically proxies `/api` calls to backend (port 7070)
- **Hot Reload:** Enabled

---

## Troubleshooting

### If you get "Port already in use" error:

**For Windows:**
```powershell
# Kill process on port 5000 (Frontend)
netstat -ano | findstr :5000
taskkill /PID <PID> /F

# Kill process on port 7070 (Backend)
netstat -ano | findstr :7070
taskkill /PID <PID> /F
```

### If npm command not found:
The scripts already handle this by adding Node.js to PATH. If you still have issues:
1. Make sure Node.js is installed: `C:\Program Files\nodejs`
2. Restart your terminal after installing Node.js

---

## API Endpoints

When both are running, you can access:
- **Frontend UI:** http://localhost:5000
- **Backend API:** http://localhost:7070
- **H2 Database Console:** http://localhost:7070/h2-console

---

## Project Structure

```
PaymentIntegrationSystem/
├── run-all.bat              ← Start everything (RECOMMENDED)
├── run-backend.bat          ← Start backend only
├── run-frontend.bat         ← Start frontend only
├── src/                     ← Backend Spring Boot code
├── frontend/                ← React frontend code
│   └── src/
│       ├── pages/           ← Login, Register, Dashboard
│       ├── services/        ← API service calls
│       └── App.jsx
└── ...
```

---

## First Time Setup

If you're running for the first time:

1. **Double-click:** `run-all.bat`
2. Wait 5-10 seconds for backend to initialize
3. Frontend npm dependencies will auto-install
4. When both are ready, open: `http://localhost:5000`
5. Register a new merchant account or login
6. Start using the payment integration system!

---

## Keyboard Shortcuts

- **Ctrl+C** in any terminal window to stop that service
- **Alt+Tab** to switch between backend and frontend windows

---

Good luck! 🚀

