pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        maven("https://repo.auxilor.io/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "ExpandLibreforge"

include("core")
include("core:core-plugin")
include("core:core-nms")
include("core:core-nms:v1_21_4")
include("core:core-nms:common")