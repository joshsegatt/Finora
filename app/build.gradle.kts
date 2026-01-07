plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    // Firebase plugins comentados - descomentar após adicionar google-services.json
    // id("com.google.gms.google-services")
    // id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.finora.expenses"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.finora.expenses"
        minSdk = 26
        targetSdk = 35
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            // Read keystore path from local.properties or env
            val keystorePropertiesFile = rootProject.file("keystore.properties")
            val keystoreProperties = java.util.Properties()
            
            if (keystorePropertiesFile.exists()) {
                keystoreProperties.load(java.io.FileInputStream(keystorePropertiesFile))
            }
            
            val keystoreFile = file(
                keystoreProperties.getProperty("storeFile") 
                    ?: System.getenv("FINORA_KEYSTORE_PATH") 
                    ?: "../finora-release.keystore"
            )
            
            if (!keystoreFile.exists()) {
                throw GradleException(
                    """
                    ❌ Keystore not found at: ${keystoreFile.absolutePath}
                    
                    To create a new keystore:
                    keytool -genkey -v -keystore finora-release.keystore \
                      -alias finora -keyalg RSA -keysize 2048 -validity 10000
                    
                    Then create keystore.properties with:
                    storeFile=../finora-release.keystore
                    storePassword=YOUR_STORE_PASSWORD
                    keyAlias=finora
                    keyPassword=YOUR_KEY_PASSWORD
                    """.trimIndent()
                )
            }
            
            storeFile = keystoreFile
            
            // CRITICAL: No fallback values - fail fast if env vars missing
            storePassword = keystoreProperties.getProperty("storePassword")
                ?: System.getenv("FINORA_STORE_PASSWORD")
                ?: throw GradleException(
                    "❌ FINORA_STORE_PASSWORD not set. Add to keystore.properties or environment."
                )
            
            keyAlias = keystoreProperties.getProperty("keyAlias")
                ?: System.getenv("FINORA_KEY_ALIAS")
                ?: "finora"
            
            keyPassword = keystoreProperties.getProperty("keyPassword")
                ?: System.getenv("FINORA_KEY_PASSWORD")
                ?: throw GradleException(
                    "❌ FINORA_KEY_PASSWORD not set. Add to keystore.properties or environment."
                )
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":ui-theme"))
    implementation(project(":features:expenses"))
    implementation(project(":features:reports"))
    
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.3")
    
    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.09.03"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.52")
    ksp("com.google.dagger:hilt-android-compiler:2.52")
    
    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")
    
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.03"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.navigation:navigation-testing:2.8.3")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.52")
    kspAndroidTest("com.google.dagger:hilt-android-compiler:2.52")
}
