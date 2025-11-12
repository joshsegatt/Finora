# Quick Build & Run Scripts

# Windows PowerShell

# Clean build
Write-Host "Cleaning project..." -ForegroundColor Cyan
.\gradlew clean

# Build debug APK
Write-Host "Building debug APK..." -ForegroundColor Cyan
.\gradlew assembleDebug

# Run tests
Write-Host "Running tests..." -ForegroundColor Cyan
.\gradlew test

# Install on device
Write-Host "Installing on device..." -ForegroundColor Cyan
.\gradlew installDebug

Write-Host "Build completed successfully!" -ForegroundColor Green
Write-Host "APK location: app\build\outputs\apk\debug\app-debug.apk" -ForegroundColor Yellow
