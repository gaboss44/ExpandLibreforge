plugins {
    id("io.papermc.paperweight.userdev")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(project(":core:core-nms:common"))
    paperweight.paperDevBundle("1.21.6-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.21.6-R0.1-SNAPSHOT")
}

tasks {
    build {
        dependsOn(reobfJar)
    }

    reobfJar {
        mustRunAfter(shadowJar)
    }

    shadowJar {
        relocate(
            "com.github.gaboss44.expandlibreforge.proxy.common",
            "com.github.gaboss44.expandlibreforge.proxy.v1_21_6.common"
        )
    }
}