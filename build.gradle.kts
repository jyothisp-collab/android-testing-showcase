plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.androidtestingshowcase"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.androidtestingshowcase"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        version = "1.0"
    }
}

dependencies {
    implementation(project(":app"))
}
