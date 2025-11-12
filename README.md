# 💰 Finora - Premium Finance Manager

![License](https://img.shields.io/badge/license-Proprietary-red.svg)
![Platform](https://img.shields.io/badge/platform-Android-green.svg)
![Kotlin](https://img.shields.io/badge/kotlin-2.0.0-blue.svg)

A sophisticated personal finance application with AI-powered insights and multi-currency support.

---

## 🚀 Key Features

- **Multi-Currency Support** - 9 currencies (USD, EUR, GBP, JPY, CAD, AUD, CHF, CNY, INR)
- **AI Categorization** - ML Kit OCR + Gemini Pro for receipt scanning
- **Budget Management** - Category-specific budgets with real-time alerts
- **Smart Insights** - Personalized spending analysis and recommendations
- **Visual Analytics** - Interactive charts with animated bar graphs
- **Cloud Sync** - Firebase Firestore for cross-device synchronization
- **Premium Tiers** - Free, Plus, and Pro subscriptions

---

## 🛠️ Tech Stack

**Architecture**: MVVM + Clean Architecture  
**DI**: Hilt • **Database**: Room + Firestore  
**UI**: Jetpack Compose + Material 3  
**AI/ML**: ML Kit OCR + Gemini Pro API  
**Reactive**: Kotlin Coroutines + Flow

### Modules
```
app/          # Entry point
core/         # Utilities, error handling
domain/       # Business logic, use cases
data/         # Repositories, Room, Firestore
ui-theme/     # Design system, 3D components
features/
  ├── auth/      # Authentication
  ├── expenses/  # Expense tracking
  ├── budgets/   # Budget management
  ├── insights/  # AI insights
  └── reports/   # Visual reports
```

---

## 🔧 Build Instructions

### Prerequisites
- Android Studio Ladybug 2024.2.1+
- JDK 17
- Android SDK 34

### Setup
1. Add `local.properties`:
```properties
GEMINI_API_KEY=your_key
```
2. Add `google-services.json` to `app/`
3. Build:
```bash
./gradlew assembleDebug installDebug
```

---

## 🔐 License & Copyright

**© 2024-2025 Josh Segatt. All Rights Reserved.**

This software is **proprietary and confidential**. 

### Prohibited Actions
- ❌ Copying, cloning, or forking without permission
- ❌ Commercial use or redistribution
- ❌ Reverse engineering
- ❌ Unauthorized modification

**For licensing inquiries**: josh@finora.app

---

## 📞 Contact

**Developer**: Josh Segatt  
**Repository**: Private  
**Support**: support@finora.app

---

**Built with ❤️ using Kotlin & Jetpack Compose**
