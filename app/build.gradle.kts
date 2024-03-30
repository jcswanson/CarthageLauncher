// Apply necessary plugins for the project
plugins {
    id("com.android.application") // Apply the android application plugin
    id("org.jetbrains.kotlin.android") // Apply the kotlin android plugin
    id("com.google.gms.google-services") // Apply the google services plugin
    id("com.google.firebase.crashlytics") // Apply the firebase crashlytics plugin
    id("kotlin-kapt") // Apply the kotlin kapt plugin
    id("dagger.hilt.android.plugin") // Apply the dagger hilt plugin
}

// Configure the android project
android {
    // Set the namespace for the project
    namespace = "com.codesteem.mylauncher"
    // Set the compile and target SDK versions
    compileSdk = 34

    defaultConfig {
        // Set the application ID, minimum and target SDK versions, version code and name
        applicationId = "com.codesteem.mylauncher"
        minSdk = 29
        targetSdk = 34
        versionCode = 2
        versionName = "1.1.0"

        // Set the test instrumentation runner
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Print the PERPLEXITY_API_KEY property
        println("PERPLEXITY_API_KEY: ${findProperty("PERPLEXITY_API_KEY")}")

        // Set the buildConfigField with the PERPLEXITY_API_KEY property
        buildConfigField("String", "PERPLEXITY_API_KEY", "${findProperty("PERPLEXITY_API_KEY")}")

        // Set the signing config for the debug build variant
        signingConfig = signingConfigs.getByName("debug")
    }

    // Configure the build types
    buildTypes {
        release {
            // Set the isMinifyEnabled flag for the release build variant
            isMinifyEnabled = false

            // Set the proguard files for the release build variant
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Configure the compile options
    compileOptions {
        // Set the source and target compatibility to Java 17
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // Configure the kotlin options
    kotlinOptions {
        // Set the jvmTarget to Java 17
        jvmTarget = "17"
    }

    // Configure the build features
    buildFeatures {
        // Enable view binding, data binding and build config
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

// Define the dependencies for the project
dependencies {
    // Implementation dependencies
    implementation("androidx.databinding:databinding-runtime:8.1.0")
    implementation("com.intuit.ssp:ssp-android:1.0.6")
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("io.github.g00fy2.quickie:quickie-bundled:1.7.0")
    implementation("com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.0")
    implementation("com.karumi:dexter:6.0.0")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    implementation("com.google.code.gson:gson:2.8.8")
    //implementation("com.github.thesurix:gesture-recycler:1.17.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Coroutines dependencies
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Coroutine Lifecycle Scopes dependencies
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    implementation ("androidx.activity:activity-ktx:1.8.0")

    // Firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:32.4.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    // Implementation of various libraries
    implementation ("com.github.AbedElazizShe:LightCompressor:1.3.2")
    implementation("com.infideap.drawerbehavior:drawer-behavior:1.0.4")
    implementation ("com.github.vimalcvs:Day-Night-Switch:1
