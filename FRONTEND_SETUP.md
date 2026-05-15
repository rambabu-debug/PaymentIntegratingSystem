# 🎨 React Frontend - Complete Setup & Run Guide

## **What Was Created**

A complete React dashboard frontend with:
- ✅ User authentication (Login/Register)
- ✅ Merchant dashboard with stats
- ✅ Transaction history table
- ✅ Beautiful modern UI
- ✅ Fully integrated with backend API
- ✅ JWT token management
- ✅ Responsive design

---

## **📁 Project Structure**

```
frontend/
├── package.json              ← Dependencies
├── vite.config.js            ← Vite configuration
├── index.html                ← HTML entry point
└── src/
    ├── main.jsx              ← React app entry
    ├── App.jsx               ← Main app component
    ├── index.css             ← Global styles
    ├── pages/
    │   ├── Login.jsx         ← Login page
    │   ├── Register.jsx      ← Registration page
    │   └── Dashboard.jsx     ← Main dashboard
    └── services/
        └── api.js            ← API client (Axios)
```

---

## **🚀 Quick Start**

### **Step 1: Navigate to Frontend Directory**

```powershell
cd 'C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem\frontend'
```

### **Step 2: Install Dependencies**

```powershell
npm install
```

This installs:
- React & React DOM
- React Router
- Axios (for API calls)
- Vite (build tool)

### **Step 3: Start Development Server**

```powershell
npm run dev
```

Output should show:
```
VITE v5.0.8  ready in XXX ms

➜  Local:   http://localhost:5173/
```

### **Step 4: Open in Browser**

```
http://localhost:5173/
```

---

## **🎯 Using the Frontend**

### **Step 1: Register**

1. Click "Register here" link
2. Fill in the form:
   - Email: `merchant@test.com`
   - Password: `Test@123`
   - Account Type: `Merchant`
   - Business Name: `My Store`
3. Click "Create Account"
4. You'll be redirected to dashboard

### **Step 2: View Dashboard**

Dashboard shows:
- **Total Transactions** - Count of all payments
- **Total Amount** - Sum of all transaction amounts
- **Successful** - Count of successful transactions
- **Recent Transactions** - Table with transaction details
- **Account Info** - Your email and role

### **Step 3: Login**

1. Click "Login here"
2. Enter your credentials
3. Click "Login"
4. Access your dashboard

### **Step 4: Logout**

Click the "Logout" button in top right corner.

---

## **🔗 API Integration**

The frontend automatically connects to your backend at `http://localhost:3000`

**Endpoints used:**
```
POST /api/auth/register     → Register new user
POST /api/auth/login        → Login & get JWT
GET /api/transactions       → Get transaction history
```

**JWT Token Management:**
- Automatically saved to `localStorage` after login
- Automatically added to all API requests
- Automatically sent in `Authorization: Bearer <token>` header

---

## **🎨 Features**

### **Authentication**
- ✅ Register with email, password, role, business name
- ✅ Login with email & password
- ✅ JWT token storage
- ✅ Automatic token refresh in API calls
- ✅ Logout functionality

### **Dashboard**
- ✅ Display user email & account type
- ✅ Show transaction statistics
- ✅ Display transaction history in table
- ✅ Status badges (succeeded, pending, failed)
- ✅ Format amounts as currency

### **UI/UX**
- ✅ Modern gradient design
- ✅ Responsive on all devices
- ✅ Smooth animations & transitions
- ✅ Error handling & validation
- ✅ Loading states
- ✅ Clean, professional styling

---

## **🧪 Testing the Full Flow**

### **1. Register a New Merchant**
```
Frontend: http://localhost:5173/
Email: merchant@store.com
Password: Store@123
Account Type: Merchant
Business Name: My Awesome Store
```

### **2. Check Backend**
```
Backend H2 Console: http://localhost:3000/h2-console
SQL: SELECT * FROM users;
SQL: SELECT * FROM merchant_profiles;
```

