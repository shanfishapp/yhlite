pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_LOAD)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ChatApp"
include(":app")