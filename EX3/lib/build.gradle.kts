plugins {
    `java-library`
    kotlin("jvm")
}

version = "1.0.0"
group = "com.shehatamilo"

repositories {
    mavenCentral()
}

dependencies {
    api(kotlin("stdlib"))

    // https://mvnrepository.com/artifact/com.sun.xml.bind/jaxb-core
    api("com.sun.xml.bind:jaxb-core:3.0.2")
    // https://mvnrepository.com/artifact/com.sun.xml.bind/jaxb-impl
    api("com.sun.xml.bind:jaxb-impl:3.0.2")
    // https://mvnrepository.com/artifact/com.sun.xml.ws/jaxws-rt
    api("com.sun.xml.ws:jaxws-rt:3.0.2")

    // https://mvnrepository.com/artifact/org.eclipse.persistence/org.eclipse.persistence.moxy
    api("org.eclipse.persistence:org.eclipse.persistence.moxy:3.0.2")
    // https://mvnrepository.com/artifact/org.glassfish/jakarta.json
    api("org.glassfish:jakarta.json:2.0.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}