### **3. Login Again**
```
Frontend: http://localhost:5173/
Email: merchant@store.com
Password: Store@123
```

### **4. View Dashboard**
```
✓ See your email in navbar
✓ See account type (MERCHANT)
✓ See transaction stats (0 initially)
```

---

## **🐛 Troubleshooting**

### **Frontend Won't Start**

```powershell
# Clear node_modules and reinstall
rm -Recurse node_modules
npm install
npm run dev
```

### **"API Connection Failed"**

Check:
1. Backend is running on port 3000
2. Run: `npm run dev` (frontend on 5173)
3. Check browser console for errors (F12)

### **Login Returns Error**

1. Make sure user exists in backend
2. Check email/password are correct
3. Verify backend is running

### **Can't Access Dashboard**

1. Check if token is in browser localStorage (F12 → Application → localStorage)
2. Try logging in again
3. Clear browser cache

---

## **📱 Customization**

### **Change Colors**
Edit `src/index.css`:
```css
/* Change primary color from purple to blue */
background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
/* to */
background: linear-gradient(135deg, #0066ff 0%, #0044cc 100%);
```

### **Add New Pages**

1. Create new file in `src/pages/`:
```jsx
export default function NewPage() {
  return <div>New Page Content</div>
}
```

2. Import in `src/App.jsx`:
```jsx
import NewPage from './pages/NewPage'
```

### **Add New API Endpoints**

Update `src/services/api.js`:
```javascript
export const newAPI = {
  getData: () => api.get('/endpoint'),
  postData: (data) => api.post('/endpoint', data),
}
```

Then use in components:
```jsx
import { newAPI } from '../services/api'

const data = await newAPI.getData()
```

---

## **🚀 Deployment**

### **Build for Production**

```powershell
npm run build
```

Creates optimized `dist/` folder ready to deploy.

### **Deploy Options**

1. **Vercel** (Easiest)
   ```powershell
   npm install -g vercel
   vercel
   ```

2. **Netlify**
   - Drag & drop `dist/` folder

3. **GitHub Pages**
   ```powershell
   npm run build
   # Push dist/ to gh-pages branch
   ```

---

## **📊 Backend Integration**

Frontend communicates with backend endpoints:

```
Backend (Running on localhost:3000)
│
├─ POST /api/auth/register
│  Input: { email, password, role, businessName }
│  Output: { token, email, role }
│
├─ POST /api/auth/login
│  Input: { email, password }
│  Output: { token, email, role }
│
├─ GET /api/auth/me
│  Headers: Authorization: Bearer <token>
│  Output: { email, role }
│
└─ GET /api/transactions
   Headers: Authorization: Bearer <token>
   Output: [{ id, amount, status, ... }]
```

---

## **✨ Future Enhancements**

You can add:

1. **Payment Processing**
   - Stripe integration
   - Payment form
   - Transaction creation

2. **User Profile**
   - Edit profile
   - Change password
   - Update business info

3. **Analytics**
   - Charts & graphs
   - Revenue reports
   - Customer statistics

4. **Admin Panel**
   - User management
   - Transaction monitoring
   - Reports

5. **Notifications**
   - Email alerts
   - In-app notifications
   - Transaction confirmations

---

## **📝 Commands Reference**

```powershell
# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build

# Preview production build locally
npm run preview

# Clear cache and reinstall
rm -Recurse node_modules
npm install
```

---

## **🎉 You're All Set!**

Your React frontend is ready to use with the backend API.

**Quick Checklist:**
- ✅ Frontend files created
- ✅ React components built
- ✅ Styling complete
- ✅ API integration ready
- ✅ Authentication working
- ✅ Dashboard functional

**Next Steps:**
1. Run `npm install` in frontend directory
2. Run `npm run dev`
3. Open `http://localhost:5173/`
4. Register and test!

---

**Frontend is ready! 🚀**

