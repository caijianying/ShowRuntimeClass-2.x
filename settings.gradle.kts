plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "ShowRuntimeClass-2.x"
include("plugin-core")
include("plugin-agent")
