plugins {
    `java-library`
    war
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":rmi:client"))
    implementation(project(":lib"))

    compileOnly("org.apache.tomcat:tomcat-servlet-api:8.5.73")

//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

//tasks.getByName<Test>("test") {
//    useJUnitPlatform()
//}