import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

allprojects {
    group = "com.llamalad7.consteval"
    version = "0.1.0-SNAPSHOT"
}

val kotlinVersion: String by project.properties

plugins {
    java
    kotlin("jvm") version "2.0.20"
}

repositories {
    mavenCentral()
}

sourceSets {
    val testGenerator by creating {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }

    test {
        java.setSrcDirs(listOf("src/tests-gen"))
        compileClasspath += testGenerator.output
        runtimeClasspath += testGenerator.output
    }
}

dependencies {
    implementation(kotlin("reflect"))
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable")

    val sharedTestLibs = listOf(
        "org.jetbrains.kotlin:kotlin-compiler-internal-test-framework",
        "org.jetbrains.kotlin:kotlin-compiler",
        platform("org.junit:junit-bom:5.11.0"),
        "org.junit.jupiter:junit-jupiter",
    )

    sharedTestLibs.forEach {
        testImplementation(it)
        "testGeneratorImplementation"(it)
    }

    testImplementation(kotlin("test-junit"))
    testRuntimeOnly("org.jetbrains.kotlin:kotlin-annotations-jvm:$kotlinVersion")
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
        optIn.add("org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI")
    }
}

val generateTests by tasks.creating(JavaExec::class) {
    inputs.dir("src/testData")
    outputs.dir("src/tests-gen")
    classpath = sourceSets["testGenerator"].runtimeClasspath
    mainClass.set("com.llamalad7.consteval.test.GenerateTestsKt")
}

tasks.compileTestJava {
    dependsOn(generateTests)
}

tasks.test {
    inputs.dir("src/testData")
    useJUnitPlatform()
    doFirst {
        setLibraryProperty("org.jetbrains.kotlin.test.kotlin-stdlib", "kotlin-stdlib")
        setLibraryProperty("org.jetbrains.kotlin.test.kotlin-stdlib-jdk8", "kotlin-stdlib-jdk8")
        setLibraryProperty("org.jetbrains.kotlin.test.kotlin-reflect", "kotlin-reflect")
        setLibraryProperty("org.jetbrains.kotlin.test.kotlin-test", "kotlin-test")
        setLibraryProperty("org.jetbrains.kotlin.test.kotlin-script-runtime", "kotlin-script-runtime")
        setLibraryProperty("org.jetbrains.kotlin.test.kotlin-annotations-jvm", "kotlin-annotations-jvm")
    }
}

fun Test.setLibraryProperty(propName: String, jarName: String) {
    val path = project.configurations
        .testRuntimeClasspath.get()
        .files
        .find { """$jarName-\d.*jar""".toRegex().matches(it.name) }
        ?.absolutePath
        ?: return
    systemProperty(propName, path)
}