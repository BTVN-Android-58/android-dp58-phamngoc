plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.rsui.rs_network"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.rsui.rs_network"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Network
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:5.3.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.0")

    // Image loading
    implementation ("com.github.bumptech.glide:glide:5.0.5")
    implementation ("androidx.recyclerview:recyclerview:1.4.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:5.0.5")

    // Room
    implementation("androidx.room:room-runtime:2.8.3")
    implementation(libs.datastore.preferences.rxjava3)
    annotationProcessor ("androidx.room:room-compiler:2.8.3")

    implementation(libs.datastore.preferences.core)
    implementation(libs.datastore.rxjava3)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}