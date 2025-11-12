# 🚀 Finora - First Build Instructions

## ⚠️ IMPORTANT: Before First Build

### Step 1: Generate Gradle Wrapper JAR

The `gradle-wrapper.jar` file is missing and must be generated. Choose one option:

#### Option A: Open in Android Studio (EASIEST)
1. Open Android Studio
2. File → Open → Select `c:\Users\josh\Desktop\finora`
3. When prompted "Gradle Sync", click **"Sync Now"**
4. Android Studio will automatically download the wrapper
5. Wait for sync to complete (2-5 minutes first time)

#### Option B: Use Existing Gradle Installation
If you have Gradle installed:
```powershell
cd c:\Users\josh\Desktop\finora
gradle wrapper --gradle-version 8.9
```

---

## 📋 Complete Build Process

### Prerequisites
- ✅ JDK 17 installed ([Download](https://adoptium.net/))
- ✅ Android Studio Hedgehog or later
- ✅ Android SDK API 35

### Step-by-Step Build

#### 1. Verify Setup
```powershell
cd c:\Users\josh\Desktop\finora
.\verify-setup.ps1
```

This checks:
- Java version
- Gradle wrapper
- Project structure
- Module configuration

#### 2. Open in Android Studio
```
File → Open → c:\Users\josh\Desktop\finora
```

Wait for:
- Gradle sync
- Indexing
- Dependency download

**Expected time**: 2-5 minutes on first open

#### 3. Build Debug APK
In Android Studio terminal or PowerShell:
```powershell
.\gradlew clean assembleDebug
```

Or use the build script:
```powershell
.\build-debug.ps1
```

**Expected output**:
```
BUILD SUCCESSFUL in 1m 23s
127 actionable tasks: 127 executed
```

APK location: `app\build\outputs\apk\debug\app-debug.apk`

#### 4. Run Tests
```powershell
.\gradlew test
```

**Expected**: All tests pass ✅

#### 5. Install on Device
Connect Android device or start emulator, then:
```powershell
.\gradlew installDebug
```

Or click ▶️ Run button in Android Studio

---

## 🐛 Troubleshooting

### Problem: "gradlew is not recognized"
**Solution**: Run from project root directory
```powershell
cd c:\Users\josh\Desktop\finora
.\gradlew --version
```

### Problem: "JAVA_HOME is not set"
**Solution**: 
1. Install JDK 17 from https://adoptium.net/
2. Set JAVA_HOME:
   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.9.9-hotspot"
   ```
3. Add to PATH:
   ```powershell
   $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
   ```

### Problem: "Sync failed: Connection timeout"
**Solution**: Check internet connection, Gradle needs to download dependencies (~500MB first time)

### Problem: "No toolchain found"
**Solution**: 
1. File → Settings → Build Tools → Gradle → Gradle JDK
2. Select JDK 17
3. Click Apply

### Problem: Build fails with "Out of memory"
**Solution**: Increase heap size in `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m
```

### Problem: "SDK location not found"
**Solution**: Android Studio will prompt to download SDK automatically, or:
1. File → Settings → Appearance & Behavior → System Settings → Android SDK
2. Install SDK Platform 35

---

## ✅ Build Success Checklist

After successful build, you should have:
- ✅ `app-debug.apk` in `app\build\outputs\apk\debug\`
- ✅ All tests passed
- ✅ No build errors or warnings
- ✅ App installs and runs on device

---

## 🎯 Quick Commands Reference

| Task | Command |
|------|---------|
| Clean | `.\gradlew clean` |
| Build Debug | `.\gradlew assembleDebug` |
| Build Release | `.\gradlew assembleRelease` |
| Run Tests | `.\gradlew test` |
| Install | `.\gradlew installDebug` |
| Check Dependencies | `.\gradlew dependencies` |
| Lint | `.\gradlew lint` |

---

## 📱 Running the App

### On Physical Device
1. Enable Developer Options on Android device
2. Enable USB Debugging
3. Connect via USB
4. Run: `.\gradlew installDebug`
5. Open "Finora" app on device

### On Emulator
1. In Android Studio: Tools → Device Manager
2. Create/Start emulator (API 26+)
3. Run: `.\gradlew installDebug`
4. App launches automatically

---

## 📦 APK Sizes

| Build Type | Size (approx) |
|------------|---------------|
| Debug | 15-20 MB |
| Release (minified) | 8-12 MB |

---

## 🎉 Next Steps After Build

1. ✅ Test app functionality
2. 📸 Scan a receipt
3. 📊 Generate a report
4. 🧪 Run instrumented tests
5. 🚀 Build release APK for distribution

---

## 📞 Need Help?

1. Check `PROJECT_SUMMARY.md` for overview
2. Review `QUICKSTART.md` for common issues  
3. Read `README.md` for full documentation
4. Check `CONTRIBUTING.md` for development guide

---

## 🎊 You're All Set!

Run this to build:
```powershell
cd c:\Users\josh\Desktop\finora
.\gradlew assembleDebug
```

**Happy coding! 🚀**
