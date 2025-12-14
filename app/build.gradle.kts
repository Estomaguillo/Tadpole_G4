plugins {
    // Plugin para crear aplicaciones Android
    alias(libs.plugins.android.application)

    // Plugin de Kotlin para Android
    alias(libs.plugins.kotlin.android)

    // Plugin de Jetpack Compose
    alias(libs.plugins.kotlin.compose)

    // NECESARIO PARA QUE ROOM GENERE CÓDIGO
    id("kotlin-kapt")

}

android {
    namespace = "com.example.tadpole_g4"    // Usa el namespace real de tu proyecto
    compileSdk = 36                         // Usa la versión de SDK más reciente permitida

    defaultConfig {
        applicationId = "com.example.tadpole_g4"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // Activar Jetpack Compose
    buildFeatures {
        compose = true
    }

    // Versión recomendada del compilador Compose
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // ============================================================
    // DEPENDENCIAS PRINCIPALES DE ANDROID
    // ============================================================
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // ============================================================
    // JETPACK COMPOSE USANDO BOM (Versiones alineadas)
    // ============================================================
    //implementation(platform("androidx.compose:compose-bom:2024.10.00"))
    implementation(platform(libs.androidx.compose.bom))
    
    // UI Base de Compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Material 3 moderno
    implementation("androidx.compose.material3:material3")

    // ============================================================
    // Corrección importante — Inputs de texto + teclado
    // Necesario para: KeyboardOptions, PasswordTransformation, etc
    // ============================================================
    implementation("androidx.compose.ui:ui-text")
    // Forzar versión explícita (resuelve el "unresolved reference")
    //implementation("androidx.compose.ui:ui-text:1.5.3")


    // ============================================================
    // Control del estado guardado (rotación, cambio de tamaño)
    // ============================================================
    implementation("androidx.compose.runtime:runtime-saveable")

    // ============================================================
    // Íconos Material (Visibility / VisibilityOff, etc.)
    // ============================================================
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    // ============================================================
    // Navegación entre pantallas
    // ============================================================
    implementation("androidx.navigation:navigation-compose:2.8.2")

    // ============================================================
    // Integración ciclo vida + Compose
    // ============================================================
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")

    // ============================================================
    // Para Scroll View, Box, Column, etc.
    // ============================================================
    implementation("androidx.compose.foundation:foundation")

    // ============================================================
    // DEPENDENCIAS DE TESTING
    // ============================================================
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // ============================================================
    // DEPURACIÓN Y PREVISUALIZACIÓN
    // ============================================================
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // ============================================================
    // ROOM: Base de datos local de Android
    // ============================================================

    // Biblioteca principal de Room (maneja SQLite)
    implementation("androidx.room:room-runtime:2.5.2")

    // Procesador de anotaciones — genera automáticamente las clases DAO
    // Requiere tener activado el plugin "kotlin-kapt" en la parte superior
    kapt("androidx.room:room-compiler:2.5.2")

    // Extensiones para usar corrutinas (suspend fun, Flow, etc.)
    implementation("androidx.room:room-ktx:2.5.2")
}
