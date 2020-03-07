plugins {
    `maven-publish`
    kotlin("jvm") version "1.3.70"
    id("net.nemerosa.versioning") version "2.8.2"
    id("com.jfrog.bintray") version "1.8.4"
}

group = "pw.forst"
version = versioning.info.lastTag

repositories {
    jcenter()
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly("org.jetbrains.exposed", "exposed-core", "0.21.1")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

// deployment configuration - deploy with sources and documentation
val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
}

val publication = "exposed-upsert"
publishing {
    publications {
        register("exposed-upsert", MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar)
            artifact(javadocJar)
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/LukasForst/exposed-upsert")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_TOKEN")
    publish = true
    setPublications(publication)
    pkg(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig> {
        repo = "jvm-packages"
        name = "exposed-upsert"
        websiteUrl = "https://forst.pw"
        githubRepo = "LukasForst/exposed-upsert"
        vcsUrl = "https://github.com/LukasForst/exposed-upsert"
        description = "Simple upsert implementation for Exposed and PostgreSQL"
        setLabels("kotlin", "PostgreSQL", "Exposed", "Exposed-Extensions")
        setLicenses("MIT")
        desc = description
    })
}
