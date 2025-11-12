#!/bin/bash
# Quick Build & Run Script for macOS/Linux

set -e

echo "🧹 Cleaning project..."
./gradlew clean

echo "🔨 Building debug APK..."
./gradlew assembleDebug

echo "🧪 Running tests..."
./gradlew test

echo "📱 Installing on device..."
./gradlew installDebug

echo "✅ Build completed successfully!"
echo "📦 APK location: app/build/outputs/apk/debug/app-debug.apk"
