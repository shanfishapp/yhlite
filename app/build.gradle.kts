plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.kapt)  // 保持 kapt
    alias(libs.plugins.ksp)          // 保留 ksp（但仅用于 Room，Hilt 仍用 kapt）
}

android {
    namespace = "com.yhchat.community"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.yhchat.community"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17  // 改为 JDK 11（避免兼容性问题）
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
    }

    kotlinOptions {
        jvmTarget = "17"  // 改为 JDK 11
        freeCompilerArgs = listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
            "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/*.version"
        }
    }
}

dependencies {
    // Android Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Navigation
    implementation(libs.androidx.navigation.compose) {
        version { strictly(libs.versions.androidx.navigation.get()) }
    }
    implementation(libs.hilt.navigation.compose)

    // Hilt (保持 kapt)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)  // 明确使用 kapt

    // Lifecycle Components
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Networking
    implementation(libs.bundles.network)

    // Image Loading
    implementation(libs.coil.compose)

    // Database (Room) - 保持 ksp（因为 Room 2.5+ 推荐 KSP）
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)  // Room 用 ksp

    // Security
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.security.crypto.ktx)

    // Miscellaneous Utilities
    implementation(libs.timber)
    implementation(libs.kotlinx.coroutines.android)

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

// Hilt 的 kapt 配置（移除可能冲突的参数）
kapt {
    correctErrorTypes = true
    // 移除自定义参数（避免与新版 Hilt 冲突）
    // 原参数：
    // arg("dagger.hilt.disableModulesHaveInstallInCheck", "true")
    // arg("dagger.fastInit", "enabled")
    // arg("dagger.hilt.android.internal.disableAndroidSuperclassValidation", "true")
}
