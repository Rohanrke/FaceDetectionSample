import de.undercouch.gradle.tasks.download.Download

plugins {
    id("kotlin-kapt")
    id("de.undercouch.download") version "5.6.0"
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

// Define ASSET_DIR as a project property
val ASSET_DIR = "$projectDir/src/main/assets"

// Apply the external download models script
tasks.register<Download>("downloadModelFile") {
    src("https://storage.googleapis.com/mediapipe-models/face_detector/blaze_face_short_range/float16/1/blaze_face_short_range.tflite")
    dest(file("$ASSET_DIR/face_detection_short_range.tflite"))
    overwrite(false)
}

tasks.named("preBuild") {
    dependsOn("downloadModelFile")
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
    testImplementation(libs.mockitoCore)
    testImplementation(libs.mockitoKotlin)
    testImplementation(libs.kotlinxCoroutinesTest)
    androidTestImplementation(libs.androidx.espresso.core)
}