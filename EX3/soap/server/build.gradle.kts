plugins {
    java
    idea
    war
    checkstyle
    application
    kotlin("jvm")
}

version = "1.0.0"
group = "com.shehatamilo"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":lib"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}