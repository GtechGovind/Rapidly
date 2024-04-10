plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {

    namespace = "com.gtech.rapidly"
    compileSdk = 34

    defaultConfig {

        applicationId = "com.gtech.rapidly"
        minSdk = 24
        targetSdk = 34
        versionCode = 7
        versionName = "2024.1.1.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    // CORE
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // FIREBASE FIRESTORE
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.crashlytics)

    // VOYAGER NAVIGATION
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.screenmodel)

    // AI ML KIT
    implementation(libs.text.recognition)
    implementation(libs.play.services.mlkit.document.scanner)

    // COIL IMAGE LOADER
    implementation(libs.coil.compose)

    // GSON
    implementation(libs.gson)

    // SIGNATURE
    implementation(libs.signature.pad)

    // LOTTE ANIMATION
    implementation(libs.lottie.compose)

}