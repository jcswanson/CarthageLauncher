// This is the top-level build file for a multi-module project in Gradle,
// where you can add configuration options common to all sub-projects/modules.

// Plugins are configured in the `plugins` block. Here, we configure several Android-related plugins,
// including the Android application plugin, Kotlin Android plugin, Google Services plugin,
// Crashlytics plugin, and Hilt plugin. The `apply false` statement means that these plugins
// are not immediately applied to this build script, but can be applied to individual sub-projects.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id ("com.google.firebase.crashlytics") version "2.9.9" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}

// The `buildscript` block is used to configure the build script itself.
// Here, we configure the repositories where dependencies can be downloaded from.
buildscript {
    repositories {
        // `google()` is a shorthand for the Google Maven repository, which contains
        // Android-related dependencies.
        google()
        // `mavenCentral()` is a shorthand for the Maven Central repository, which contains
        // a wide variety of Java dependencies.
        mavenCentral()
    }

    // The `dependencies` block is used to declare dependencies for the build script.
    // Here, we declare a dependency on the `android-maven-gradle-plugin`, which provides
    // additional functionality for working with Android libraries published to Maven.
    dependencies {
        classpath ("com.github.dcendents:android-maven-gradle-plugin:2.1")
    }
}
