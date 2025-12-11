plugins {
    id("io.papermc.paperweight.userdev")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(project(":core:core-nms:common"))
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

tasks {
    build {
        dependsOn(shadowJar)
        dependsOn(reobfJar)
    }

    reobfJar {
        mustRunAfter(shadowJar)
    }

    shadowJar {
        relocate(
            "com.github.gaboss44.expandlibreforge.proxy.common",
            "com.github.gaboss44.expandlibreforge.proxy.v1_21_4.common"
        )
    }
}
