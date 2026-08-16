import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.parcelize)
    // Navigation 3 persists its back stack by serialising the NavKeys.
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

val appVersionCode = 3
val appVersionName = "1.1.0"

android {
    namespace = "wkolendo.dowodyrejestracyjne"
    compileSdk = 37

    defaultConfig {
        applicationId = "wkolendo.dowodyrejestracyjne"
        minSdk = 26
        targetSdk = 37
        versionCode = appVersionCode
        versionName = appVersionName
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
        optIn.addAll(
            "kotlin.contracts.ExperimentalContracts",
            "kotlinx.coroutines.ObsoleteCoroutinesApi",
            "kotlin.ExperimentalStdlibApi",
            "kotlin.experimental.ExperimentalTypeInference",
        )
    }
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            output.outputFileName.set("[$appVersionCode][$appVersionName]DowodyRejestracyjne.apk")
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.bundles.navigation3)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core.ktx)

    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.camerax)
    implementation(libs.androidx.camera.compose)

    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    implementation(libs.google.material)
    implementation(libs.mlkit.barcode.scanning)
    implementation(libs.timber)

    testImplementation(libs.junit)
}
