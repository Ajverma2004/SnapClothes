import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()

if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

val googleWebClientId = localProperties["GOOGLE_WEB_CLIENT_ID"] as String
val lensGroupId = localProperties["LENS_GROUP_ID"] as String
val snapApiToken = localProperties["SNAP_API_TOKEN"] as String
val facebookClientToken = localProperties["FACEBOOK_CLIENT_TOKEN"] as String


android {
    namespace = "com.ajverma.snapclothes"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ajverma.snapclothes"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["snapApiToken"] = snapApiToken
        manifestPlaceholders["facebookClientToken"] = facebookClientToken

        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleWebClientId\"")
        buildConfigField("String", "LENS_GROUP_ID", "\"$lensGroupId\"")
        buildConfigField("String", "SNAP_API_TOKEN", "\"$snapApiToken\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore.ktx)

    //dot indicator
    implementation(libs.dotsindicator)

    //google auth
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    //facebook auth
    implementation (libs.facebook.android.sdk)
    implementation(libs.google.firebase.auth.ktx)

    //scene view
    implementation(libs.sceneview)

    //coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    //splash screen
    implementation(libs.core.splashscreen)

    //room DB
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Snap Camera Kit
    implementation("com.snap.camerakit:camerakit:1.39.0")
    implementation("com.snap.camerakit:lenses-bundle:1.39.0")
    implementation("com.snap.camerakit:support-camerax:1.39.0")
    implementation("com.snap.camerakit:support-camera-layout:1.39.0")

    // Permissions handling
    implementation("com.google.accompanist:accompanist-permissions:0.37.2")

    //navigation
    implementation (libs.androidx.navigation.compose)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.kotlinx.serialization.json)

    //dagger hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.room.compiler)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.logging.interceptor)

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

    configurations.all {
        exclude(group = "com.intellij", module = "annotations")
    }
}