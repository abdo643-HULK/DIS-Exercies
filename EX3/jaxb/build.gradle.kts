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
    implementation(kotlin("stdlib"))

    implementation(project(":lib"))

    // https://mvnrepository.com/artifact/com.sun.xml.bind/jaxb-core
    implementation("com.sun.xml.bind:jaxb-core:3.0.2")
    // https://mvnrepository.com/artifact/com.sun.xml.bind/jaxb-impl
    implementation("com.sun.xml.bind:jaxb-impl:3.0.2")

//    // https://mvnrepository.com/artifact/jakarta.json/jakarta.json-api
//    implementation("jakarta.json:jakarta.json-api:2.0.1")
    // https://mvnrepository.com/artifact/org.eclipse.persistence/org.eclipse.persistence.moxy
    implementation("org.eclipse.persistence:org.eclipse.persistence.moxy:3.0.2")
    // https://mvnrepository.com/artifact/org.glassfish/jakarta.json
    implementation("org.glassfish:jakarta.json:2.0.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}