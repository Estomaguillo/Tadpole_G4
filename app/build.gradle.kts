plugins {
    // Plugin para crear aplicaciones Android
    alias(libs.plugins.android.application)

    // Plugin de Kotlin para Android
    alias(libs.plugins.kotlin.android)

    // Plugin de Jetpack Compose
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.tadpole_g4" // Nombre de tu paquete
    compileSdk = 36 // SDK con el que compila la app

    defaultConfig {
        applicationId = "com.example.tadpole_g4" // ID único del paquete
        minSdk = 24 // Mínimo SDK soportado (Android 7.0)
        targetSdk = 36 // SDK objetivo
        versionCode = 1
        versionName = "1.0"

        // Runner para pruebas instrumentadas
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Desactiva la minificación (útil durante desarrollo)
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Configuración de compatibilidad con Java 17 (recomendado para Compose)
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

    // Versión del compilador de Compose
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    // Excluir licencias duplicadas al empaquetar
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
    // JETPACK COMPOSE (usando BOM para mantener versiones alineadas)
    // ============================================================
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))

    // Librerías base de Compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    // Material 3 (interfaz moderna de Android)
    implementation("androidx.compose.material3:material3")

    // ============================================================
    // ESTADO Y GUARDADO DE DATOS EN COMPOSE
    // Permite usar `rememberSaveable` para conservar datos al rotar pantalla
    // ============================================================
    implementation("androidx.compose.runtime:runtime-saveable")

    // ============================================================
    // ICONOS Y COMPONENTES VISUALES
    // Incluye íconos por defecto como Visibility/VisibilityOff
    // ============================================================
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    // ============================================================
    // NAVEGACIÓN ENTRE PANTALLAS EN COMPOSE
    // ============================================================
    implementation("androidx.navigation:navigation-compose:2.8.2")

    // ============================================================
    // CICLO DE VIDA Y COMPATIBILIDAD CON COMPOSE
    // ============================================================
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")

    // Soporte de input y teclado
    implementation("androidx.compose.foundation:foundation")

    // ============================================================
    // DEPENDENCIAS PARA PRUEBAS
    // ============================================================
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // ============================================================
    // HERRAMIENTAS DE DEPURACIÓN Y PREVISUALIZACIÓN
    // ============================================================
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
