// settings.gradle.kts
pluginManagement {
    repositories {
        google()  // 添加 Google Maven 仓库
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()  // 添加 Google Maven 仓库
        mavenCentral()
    }
}

rootProject.name = "MD3"
include(":app")