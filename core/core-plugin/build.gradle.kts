group = rootProject.group
version = rootProject.version

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = "expandlibreforge"

            pom {
                name.set("ExpandLibreforge")
                description.set("API for ExpandLibreforge plugin")
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/gaboss44/ExpandLibreforge")
            credentials(PasswordCredentials::class) {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}
