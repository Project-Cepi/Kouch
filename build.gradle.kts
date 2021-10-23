import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.21"
    application
}

group = "world.cepi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor", "ktor-client-core", "1.6.2")
    implementation("io.ktor", "ktor-client-java","1.6.2")

    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.5.2")

    implementation(platform("org.http4k:http4k-bom:4.14.0.0"))
    implementation("org.http4k", "http4k-core")
    implementation("org.http4k", "http4k-client-okhttp")

    implementation("org.http4k", "http4k-format-kotlinx-serialization")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}