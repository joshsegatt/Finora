# Finora Development Guide

## Code Quality Standards

### Kotlin Style
- Follow official Kotlin coding conventions
- Max line length: 120 characters
- Use meaningful variable names
- Prefer immutability (val over var)
- Use extension functions where appropriate

### Architecture Rules
1. **Domain layer**: Pure Kotlin, no Android dependencies
2. **Data layer**: Implementations, Android/3rd party libs
3. **Presentation**: UI, ViewModels, Navigation
4. **Dependency Rule**: Inner layers don't know about outer layers

### Testing Strategy
- Unit tests for business logic (domain, data)
- Integration tests for Room DAO
- UI tests for critical flows
- Target: >70% code coverage

### Git Workflow
1. Create feature branch from `main`
2. Make atomic commits with clear messages
3. Write tests before pushing
4. Create PR with description
5. Wait for CI checks
6. Merge after approval

### Commit Message Format
```
type(scope): subject

body (optional)

footer (optional)
```

Types: feat, fix, docs, style, refactor, test, chore

Example:
```
feat(expenses): add receipt scanning with ML Kit

Implement OCR scanning using ML Kit Text Recognition API.
Extract amount, date, and merchant from receipts.

Closes #42
```

## Module Dependencies

```
app
├── features:expenses
├── features:reports
├── ui-theme
└── (all modules)

features:expenses
├── domain
├── core
└── ui-theme

features:reports
├── domain
├── core
└── ui-theme

data
├── domain
└── core

domain
└── core

ui-theme
└── domain (for category colors)

core
└── (no dependencies)
```

## Adding a New Feature Module

1. Create module directory: `mkdir features/new-feature`
2. Create `build.gradle.kts`
3. Add dependencies to `:core`, `:domain`, `:ui-theme`
4. Create package: `com.finora.features.newfeature`
5. Add screens, ViewModels, navigation
6. Update `:app` navigation
7. Add tests
8. Update README

## Debugging Tips

### Enable verbose logging
```kotlin
// In FinoraApplication.kt
Logger.init(isDebug = true)
```

### Inspect Room database
```bash
adb shell
run-as com.finora.expenses
cd databases
sqlite3 finora_database
.tables
.schema expenses
SELECT * FROM expenses;
```

### Profile performance
- Android Studio Profiler
- Layout Inspector for Compose
- Enable GPU rendering profile in developer options

## Release Checklist

- [ ] All tests passing
- [ ] No lint warnings
- [ ] ProGuard rules tested
- [ ] Version code/name updated
- [ ] Changelog updated
- [ ] Build signed release APK
- [ ] Test on multiple devices
- [ ] Create Git tag
- [ ] Publish release notes

## Resources

- [Android Developers](https://developer.android.com/)
- [Kotlin Docs](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
