// 根项目的 build.gradle.kts
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.1") // 使用最新稳定版
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20") // 如果用Kotlin
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}