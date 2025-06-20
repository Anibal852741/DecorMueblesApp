pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io") // Necesario para MPAndroidChart
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // Prioridad aquí
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io") // Necesario para MPAndroidChart
    }
}
rootProject.name = "Stockifi2"
include(":app")
