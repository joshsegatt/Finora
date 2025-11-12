# Finora - Automated Expense Tracking App

<div align="center">
  <h3>📱 Smart Receipt Scanning • 📊 Visual Reports • 🔒 Privacy-First</h3>
  <p>Finora automatically tracks your daily expenses by scanning receipts using OCR technology.</p>
</div>

---

## 🌟 Features

### Core Functionality
- **📸 Receipt Scanning**: Capture receipts with your camera and extract amount, date, and merchant using ML Kit OCR
- **💾 Local Persistence**: All data stored locally using Room Database - your privacy is guaranteed
- **📊 Visual Reports**: Interactive pie charts showing spending by category
- **🏷️ Smart Categorization**: Automatic expense categorization based on merchant name
- **📈 Trend Analysis**: Daily, weekly, monthly, and yearly spending trends
- **📤 Data Export**: Export your expenses to CSV or JSON format
- **🌓 Dark Mode**: Full Material 3 theming with dynamic light/dark mode support

### Technical Highlights
- **Clean Architecture**: Modular structure with clear separation of concerns
- **100% Kotlin**: Modern, null-safe codebase
- **Jetpack Compose**: Beautiful, declarative UI with Material 3
- **Offline-First**: Works without internet connection
- **Type-Safe Navigation**: Compose Navigation with type safety
- **Dependency Injection**: Hilt for scalable DI
- **Reactive Programming**: Kotlin Coroutines and Flow
- **Tested**: Unit and instrumentation tests included

---

## 🏗️ Architecture

### Modular Structure

```
finora/
├── app/                    # Application entry point, navigation
├── core/                   # Result wrappers, error handling, utilities
├── domain/                 # Business logic, use cases, models
├── data/                   # Data sources (Room, ML Kit)
├── ui-theme/              # Material 3 theme, colors, typography
├── features/
│   ├── expenses/          # Expense capture and list UI
│   └── reports/           # Reports and analytics UI
```

### Layer Communication

```
┌─────────────────────────────────────────┐
│              Presentation               │
│  (Compose UI, ViewModels, Navigation)   │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│               Domain                    │
│   (Use Cases, Models, Repositories)     │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│                Data                     │
│  (Room DAO, ML Kit OCR, Repository)     │
└─────────────────────────────────────────┘
```

---

## 🛠️ Tech Stack

### Core
- **Language**: Kotlin 2.0.21
- **Build**: Gradle 8.9, AGP 8.5.2
- **JDK**: 17 (toolchain configured)

### Android
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 35 (Android 15)
- **Compose**: 1.7.x with Material 3

### Libraries
| Category | Library | Version |
|----------|---------|---------|
| DI | Hilt | 2.52 |
| Database | Room | 2.6.1 |
| OCR | ML Kit Text Recognition | 16.0.1 |
| Navigation | Navigation Compose | 2.8.3 |
| Async | Coroutines | 1.8.1 |
| Logging | Timber | 5.0.1 |
| Camera | CameraX | 1.3.4 |
| Image Loading | Coil | 2.7.0 |

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: 17 or later
- **Android SDK**: API 35
- **Gradle**: 8.9 (included via wrapper)

### Build & Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/finora.git
   cd finora
   ```

2. **Open in Android Studio**
   - File → Open → Select the `finora` directory
   - Wait for Gradle sync to complete

3. **Build the project**
   ```bash
   ./gradlew assembleDebug
   ```

4. **Run on device/emulator**
   - Connect Android device (API 26+) or start emulator
   - Run → Run 'app'
   - Or via command line:
     ```bash
     ./gradlew installDebug
     ```

### Build Variants

- **Debug**: Development build with logging enabled
  ```bash
  ./gradlew assembleDebug
  ```

- **Release**: Production build with ProGuard/R8 optimization
  ```bash
  ./gradlew assembleRelease
  ```

---

## 🧪 Testing

### Unit Tests

Run all unit tests across modules:
```bash
./gradlew test
```

Run tests for specific module:
```bash
./gradlew :core:test
./gradlew :domain:test
./gradlew :data:test
```

### Instrumentation Tests

Run on connected device:
```bash
./gradlew connectedAndroidTest
```

### Test Coverage

Generate coverage report:
```bash
./gradlew jacocoTestReport
```

### What's Tested

- ✅ Result wrapper operations
- ✅ Currency and date formatting
- ✅ Expense validation logic
- ✅ Category inference from text
- ✅ OCR receipt parsing
- ✅ Room DAO operations
- ✅ Use case business logic
- ✅ Navigation flow (instrumented)

---

## 📱 App Usage

### 1. Scan a Receipt

1. Tap the **+** button on the Expenses screen
2. Grant camera permission when prompted
3. Tap **"Scan Receipt"**
4. Align receipt in camera viewfinder and capture
5. Wait for OCR processing (2-5 seconds)
6. Verify extracted data (amount, merchant, category)
7. Tap **✓** to save

### 2. View Expenses

- All expenses displayed in chronological order
- Filter by category using chips at the top
- Swipe to delete an expense
- Tap expense card for details (future feature)

### 3. Generate Reports

1. Navigate to **Reports** tab
2. Select period: Daily, Weekly, Monthly, Yearly
3. View pie chart showing category breakdown
4. Scroll down for detailed statistics
5. Tap **⋮** → **Export CSV** or **Export JSON** to share

---

## 🎨 UI/UX Features

### Material 3 Design
- Dynamic color theming
- Smooth animations and transitions
- Responsive layouts for tablets and foldables
- Accessibility: TalkBack support, semantic labels

### Dark Mode
- Automatic based on system settings
- Optimized contrast for OLED displays
- Consistent color palette across themes

### Microinteractions
- Button ripple effects
- Smooth navigation transitions
- Loading states with progress indicators
- Success/error feedback with snackbars

---

## 🔒 Privacy & Security

- **100% Offline**: No data leaves your device
- **No Analytics**: Zero tracking or telemetry
- **Local Storage**: Room database encrypted at rest (user-controlled)
- **Permissions**: Only camera access required (optional)
- **Open Source**: Full transparency, audit the code yourself

---

## 🗂️ Project Structure

```
app/
├── src/main/
│   ├── java/com/finora/expenses/
│   │   ├── MainActivity.kt
│   │   ├── FinoraApplication.kt
│   │   └── navigation/
│   │       └── FinoraNavHost.kt
│   ├── res/
│   │   ├── values/
│   │   ├── xml/
│   │   └── mipmap/
│   └── AndroidManifest.xml
└── build.gradle.kts

