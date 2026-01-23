plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("ru.practicum.android.diploma.plugins.developproperties")

    alias(libs.plugins.ksp)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "ru.practicum.android.diploma"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.practicum.android.diploma"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            type = "String",
            name = "API_ACCESS_TOKEN",
            value = "\"${developProperties.apiAccessToken}\""
        )
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
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(libs.androidX.core)
    implementation(libs.androidX.appCompat)

    // UI (XML)
    implementation(libs.ui.material)
    implementation(libs.ui.constraintLayout)

    // Navigation (Fragments)
    implementation(libs.nav.fragmentKtx)
    implementation(libs.nav.uiKtx)

    // Retrofit + OkHttp
    implementation(libs.network.retrofit)
    implementation(libs.network.retrofitGson)
    implementation(libs.network.okhttp)
    implementation(libs.network.okhttpLogging)

    // Room (KSP)
    implementation(libs.db.roomRuntime)
    implementation(libs.db.roomKtx)
    ksp(libs.db.roomCompiler)

    // Glide (через kapt)
    implementation(libs.img.glide)
    kapt(libs.img.glideCompiler)

    // Coroutines
    implementation(libs.kotlinx.coroutinesAndroid)

    // Lifecycle
    implementation(libs.androidX.lifecycleRuntimeKtx)
    implementation(libs.androidX.lifecycleViewModelKtx)

    // Fragments helpers
    implementation(libs.androidX.fragmentKtx)

    // Koin
    implementation(libs.di.koinAndroid)
    implementation(libs.di.koinCompose)

    // Compose (через BOM)
    implementation(platform(libs.ui.composeBom))
    implementation(libs.ui.composeUi)
    implementation(libs.ui.composeRuntime)
    implementation(libs.ui.composeUiToolingPreview)
    implementation(libs.ui.composeMaterial3)
    implementation(libs.ui.activityCompose)

    // Часто нужно почти сразу (scroll, basic layouts)
    implementation(libs.ui.composeFoundation)

    debugImplementation(libs.ui.composeUiTooling)

    // Tests
    testImplementation(libs.unitTests.junit)
    androidTestImplementation(libs.uiTests.junitExt)
    androidTestImplementation(libs.uiTests.espressoCore)
}
