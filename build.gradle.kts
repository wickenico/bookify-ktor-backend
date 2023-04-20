@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktorPlugin)
    alias(libs.plugins.versionUpdate)
    alias(libs.plugins.catalogUpdate)
}

group = "com.nw"
version = "0.2.0"
application {
    mainClass.set("io.ktor.server.cio.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation(libs.bundles.ktorServer)
    implementation(libs.bundles.ktorClient)
    implementation(libs.bundles.ktorSerialization)
    implementation(libs.bundles.exposed)
    implementation(libs.bundles.dbConnector)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.swaggerAndOpenAPI)
    implementation(libs.bundles.security)
    implementation(libs.bundles.cookies)
    implementation(libs.bundles.testing)
}

ktor {
    docker {
        jreVersion.set(io.ktor.plugin.features.JreVersion.JRE_17)
        portMappings.set(
            listOf(
                io.ktor.plugin.features.DockerPortMapping(
                    8090,
                    8090,
                    io.ktor.plugin.features.DockerPortMappingProtocol.TCP
                )
            )
        )
    }
}
