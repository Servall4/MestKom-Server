val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val postgres_version : String by project
val h2_version : String by project
val exposed_version: String by project
val commons_codec_version: String by project
val java_time: String by project

plugins {
    application
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.5"
    //id("plugin.serialization") version("1.8.0")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
}

group = "com.mestKom"
version = "0.0.1"

application {
    mainClass.set("com.mestKom.ApplicationKt")

//    val isDevelopment: Boolean = project.ext.has("development")
//    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("org.postgresql:postgresql:$postgres_version")
    implementation("com.h2database:h2:$h2_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("commons-codec:commons-codec:$commons_codec_version")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.exposed:exposed-java-time:$java_time")
}

java {
  targetCompatibility = JavaVersion.VERSION_11
  sourceCompatibility = JavaVersion.VERSION_11
}

kotlin {
  compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
}

ktor {

  fatJar {
    archiveFileName.set("fat.jar")
  }

  docker {
    jreVersion.set(JavaVersion.VERSION_11)
    localImageName.set("mestkom-image")
    imageTag.set("0.0.1-preview")
    portMappings.set(listOf(
      io.ktor.plugin.features.DockerPortMapping(
        80,
        8080,
        io.ktor.plugin.features.DockerPortMappingProtocol.TCP
      )
    ))

    externalRegistry.set(
      io.ktor.plugin.features.DockerImageRegistry.dockerHub(
        appName = provider { "MestKom" },
        username = providers.environmentVariable("DOCKER_HUB_USERNAME"),
        password = providers.environmentVariable("DOCKER_HUB_PASSWORD")
      )
    )

    environmentVariable("JWT_SECRET", "i-sacrifice")
    environmentVariable("VIDEOS_DIR", "./home/videos")
  }
}
