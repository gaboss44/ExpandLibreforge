plugins {
    id("io.papermc.paperweight.userdev")
}

group = rootProject.group
version = rootProject.version

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    pluginRemapper("net.fabricmc:tiny-remapper:0.10.3:fat")
}
