// Define the plugin management repositories
pluginManagement {
    repositories {
        gradlePluginPortal() // Include Gradle Plugin Portal repository
        google()            // Include Google's Maven repository
        mavenCentral()      // Include Maven Central repository
    }
}

// Configure dependency resolution management
repositories {
    mavenCentral()        // Include Maven Central repository (added again for dependencies)
    google()              // Include Google's Maven repository (added again for dependencies)
    maven { url 'https://jitpack.io' }  // Include Jitpack Maven repository
    //jcenter()             // Commented out: jcenter is going to be deprecated
}

// Set the root project name to "Beta Backers"
rootProject.name = 'Beta Backers'

// Include the 'app' module
include ':app'

// Commented out: include the 'library' module
//include ':library'

// Fail on project repositories when repositories mode is set to 'FAIL_ON_PROJECT_REPOS'
allprojects {
    repositories {
        if (repositoriesMode.equals('FAIL_ON_PROJECT_REPOS')) {
            fail("Project repository configuration is not allowed.")
        }
    }
}
