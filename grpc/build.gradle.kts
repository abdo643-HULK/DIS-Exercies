import com.google.protobuf.gradle.*
import org.gradle.kotlin.dsl.provider.gradleKotlinDslOf

plugins {
    idea
    id("com.google.protobuf") version "0.8.18"
    kotlin("jvm") version Constants.kotlinVersion
}

group = "org.shehata"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Constants.coroutinesVersion}")

    implementation("io.grpc:grpc-stub:${Constants.grpcVersion}")
    implementation("io.grpc:grpc-netty:${Constants.grpcVersion}")
    implementation("io.grpc:grpc-protobuf:${Constants.grpcVersion}")
    implementation("com.google.protobuf:protobuf-kotlin:${Constants.protobufVersion}")

    implementation("javax.annotation:javax.annotation-api:1.3.2")
}
