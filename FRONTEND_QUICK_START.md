# 🚀 Frontend Quick Start (5 Minutes)

## **Everything You Need**

Your React frontend is complete with:
- ✅ Login/Register pages
- ✅ Beautiful dashboard
- ✅ Transaction history
- ✅ Modern UI design
- ✅ Full API integration

---

## **⚡ Run Now (Copy & Paste)**

### **Terminal 1: Backend (Already Running)**
```powershell
# Your backend should still be running on port 3000
# If not, run:
cd 'C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem'
.\run-local.ps1 -serverPort 3000
```

### **Terminal 2: Frontend**
```powershell
# New terminal window
cd 'C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem\frontend'

# Install dependencies (first time only)
npm install

# Start frontend development server
npm run dev
```

**Wait for:**
```
VITE v5.0.8 ready in XXX ms
➜  Local:   http://localhost:5173/
```

---

## **🎯 Test It (5 Steps)**

### **Step 1: Open Browser**
```
http://localhost:5173/
```

### **Step 2: Register**
```
Email: testmerchant@store.com
Password: Test@123
Account Type: Merchant
Business Name: My Test Store
Click: Create Account
```

### **Step 3: See Dashboard**
✓ Welcome page should show
✓ Your email in top right
✓ Stats cards (0 transactions)
✓ Transaction table (empty)

### **Step 4: Logout**
Click "Logout" button → Redirects to login

### **Step 5: Login Again**
```
Email: testmerchant@store.com
Password: Test@123
```

---

## **📂 Project Layout**

```
frontend/
├── src/
│   ├── App.jsx              ← Main app
│   ├── index.css            ← Styles
│   ├── pages/
│   │   ├── Login.jsx        ← Login UI
│   │   ├── Register.jsx     ← Register UI
│   │   └── Dashboard.jsx    ← Dashboard
│   └── services/
│       └── api.js           ← Backend API calls
├── package.json             ← Dependencies
└── vite.config.js           ← Build config
```

---

## **🔗 How It Works**

```
Frontend (React)           Backend (Spring Boot)
Port 5173                  Port 3000
│                          │
├─ User clicks Register    │
│  ↓                       │
├─ Form submitted ─────→ POST /api/auth/register
│  ↓                       ↓
├─ Token received ←─── JWT token returned
│  ↓                       │
├─ Saved to localStorage   │
│  ↓                       │
├─ Redirects to dashboard  │
│  ↓                       │
├─ Shows user info         │
└─ All requests include JWT token
```

---

## **💡 What Data Shows**

### **Dashboard Stats**
- **Total Transactions**: Count of all payments
- **Total Amount**: Sum of all payment amounts
- **Successful**: Count of succeeded payments

### **Transaction Table**
Shows each transaction with:
- Transaction ID (from Stripe)
- Amount (in USD)
- Status (succeeded/failed/pending)
- Date

---

## **🎨 Frontend Features**

✅ **Authentication**
- Register with business info (merchants)
- Login securely
- JWT token management
- Auto logout

✅ **Dashboard**
- Beautiful stat cards
- Transaction history table
- User info display
- Real-time data

✅ **Design**
- Modern gradient colors (purple)
- Responsive layout
- Smooth animations
- Mobile-friendly

✅ **Integration**
- Connects to localhost:3000 backend
- Automatic API error handling
- Loading states
- Data formatting

---

## **🛠️ Troubleshooting**

### **"npm: command not found"**
→ Install Node.js from nodejs.org

### **"Cannot find module..."**
```powershell
rm -Recurse node_modules
npm install
```

### **"Connection refused on localhost:3000"**
→ Make sure backend is running on port 3000

### **"404 Not Found"**
→ Check backend API endpoints exist

### **"Token not working"**
→ Clear localStorage:
   F12 → Application → localStorage → Clear all

---

## **📱 Access Everywhere**

Frontend is accessible at:
```
http://localhost:5173/     (Local)
http://<your-ip>:5173/    (From other machines on network)
```

Backend API at:
```
http://localhost:3000/     (Local)
```

---

## **🔑 Test Credentials**

After registering, use:
```
Email: testmerchant@store.com
Password: Test@123
```

Create multiple accounts with different emails to test multiple users!

---

## **📊 Backend Verification**

To verify data in backend:
```
1. Open: http://localhost:3000/h2-console
2. JDBC URL: jdbc:h2:mem:paymentdb
3. Username: sa
4. Password: (blank)
5. SQL: SELECT * FROM users;
```

You'll see your registered user!

---

## **✨ Next Steps**

After testing:

1. **Verify Backend** 
   - Check H2 console for your user data
   - Verify merchant profile created

2. **Try Both User Types**
   - Register as Merchant
   - Register as Client
   - See different data

3. **Test API with Postman**
   - Use POSTMAN_COLLECTION.json
   - Test endpoints directly
   - Verify JWT tokens

4. **Explore Frontend Code**
   - Understand React components
   - See how API calls work
   - Learn about state management

5. **Build More Features** (Optional)
   - Add payment form
   - Add user profile page
   - Add settings page
   - Add charts

---

## **🎉 That's It!**

Your complete payment system is running:

```
✅ Backend API         http://localhost:3000
✅ Frontend Dashboard  http://localhost:5173
✅ Database (H2)       http://localhost:3000/h2-console
✅ Authentication      Working (JWT)
✅ Integration         Complete
```

**Now running:**
- React frontend with login/register
- Merchant dashboard
- Transaction history
- Beautiful UI
- Full backend integration

**Everything working together!** 🚀

---

## **Commands Cheat Sheet**

```powershell
# Start frontend
cd frontend
npm install        # First time only
npm run dev        # Start dev server

# Build for production
npm run build      # Creates dist/ folder

# View running on
# http://localhost:5173/
```

---

**Enjoy your new dashboard!** 🎨

