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

    implementation("org.jetbrains.kotlinx", "kotlinx.coroutines", "1.5.1")

    implementation("com.squareup.moshi", "moshi", "1.12.0")
    implementation("com.squareup.moshi", "moshi-kotlin", "1.12.0")
    implementation("com.squareup.moshi", "moshi-kotlin-codegen", "1.12.0")
    implementation("org.json", "json", "20210307")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}