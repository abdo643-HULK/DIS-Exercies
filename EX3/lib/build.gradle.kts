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

    api("com.sun.xml.ws:jaxws-rt:2.3.0.2")
    api("org.eclipse.persistence:org.eclipse.persistence.moxy:2.7.3")
// https://mvnrepository.com/artifact/org.glassfish/javax.json
    api("org.glassfish:javax.json:1.1.4")


// ------- new libs --------

//    // https://mvnrepository.com/artifact/com.sun.xml.bind/jaxb-core
//    api("com.sun.xml.bind:jaxb-core:3.0.2")
//    // https://mvnrepository.com/artifact/com.sun.xml.bind/jaxb-impl
//    api("com.sun.xml.bind:jaxb-impl:3.0.2")
//    // https://mvnrepository.com/artifact/com.sun.xml.ws/jaxws-rt
//    api("com.sun.xml.ws:jaxws-rt:3.0.2")
//    // https://mvnrepository.com/artifact/org.eclipse.persistence/org.eclipse.persistence.moxy
//    api("org.eclipse.persistence:org.eclipse.persistence.moxy:3.0.2")
    // https://mvnrepository.com/artifact/org.glassfish/jakarta.json
//    api("org.glassfish:jakarta.json:2.0.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<Jar> { duplicatesStrategy = DuplicatesStrategy.EXCLUDE }

tasks.withType<Tar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Zip>{
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}