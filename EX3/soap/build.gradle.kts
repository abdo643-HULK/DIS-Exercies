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
    // https://mvnrepository.com/artifact/javax.xml.ws/jaxws-api
    implementation("javax.xml.ws:jaxws-api:2.3.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}