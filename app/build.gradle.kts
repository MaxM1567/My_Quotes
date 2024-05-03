plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.my_quotes"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.my_quotes"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

buildscript {
    extra.apply {
        set("room_version", "2.5.2")
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation(libs.room.common)
    implementation(libs.room.runtime)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    annotationProcessor(libs.room.compiler)

//    // room (для БД)
//    implementation(androidx.room:room-runtime:2.2.5)
//    annotationProcessor(androidx.room:room-compiler:2.2.5)
//
//    // Recycler view
//    implementation(androidx.recyclerview:recyclerview:1.1.0)
//
//    // Scalable Size Unit (support for different screen sizes)
//    implementation(com.intuit.sdp:sdp-android:1.0.6)
//    implementation(com.intuit.ssp:ssp-android:1.0.6)
//
//    // Material Design
//    implementation(com.google.android.material:material:1.1.0)
//
//    // Rounded ImageView
//    implementation (com.makeramen:roundedimageview:2.3.0)
}