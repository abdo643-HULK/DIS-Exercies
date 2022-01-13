plugins {
    kotlin("jvm")
    java
}

version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":lib"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}