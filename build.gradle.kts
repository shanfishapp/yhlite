// 项目根目录的 build.gradle.kts
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}