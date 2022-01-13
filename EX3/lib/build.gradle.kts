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
    implementation(kotlin("stdlib"))
    // https://mvnrepository.com/artifact/com.sun.xml.ws/jaxws-rt
    implementation("com.sun.xml.ws:jaxws-rt:3.0.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}