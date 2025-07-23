pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_RELOAD)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ChatApp"
include(":app")