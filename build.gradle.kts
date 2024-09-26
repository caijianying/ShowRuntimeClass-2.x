import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    java
    id("org.jetbrains.intellij.platform") version "2.1.0"
    distribution
}

apply(from = "config.gradle.kts")
// 获取 dependencies map
val deps = rootProject.extra["dependencies"] as? Map<String, String> ?: error("Dependencies map not found in root project")
val plugin = rootProject.extra["plugin"] as? Map<String, String> ?: error("plugin map not found in root project")

group = "com.xiaobaicai.plugin"
version = "$rootProject.ext.plugin.version"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()

    // IntelliJ Platform Gradle Plugin Repositories Extension
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    implementation(project(":plugin-core"))
    implementation(project(mapOf("path" to ":plugin-agent", "configuration" to "shadow")))

    implementation(deps.getValue("hutool-all"))
    implementation(deps.getValue("slf4j"))
    annotationProcessor(deps.getValue("lombok"))
    compileOnly(deps.getValue("lombok"))
    testAnnotationProcessor(deps.getValue("lombok"))
    testCompileOnly(deps.getValue("lombok"))

    // 单测
    testCompileOnly(deps.getValue("junit"))
    testImplementation(deps.getValue("junit.jupiter.api"))
    testImplementation(deps.getValue("junit.jupiter.engine"))

    // IntelliJ Platform Gradle Plugin Dependencies Extension
    intellijPlatform {
        bundledPlugin("com.intellij.java")

        val type = providers.gradleProperty("platformType")
        val version = providers.gradleProperty("platformVersion")
        create(type.get(), version.get())

        pluginVerifier()
        zipSigner()
        instrumentationTools()

        testFramework(TestFrameworkType.Platform)
    }
}

// Configure IntelliJ Platform Gradle Plugin
intellijPlatform {
    buildSearchableOptions.set(false)
    instrumentCode.set(true)
    projectName.set(project.name)
    pluginConfiguration {
        name.set(providers.gradleProperty("pluginName"))
        version.set(providers.gradleProperty("pluginVersion"))

        ideaVersion {
            sinceBuild.set(providers.gradleProperty("pluginSinceBuild"))
            untilBuild.set(providers.gradleProperty("pluginUntilBuild"))
        }
    }
    publishing {
        token.set(providers.environmentVariable("pluginToken"))
    }

    pluginVerification {
        ides {
            local("/Applications/IntelliJ IDEA Ultimate.app")
            recommended()
        }
    }
}

tasks.named<JavaExec>("runIde") {
    jvmArgs("--add-exports", "java.base/jdk.internal.vm=ALL-UNNAMED")
}

tasks.test {
    useJUnitPlatform()
}

//所有项目配置
allprojects {
    group = "com.xiaobaicai.plugin"
    version = plugin.getValue("version")

    apply(plugin = "idea")
    apply(plugin = "java")

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

//所有子项目的通用配置
subprojects {
    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(deps.getValue("hutool-all"))
        annotationProcessor(deps.getValue("lombok"))
        compileOnly(deps.getValue("lombok"))
        testAnnotationProcessor(deps.getValue("lombok"))
        testCompileOnly(deps.getValue("lombok"))

        // 单测
        testCompileOnly(deps.getValue("junit"))
        testImplementation(deps.getValue("junit.jupiter.api"))
        testImplementation(deps.getValue("junit.jupiter.engine"))
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.register("packAllTwice") {
    dependsOn(":plugin-agent:packAgent")
    dependsOn(":assemble")
}