plugins {
    java
    kotlin("jvm")
}

version = "1.0.0"
group = "com.shehatamilo"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":lib"))

    // https://mvnrepository.com/artifact/jakarta.xml.bind/jakarta.xml.bind-api
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")

    // https://mvnrepository.com/artifact/org.glassfish.jersey.media/jersey-media-jaxb
    implementation("org.glassfish.jersey.media:jersey-media-jaxb:3.0.3")
    // https://mvnrepository.com/artifact/org.glassfish.jersey.containers/jersey-container-servlet
    implementation("org.glassfish.jersey.containers:jersey-container-servlet:3.0.3")

    // https://mvnrepository.com/artifact/jakarta.servlet/jakarta.servlet-api
    compileOnly("jakarta.servlet:jakarta.servlet-api:5.0.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}