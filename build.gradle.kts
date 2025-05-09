// build.gradle.kts (Project level)

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.15") // Versi terbaru
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.services) apply false
}
