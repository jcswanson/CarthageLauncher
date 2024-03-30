plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id ("com.google.firebase.crashlytics") version "2.9.9" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath ("com.github.dcendents:android-maven-gradle-plugin:2.1")
        // Add the Kotlin Gradle plugin dependency
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
    }
}

// Apply the android-maven-gradle-plugin to the buildscript classpath
apply plugin: 'com.github.dcendents.android-maven'

// Configure the android-maven-gradle-plugin
androidMaven {
    // Set the repository URL
    repositoryUrl = 'https://maven.package.com/repository/android/'
    // Set the username and password for the repository (if required)
    // authentication {
    //     userName = 'username'
    //     password = 'password'
    // }
}
