// Define the plugin management repositories
pluginManagement {
    repositories {
        // Include Google's Maven repository
        google()
        // Include Maven Central repository
        mavenCentral()
        // Include Gradle Plugin Portal repository
        gradlePluginPortal()
    }
}

// Configure dependency resolution management
dependencyResolutionManagement {
    // Fail on project repositories when repositories mode is set to 'FAIL_ON_PROJECT_REPOS'
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // Include Google's Maven repository
        google()
        // Include Maven Central repository
        mavenCentral()
        // Include Jitpack Maven repository
        maven { setUrl("https://jitpack.io") }
        // Include JCenter repository
        jcenter()
    }
}

// Set the root project name to "Beta Backers"
rootProject.name = "Beta Backers"

// Include the 'app' module
include(":app")

// Commented out: include the 'library' module
//include(":library")
