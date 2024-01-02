plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //id ("com.google.devtools.ksp")

    id("kotlin-kapt")
    id("io.realm.kotlin")
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")


}

android {
    namespace = "com.furkanylmz.diaryappcourse"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.furkanylmz.diaryappcourse"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled= true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.android.support:support-annotations:28.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    // Compose Navigation
    implementation ("androidx.navigation:navigation-compose:2.7.6")

    // Firebase
    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Gson
    implementation ("com.google.code.gson:gson:2.10")

    // Runtime Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // Splash API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Mongo DB Realm
    implementation("io.realm.kotlin:library-sync:1.12.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Message Bar Compose
    implementation("com.github.stevdza-san:MessageBarCompose:1.0.5")

    // One-Tap Compose
    implementation("com.github.stevdza-san:OneTapCompose:1.0.7")

    // Swipe Refresh
    implementation ("com.google.accompanist:accompanist-swiperefresh:0.27.0")

    // Data Store
    implementation ("androidx.datastore:datastore-preferences:1.1.0-alpha07")

    // Material
    implementation("androidx.compose.material:material:1.5.4")
    implementation("androidx.compose.material:material-icons-extended-android:1.5.4")

    // Desugar JDK
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // Google Maps
    implementation ("com.google.maps.android:maps-compose:4.3.0")

    // Room components
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")


    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-compiler:2.49")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")


    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics")


}

