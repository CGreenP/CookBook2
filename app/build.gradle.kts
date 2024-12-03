plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.cookbook"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cookbook"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
    }
}

dependencies {
    val lifecycle_version = "2.8.6"
    val nav_version = "2.8.3"

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")

    // LiveData
    //implementation("androidx.compose.runtime:runtime-livedata:1.5.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    // Lifecycle utilities for Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Coil
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")

    // Navigation
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // JSON serialization library, works with the Kotlin serialization plugin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    //OkHttp
    // define a BOM and its version
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))

    // define any required OkHttp artifacts without version
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    // splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Icon
    // Compose Material Icons Extended
    implementation("androidx.compose.material:material-icons-extended:1.7.5")
    // Simple Icons
    implementation("br.com.devsrsouza.compose.icons:simple-icons:1.1.1")
    // Feather Icons
    implementation("br.com.devsrsouza.compose.icons:feather:1.1.1")
    // Tabler Icons
    implementation("br.com.devsrsouza.compose.icons:tabler-icons:1.1.1")
    // Eva Icons
    implementation("br.com.devsrsouza.compose.icons:eva-icons:1.1.1")
    // Font Awesome
    implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.1")
    // Octicons
    implementation("br.com.devsrsouza.compose.icons:octicons:1.1.1")
    // Linea
    implementation("br.com.devsrsouza.compose.icons:linea:1.1.1")
    // Line Awesome
    implementation("br.com.devsrsouza.compose.icons:line-awesome:1.1.1")
    // Weather Icons by Erik Flowers
    implementation("br.com.devsrsouza.compose.icons:erikflowers-weather-icons:1.1.1")
    // CSS.gg
    implementation("br.com.devsrsouza.compose.icons:css-gg:1.1.1")

    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.1")

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
}