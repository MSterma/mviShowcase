plugins {
    alias(libs.plugins.android.library)
    // id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.mvishowcase.core.domain"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
}
