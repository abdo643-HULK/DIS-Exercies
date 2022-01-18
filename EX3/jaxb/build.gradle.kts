plugins {
    java
    application
    kotlin("jvm")
}

version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":lib"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}