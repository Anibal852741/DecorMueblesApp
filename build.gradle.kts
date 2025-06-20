// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral() // Repositorio principal para bibliotecas estándar
        maven("https://jitpack.io") // Repositorio para MPAndroidChart y otras bibliotecas de GitHub
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
