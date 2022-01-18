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
    implementation(project(":lib"))

    // https://mvnrepository.com/artifact/com.sun.xml.ws/jaxws-rt
    implementation("com.sun.xml.ws:jaxws-rt:3.0.2")
    implementation("com.sun.xml.ws:jaxws-tools:3.0.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.register("wsimport") {
//    group = BasePlugin.BUILD_GROUP
    val srcDestDir = file("$projectDir/src/main/java")
    srcDestDir.mkdirs()

    doLast {
        ant.withGroovyBuilder {
            "taskdef"(
                "name" to "wsimport",
                "classname" to "com.sun.tools.ws.ant.WsImport",
                "classpath" to sourceSets.getAt("main").runtimeClasspath.asPath
            )

            "wsimport"(
                "sourcedestdir" to srcDestDir,
                "keep" to true,
                "package" to "com.shehatamilo",
                "wsdl" to "${projectDir}/src/main/resources/HelloWorld.wsdl",
            )
        }
    }
}