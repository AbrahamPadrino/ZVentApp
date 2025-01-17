plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    // Hilt
    alias(libs.plugins.dagger.hilt.android)

    // Para las anotaciones KSP (Kotlin Symbol Processors)
    alias(libs.plugins.ksp)

    // Para las anotaciones kotlin-kapt (Kotlin Annotation Plugin Tool)
    alias(libs.plugins.kotlin.kapt)

    //Navegacion segura
    alias(libs.plugins.navigation.safeargs)
}

android {
    namespace = "com.example.z_ventapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.z_ventapp"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Room
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Codigo de barras y QR
    implementation(libs.zxing.android.embedded)
    implementation(libs.core)

    // Grafico para reporte
    implementation(libs.mpandroidchart)

    // AdMob
    implementation(libs.play.services.ads)
    implementation(libs.firebase.analytics)

    // Libreria
    implementation(libs.libreria.pcs)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}