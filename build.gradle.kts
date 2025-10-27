// Archivo de configuración de nivel superior para Gradle.
// Aquí se definen los plugins comunes y versiones compartidas
// entre todos los módulos del proyecto (como "app").

plugins {
    // Plugin de Android Application: permite compilar y empaquetar apps Android.
    alias(libs.plugins.android.application) apply false

    // Plugin de Kotlin para Android: habilita el uso de Kotlin en módulos Android.
    alias(libs.plugins.kotlin.android) apply false

    // Plugin de Compose Multiplatform (si tu proyecto lo usa).
    // Permite integrar Jetpack Compose en Android.
    alias(libs.plugins.kotlin.compose) apply false
}

// No se necesita agregar dependencias aquí.
// Este archivo solo sirve como configuración general de Gradle.
1