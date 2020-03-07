import com.jfrog.bintray.gradle.BintrayExtension

plugins {
    `maven-publish`
    kotlin("jvm") version "1.3.70"
    id("net.nemerosa.versioning") version "2.8.2"
    id("com.jfrog.bintray") version "1.8.4"
}

// project name must be set in settings.gradle.kts
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

// ------------------------------------ Deployment Configuration  ------------------------------------
val githubRepository = "LukasForst/exposed-upsert"
val descriptionForPackage = "Simple upsert implementation for Exposed and PostgreSQL"
val tags = arrayOf("kotlin", "PostgreSQL", "Exposed", "Exposed-Extensions")
// everything bellow is set automatically

// deployment configuration - deploy with sources and documentation
val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
}

// name the publication as it is referenced
val publication = "default-gradle-publication"
publishing {
    // create jar with sources and with javadoc
    publications {
        register(publication, MavenPublication::class) {
            from(components["java"])
            artifact(sourcesJar)
            artifact(javadocJar)
        }
    }

    // publish package to the github packages
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/$githubRepository")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

// upload to bintray
bintray {
    // env variables loaded from pipeline for publish
    user = project.findProperty("bintray.user") as String? ?: System.getenv("BINTRAY_USER")
    key = project.findProperty("bintray.key") as String? ?: System.getenv("BINTRAY_TOKEN")
    publish = true
    setPublications(publication)
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        // my repository for maven packages
        repo = "jvm-packages"
        name = project.name
        // my user account at bintray
        userOrg = "lukas-forst"
        websiteUrl = "https://forst.pw"
        githubRepo = githubRepository
        vcsUrl = "https://github.com/$githubRepository"
        description = descriptionForPackage
        setLabels(*tags)
        setLicenses("MIT")
        desc = description
    })
}
