// 根项目 build.gradle.kts
buildscript {
    repositories {
        google()  // 添加 Google Maven 仓库
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.2")  // 添加 Android Gradle 插件
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
    }
}

plugins {
    id("com.android.application") version "8.1.2" apply false
    id("com.android.library") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}

allprojects {
    repositories {
        google()  // 确保所有项目都能访问 Google Maven
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}