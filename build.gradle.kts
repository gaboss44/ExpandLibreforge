import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    `java-library`
    kotlin("jvm") version "2.1.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.willfp.libreforge-gradle-plugin") version "1.0.3"
}

group = "com.github.gaboss44"
version = findProperty("version")!!

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        name = "spigotmc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.auxilor.io/repository/maven-public/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("com.willfp:eco:6.76.3")
    compileOnly("org.jetbrains:annotations:23.0.0")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:2.1.0")
    compileOnly("de.tr7zw:item-nbt-api:2.12.3")
}

apply(plugin = "java")
apply(plugin = "kotlin")
apply(plugin = "com.github.johnrengelman.shadow")

java {
    withSourcesJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

val libreforgeVersion = findProperty("libreforge-version")!!

tasks {

    shadowJar {
        relocate("com.willfp.libreforge.loader", "com.github.gaboss44.expandlibreforge.libreforge.loader")
    }

    compileKotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    compileJava {
        options.isDeprecation = true
        options.encoding = "UTF-8"

        dependsOn(clean)
    }

    processResources {
        filesMatching(listOf("**/plugin.yml", "**/eco.yml")) {
            expand(
                "version" to project.version,
                "libreforgeVersion" to libreforgeVersion,
                "pluginName" to rootProject.name
            )
        }
    }

    build {
        dependsOn(shadowJar)
    }
}
