import com.m8test.util.VersionUtils

plugins {
    alias(m8test.plugins.android.application)
    alias(m8test.plugins.kotlin.android)
}

android {
    namespace = "com.m8test.language.rhino"
    compileSdk = m8test.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = m8test.versions.minSdk.get().toInt()
        targetSdk = m8test.versions.targetSdk.get().toInt()
        versionName = libs.versions.versionName.get()
        versionCode = VersionUtils.getCode(versionName!!)

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
        sourceCompatibility = JavaVersion.toVersion(m8test.versions.sourceCompatibility.get())
        targetCompatibility = JavaVersion.toVersion(m8test.versions.targetCompatibility.get())
    }
    kotlinOptions {
        jvmTarget = m8test.versions.jvmTarget.get()
    }
}

dependencies {
    compileOnly(m8test.m8test.sdk)
    implementation(libs.rhino)
}