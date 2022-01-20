import org.jetbrains.gradle.ext.ProjectSettings
import org.jetbrains.gradle.ext.BuildArtifact

plugins {
    java
    checkstyle
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.1"
}

version = "1.0.0"
group = "com.shehatamilo"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    // https://mvnrepository.com/artifact/com.sun.xml.bind/jaxb-core
    implementation("com.sun.xml.bind:jaxb-core:3.0.2")
    // https://mvnrepository.com/artifact/com.sun.xml.bind/jaxb-impl
    implementation("com.sun.xml.bind:jaxb-impl:3.0.2")

    // https://mvnrepository.com/artifact/org.eclipse.persistence/org.eclipse.persistence.moxy
    implementation("org.eclipse.persistence:org.eclipse.persistence.moxy:3.0.2")
    // https://mvnrepository.com/artifact/org.glassfish/jakarta.json
    implementation("org.glassfish:jakarta.json:2.0.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}