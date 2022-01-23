import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    application
    kotlin("jvm")
}

group = "org.shehata"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}



dependencies {
    implementation(project(":stub"))

    runtimeOnly("io.grpc:grpc-netty:${Constants.grpcVersion}")
}

tasks.named("startScripts") {
    enabled = false
}