core/
├── src/main/java/com/finora/core/
│   ├── Result.kt
│   ├── AppError.kt
│   ├── Logger.kt
│   ├── Formatters.kt
│   └── di/CoreModule.kt
└── build.gradle.kts

domain/
├── src/main/java/com/finora/domain/
│   ├── model/
│   │   ├── Expense.kt
│   │   ├── ExpenseCategory.kt
│   │   └── ExpenseReport.kt
│   ├── repository/
│   │   ├── ExpenseRepository.kt
│   │   └── OcrRepository.kt
│   └── usecase/
│       ├── ScanReceiptUseCase.kt
│       ├── SaveExpenseUseCase.kt
│       └── GenerateReportUseCase.kt
└── build.gradle.kts

data/
├── src/main/java/com/finora/data/
│   ├── local/
│   │   ├── database/FinoraDatabase.kt
│   │   ├── dao/ExpenseDao.kt
│   │   ├── entity/ExpenseEntity.kt
│   │   └── converter/Converters.kt
│   ├── repository/
│   │   ├── ExpenseRepositoryImpl.kt
│   │   └── OcrRepositoryImpl.kt
│   ├── ocr/ReceiptParser.kt
│   └── di/DataModule.kt
└── build.gradle.kts

features/expenses/
├── src/main/java/com/finora/features/expenses/
│   ├── AddExpenseScreen.kt
│   ├── AddExpenseViewModel.kt
│   ├── ExpenseListScreen.kt
│   └── ExpenseListViewModel.kt
└── build.gradle.kts

features/reports/
├── src/main/java/com/finora/features/reports/
│   ├── ReportsScreen.kt
│   └── ReportsViewModel.kt
└── build.gradle.kts

ui-theme/
├── src/main/java/com/finora/ui/theme/
│   ├── Theme.kt
│   ├── Color.kt
│   ├── Type.kt
│   └── CategoryColors.kt
└── build.gradle.kts
```

---

## 🐛 Troubleshooting

### Build Issues

**Problem**: `java.lang.OutOfMemoryError: Java heap space`
```bash
# Increase Gradle heap size in gradle.properties
org.gradle.jvmargs=-Xmx4096m
```

**Problem**: `Unable to resolve dependency for ':app@debug/compileClasspath'`
```bash
# Invalidate caches and restart
File → Invalidate Caches → Invalidate and Restart
```

### Runtime Issues

**Problem**: Camera not working
- Grant camera permission in Settings → Apps → Finora → Permissions
- Ensure device has a camera (check AndroidManifest permissions)

**Problem**: OCR returns empty text
- Ensure good lighting when capturing receipt
- Hold device steady for 2 seconds
- Try landscape orientation for wide receipts

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use ktlint for formatting: `./gradlew ktlintFormat`
- Run Detekt: `./gradlew detekt`

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- **ML Kit**: Google's machine learning library for mobile
- **Material Design**: Google's design system
- **Jetpack Compose**: Modern Android UI toolkit
- **Open Source Community**: For amazing libraries and tools

---

## 📧 Contact

For questions or support:
- **Issues**: [GitHub Issues](https://github.com/yourusername/finora/issues)
- **Email**: support@finora.app
- **Website**: https://finora.app

---

<div align="center">
  <p>Made with ❤️ using Kotlin & Jetpack Compose</p>
  <p>
    <a href="#finora---automated-expense-tracking-app">Back to Top ⬆️</a>
  </p>
</div>
