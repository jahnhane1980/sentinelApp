plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose) // Schaltet den Compose-Compiler für Kotlin 2.0 scharf
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sentinel.deeptrace"

    // Auf 35 angehoben, um die Fehlermeldungen der neuen Libraries zu beheben
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sentinel.deeptrace"
        minSdk = 26
        targetSdk = 35 // Empfohlen für maximale Kompatibilität mit API 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        // Deine bestehende Java 17 Konfiguration
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // AndroidX & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose (BOM verwaltet die Einzelversionen)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Icons (Erweitert für das Settings-Zahnrad und die Navigation)
    implementation("androidx.compose.material:material-icons-extended:1.7.0")

    // Room & KSP (Lokale Datenbank für Assets & Transaktionen)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // ViewModel & Compose Integration
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")

    // DataStore (Permanente Speicherung von User-Einstellungen wie Währung/Haptik)
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
}