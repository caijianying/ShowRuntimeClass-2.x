plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.xiaobaicai.plugin"
version "$rootProject.ext.plugin.version"

// 获取 dependencies map
val deps = rootProject.extra["dependencies"] as? Map<String, String> ?: error("Dependencies map not found in root project")

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":plugin-core"))
    implementation(deps.getValue("slf4j"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    manifest {
        attributes(
            mapOf(
                "Manifest-Version" to "1.0",
                "Premain-Class" to "com.xiaobaicai.plugin.agent.PluginAgent",
                "Agent-Class" to "com.xiaobaicai.plugin.agent.PluginAgent",
                "Can-Redefine-Classes" to true,
                "Can-Retransform-Classes" to true
            )
        )
    }
}

val packAgent by tasks.registering(Copy::class) {
    dependsOn("clean", "shadowJar")
    sourceSets["main"].resources.srcDirs.forEach { srcDir ->
        from("$buildDir/libs")
        into("$buildDir/../../../${rootProject.extra["plugin.agentTargetJarModule"]}/build/idea-sandbox/plugins/${rootProject.extra["plugin.agentTargetJarModule"]}/lib")
    }
}

tasks.named("build") {
    dependsOn(tasks.named("shadowJar"))
}