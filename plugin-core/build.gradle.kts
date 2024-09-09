plugins {
    id("java")
}

group = providers.gradleProperty("pluginGroup").get()
version = "$rootProject.ext.plugin.version"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}