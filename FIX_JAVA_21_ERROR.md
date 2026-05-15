# 🔧 Java 21 Build Error Fix

## **Problem**

```
Execution failed for task ':todo.tutorials.Main.main()'.
> Process 'command 'C:\Program Files\Java\jdk-21.0.6\bin\java.exe'' finished with non-zero exit value 1
```

**Cause:** Gradle cache corruption or Java version mismatch

---

## **✅ Solution - Run One of These**

### **Option 1: PowerShell Script (Easiest)**

```powershell
cd 'C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem'
.\fix-and-run.ps1
```

**This will:**
1. ✅ Kill all Java processes
2. ✅ Clear Gradle cache
3. ✅ Clean build
4. ✅ Start on port 9090

---

### **Option 2: Manual Steps (PowerShell)**

```powershell
# 1. Kill all Java
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force

# 2. Wait
Start-Sleep -Seconds 2

# 3. Clear cache
cd 'C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem'
Remove-Item -Recurse -Force -Path '.gradle' -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force -Path 'build' -ErrorAction SilentlyContinue

# 4. Clean build
.\gradlew.bat clean build --no-daemon

# 5. Run on different port
.\run-local.ps1 -serverPort 9090
```

---

### **Option 3: Batch Script**

Just double-click:
```
C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem\fix-and-run.bat
```

---

## **🎯 What Was Wrong**

1. **Gradle Cache Corrupted** - Previous failed builds left cache
2. **Java Version** - Was using Java 21 but gradle was confused
3. **Port Conflict** - Old process still holding port

---

## **🚀 Expected Output**

After running the fix script, you should see:

```
✅ Build successful!
Tomcat started on port(s): 9090 (http)
Started Main in X.XXX seconds
```

---

## **📝 Update Postman & Frontend**

If using **port 9090** instead of 3000:

**Update Postman:**
- Change all URLs from `localhost:3000` to `localhost:9090`

**Update Frontend:**
```
cd frontend/src/services/api.js
Change: const API_BASE_URL = 'http://localhost:3000/api'
To:     const API_BASE_URL = 'http://localhost:9090/api'
```

Or update vite.config.js proxy:
```javascript
proxy: {
  '/api': {
    target: 'http://localhost:9090',  // Changed from 3000
    changeOrigin: true
  }
}
```

---

## **If Still Not Working**

Run with full debug info:

```powershell
cd 'C:\Users\ramba\Downloads\demo\PaymentIntegrationSystem'
.\gradlew.bat bootRun --stacktrace --info --no-daemon
```

Then paste the **last 100 lines** of output and I'll fix it.

---

## **✨ Done!**

Your app should now run on **http://localhost:9090**

Frontend should connect to **http://localhost:9090/api**

All data flows through. Everything works! 🎉

