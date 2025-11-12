# Finora - Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2024-11-12

### Added
- 📸 Receipt scanning with ML Kit OCR
- 💾 Local expense storage with Room Database
- 🏷️ Smart category inference from merchant names
- 📊 Visual reports with pie charts
- 📈 Daily, weekly, monthly, and yearly spending trends
- 📤 Export expenses to CSV and JSON formats
- 🌓 Dark mode support with Material 3
- ⚡ Offline-first architecture
- 🔒 Privacy-focused design (no data leaves device)
- ✅ Comprehensive unit and integration tests

### Features in Detail

#### Receipt Scanning
- Camera integration with CameraX
- ML Kit Text Recognition for OCR
- Automatic extraction of amount, date, and merchant
- Confidence scoring for OCR results
- Visual feedback during processing

#### Expense Management
- Add expenses manually or via receipt scan
- Edit and delete expenses
- Search and filter by category
- View transaction history with rich details
- Persistent storage with Room

#### Reports & Analytics
- Category breakdown with percentages
- Interactive pie chart visualization
- Top expenses listing
- Period selection (daily/weekly/monthly/yearly)
- Total spending calculations

#### Technical
- Clean Architecture with modular structure
- Jetpack Compose UI with Material 3
- Hilt dependency injection
- Kotlin Coroutines and Flow
- Type-safe navigation
- ProGuard/R8 optimization for release builds

### Technical Stack
- Kotlin 2.0.21
- Jetpack Compose 1.7.x
- Material 3
- Hilt 2.52
- Room 2.6.1
- ML Kit 16.0.1
- Gradle 8.9
- AGP 8.5.2
- Min SDK 26, Target SDK 35

---

## Future Roadmap

### [1.1.0] - Planned
- [ ] Recurring expenses
- [ ] Budget limits per category
- [ ] Notifications for overspending
- [ ] Multi-currency support
- [ ] Cloud backup (optional, encrypted)
- [ ] Widget for quick expense entry

### [1.2.0] - Planned
- [ ] Split expenses
- [ ] Expense attachments (multiple images)
- [ ] Advanced search filters
- [ ] Custom categories
- [ ] Tags and notes enhancement
- [ ] Export to PDF with charts

### [2.0.0] - Long Term
- [ ] Cross-device sync (optional)
- [ ] Collaboration features
- [ ] AI-powered insights
- [ ] Bank integration (read-only)
- [ ] Receipt photo gallery
- [ ] Expense trends predictions

---

## Known Issues

None reported yet. Please submit issues on GitHub.

---

## Credits

- Development: Finora Team
- Design: Material Design 3
- OCR: Google ML Kit
- Icons: Material Symbols
