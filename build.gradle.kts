plugins {
    id("application")
    id("com.gradleup.shadow") version "9.0.0-beta4"
}

group = "io.github.pixeldev"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass = "io.github.pixeldev.integral.Bootstrap"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.yannrichet:JMathPlot:1.0.1")
    implementation("com.itextpdf:itext7-core:7.2.6")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        manifest {
            attributes["Main-Class"] = "io.github.pixeldev.integral.Bootstrap"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
