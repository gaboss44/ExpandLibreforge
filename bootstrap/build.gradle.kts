plugins {
    `java-library`
}

group = rootProject.group
version = rootProject.version

java { toolchain { languageVersion.set(JavaLanguageVersion.of(21)) } }

tasks.jar {
    archiveBaseName.set("expandlibreforge-bootstrap")
    archiveClassifier.set("")
    archiveVersion.set("")
}
