plugins {
    java
    id("io.papermc.paperweight.userdev") version "1.7.4"
}
group = "com.dpshop"
version = "1.0.0"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    paperweight.paperDevBundle("1.21.6-R0.1-SNAPSHOT")
    compileOnly("net.milkbowl.vault:VaultAPI:1.7")
}

tasks {
    processResources {
        filesMatching("plugin.yml") { expand(project.properties) }
    }
}