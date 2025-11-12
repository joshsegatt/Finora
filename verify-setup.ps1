# Finora - Pre-Build Verification Script
# Run this before your first build to check prerequisites

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   Finora - Pre-Build Verification" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

$allGood = $true

# Check Java version
Write-Host "Checking Java version..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-String "version" | ForEach-Object { $_ -replace '.*version "(\d+).*', '$1' }
    if ($javaVersion -ge 17) {
        Write-Host "✓ Java $javaVersion detected" -ForegroundColor Green
    } else {
        Write-Host "✗ Java 17+ required, found version $javaVersion" -ForegroundColor Red
        $allGood = $false
    }
} catch {
    Write-Host "✗ Java not found in PATH" -ForegroundColor Red
    Write-Host "  Install from: https://adoptium.net/" -ForegroundColor Yellow
    $allGood = $false
}

# Check if gradlew exists
Write-Host "`nChecking Gradle wrapper..." -ForegroundColor Yellow
if (Test-Path "gradlew.bat") {
    Write-Host "✓ Gradle wrapper found" -ForegroundColor Green
} else {
    Write-Host "✗ gradlew.bat not found" -ForegroundColor Red
    $allGood = $false
}

# Check if settings.gradle.kts exists
Write-Host "`nChecking project structure..." -ForegroundColor Yellow
if (Test-Path "settings.gradle.kts") {
    Write-Host "✓ settings.gradle.kts found" -ForegroundColor Green
} else {
    Write-Host "✗ settings.gradle.kts not found" -ForegroundColor Red
    $allGood = $false
}

# Check modules
Write-Host "`nChecking modules..." -ForegroundColor Yellow
$modules = @("app", "core", "domain", "data", "ui-theme", "features\expenses", "features\reports")
foreach ($module in $modules) {
    if (Test-Path "$module\build.gradle.kts") {
        Write-Host "✓ Module :$($module -replace '\\', ':') configured" -ForegroundColor Green
    } else {
        Write-Host "✗ Module :$($module -replace '\\', ':') missing" -ForegroundColor Red
        $allGood = $false
    }
}

# Check Android SDK
Write-Host "`nChecking Android SDK..." -ForegroundColor Yellow
$androidHome = $env:ANDROID_HOME
if ($androidHome -and (Test-Path $androidHome)) {
    Write-Host "✓ Android SDK found at: $androidHome" -ForegroundColor Green
} else {
    Write-Host "⚠ ANDROID_HOME not set (Android Studio will handle this)" -ForegroundColor Yellow
}

# Summary
Write-Host "`n========================================" -ForegroundColor Cyan
if ($allGood) {
    Write-Host "   ✓ All checks passed!" -ForegroundColor Green
    Write-Host "   Ready to build!" -ForegroundColor Green
    Write-Host "`n   Next steps:" -ForegroundColor Cyan
    Write-Host "   1. Open project in Android Studio" -ForegroundColor White
    Write-Host "   2. Wait for Gradle sync" -ForegroundColor White
    Write-Host "   3. Run: .\gradlew assembleDebug" -ForegroundColor White
} else {
    Write-Host "   ✗ Some checks failed" -ForegroundColor Red
    Write-Host "   Please fix the issues above" -ForegroundColor Red
}
Write-Host "========================================`n" -ForegroundColor Cyan

# Offer to open in Android Studio
if ($allGood) {
    $response = Read-Host "Open project in Android Studio? (y/n)"
    if ($response -eq "y") {
        Write-Host "Looking for Android Studio..." -ForegroundColor Yellow
        
        $studioPath = "C:\Program Files\Android\Android Studio\bin\studio64.exe"
        if (Test-Path $studioPath) {
            Write-Host "Opening in Android Studio..." -ForegroundColor Green
            Start-Process $studioPath -ArgumentList (Get-Location).Path
        } else {
            Write-Host "Android Studio not found at default location" -ForegroundColor Yellow
            Write-Host "Please open manually: File → Open → $((Get-Location).Path)" -ForegroundColor Yellow
        }
    }
}
