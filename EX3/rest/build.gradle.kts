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
    // https://mvnrepository.com/artifact/javax.ws.rs/javax.ws.rs-api
    implementation("javax.ws.rs:javax.ws.rs-api:2.1.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}