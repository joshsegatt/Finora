pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Finora"

include(":app")
include(":core")
include(":domain")
include(":data")
include(":ui-theme")
include(":features:expenses")
include(":features:reports")
