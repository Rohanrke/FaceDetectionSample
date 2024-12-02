plugins {
    id("kotlin-kapt")
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.rohan.face.detection.media.scan"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":base"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":media-scan-kit"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.activity.ktx)
    implementation(libs.mediapipe.sdk)
    implementation(libs.recyclerview)
    implementation(libs.glide)
    implementation(libs.dagger)
    implementation(libs.cameraXcore)

    annotationProcessor(libs.glide.compiler)
    kapt(libs.daggerCompiler)

    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}