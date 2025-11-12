# Quick Start Guide - Finora

## 🚀 5-Minute Setup

### Step 1: Prerequisites Check
```bash
java -version    # Should be 17+
```

### Step 2: Clone & Open
```bash
cd c:\Users\josh\Desktop\finora
```
Open Android Studio → Open this directory

### Step 3: Build
Wait for Gradle sync, then:
```bash
.\gradlew assembleDebug
```

### Step 4: Run
Connect device or start emulator, then:
```bash
.\gradlew installDebug
```

Or click ▶️ Run in Android Studio

---

## 📱 Using the App

### First Launch
1. App opens to **Expenses** screen (empty)
2. Tap **+** button to add first expense

### Add Expense via Receipt
1. Tap **+** button
2. Grant **Camera** permission
3. Tap **"Scan Receipt"**
4. Point camera at receipt
5. Take photo
6. Wait 2-3 seconds for OCR
7. Verify amount/category
8. Tap **✓** to save

### Add Expense Manually
1. Tap **+** button
2. Skip camera, fill form:
   - Amount (required)
   - Category (required)
   - Merchant (optional)
   - Notes (optional)
3. Tap **✓** to save

### View Reports
1. Tap **Reports** tab at bottom
2. Select period: Daily/Weekly/Monthly/Yearly
3. View pie chart and breakdown
4. Tap **⋮** → Export to share data

---

## 🧪 Testing

### Run All Tests
```bash
.\gradlew test
```

### Run Specific Module Tests
```bash
.\gradlew :core:test
.\gradlew :domain:test
.\gradlew :data:test
```

### Run Instrumented Tests (requires device)
```bash
.\gradlew connectedAndroidTest
```

---

## 🐛 Common Issues

### "No matching toolchain found"
**Fix**: Install JDK 17
- Download from: https://adoptium.net/
- Restart Android Studio

### "Unable to resolve dependency"
**Fix**: 
```bash
.\gradlew --refresh-dependencies
```
Or: File → Invalidate Caches → Restart

### Camera not working
**Fix**: Grant permission manually
- Settings → Apps → Finora → Permissions → Camera

### OCR returns nothing
**Fix**: 
- Ensure good lighting
- Hold steady for 2 seconds
- Try landscape for wide receipts

---

## 📂 Project Structure Quick Reference

```
finora/
├── app/              → Main entry point
├── core/             → Shared utilities
├── domain/           → Business logic
├── data/             → Data sources
├── ui-theme/         → Material 3 theme
└── features/
    ├── expenses/     → Expense screens
    └── reports/      → Reports screens
```

---

## 🔧 Development Commands

| Task | Command |
|------|---------|
| Clean | `.\gradlew clean` |
| Build Debug | `.\gradlew assembleDebug` |
| Build Release | `.\gradlew assembleRelease` |
| Run Tests | `.\gradlew test` |
| Install | `.\gradlew installDebug` |
| Lint | `.\gradlew lint` |

---

## 📚 Learn More

- **README.md**: Full documentation
- **CONTRIBUTING.md**: Development guide
- **CHANGELOG.md**: Version history

---

## 🎯 Next Steps

1. ✅ Build and run the app
2. 📸 Scan your first receipt
3. 📊 Check the reports
4. 🔧 Explore the code
5. 🤝 Contribute improvements

---

**Need Help?**
- GitHub Issues: Report bugs
- README: Full documentation
- Code: Well-commented and tested

Happy coding! 🚀
