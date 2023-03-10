pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            library("lombok", "org.projectlombok:lombok:1.18.26")
        }
    }
}

rootProject.name = "buildscale-plugin"
include("plugin")
