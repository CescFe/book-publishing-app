import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.2.21"
    id("com.diffplug.spotless") version "8.1.0"
}

android {
    namespace = "org.cescfe.book_publishing_app"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "org.cescfe.book_publishing_app"
        minSdk = 24
        targetSdk = 36
        versionCode = 3
        versionName = "1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            val devProperties = Properties()
            val devPropertiesFile = rootProject.file("app/dev.properties")
            if (!devPropertiesFile.exists()) {
                throw GradleException("dev.properties file not found. Please create app/dev.properties with base.url property.")
            }
            devProperties.load(FileInputStream(devPropertiesFile))
            val baseUrl = devProperties.getProperty("base.url")
                ?: throw GradleException("base.url property not found in dev.properties")

            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            val proProperties = Properties()
            val proPropertiesFile = rootProject.file("app/pro.properties")
            if (!proPropertiesFile.exists()) {
                throw GradleException("pro.properties file not found. Please create app/pro.properties with base.url property.")
            }
            proProperties.load(FileInputStream(proPropertiesFile))
            val baseUrl = proProperties.getProperty("base.url")
                ?: throw GradleException("base.url property not found in pro.properties")

            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.kotlinx)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint("1.3.1")
            .editorConfigOverride(
                mapOf(
                    "ktlint_code_style" to "android_studio",
                    "max_line_length" to "120",
                    "ktlint_standard_package-name" to "disabled",
                    "ktlint_standard_function-naming" to "disabled",
                ),
            )
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint("1.3.1")
            .editorConfigOverride(
                mapOf(
                    "ktlint_standard_package-name" to "disabled",
                ),
            )
    }
}
