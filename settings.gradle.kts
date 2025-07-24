pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // 使用设置中的仓库
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MyMD3App"
include(":app")