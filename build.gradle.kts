// This is the top-level build file for a multi-module project. It contains common configurations for all sub-projects.

// Plugins are configured in this block. They provide additional functionality for the build script.
plugins {
    // The 'com.android.application' plugin is used to build Android applications.
    id("com.android.application") version "8.1.1" apply false

    // The 'org.jetbrains.kotlin.android' plugin is used to add Kotlin support for Android development.
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false

    // The 'com.google.gms.google-services' plugin is used to add Google Services to the project.
    id("com.google.gms.google-services") version "4.4.0" apply false

    // The 'com.google.firebase.crashlytics' plugin is used to add Firebase Crashlytics to the project.
    id ("com.google.firebase.crashlytics") version "2.9.9" apply false

    // The 'com.google.dagger.hilt.android' plugin is used to add Hilt, a dependency injection library for Android, to the project.
    id("com.google.dagger.hilt.android") version "2.48" apply false
}

// The buildscript block is used to configure the build script itself.
buildscript {
    // Repositories are where dependencies are downloaded from.
    repositories {
        // The 'google()' repository is a shortcut for the Google Maven repository.
        google()

        // The 'mavenCentral()' repository is a shortcut for the Maven Central repository.
        mavenCentral()
    }

    // Dependencies are required libraries for the build script.
    dependencies {
        // The 'com.github.dcendents:android-maven-gradle-plugin' library is used to publish Android libraries to Maven.
        classpath ("com.github.dcendents:android-maven-gradle-plugin:2.1
