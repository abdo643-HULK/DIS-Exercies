plugins {
    kotlin("jvm")
    java
}

group = "at.fh-ooe.shehata-milo"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    compileOnly("org.apache.tomcat:tomcat-servlet-api:10.0.14")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}