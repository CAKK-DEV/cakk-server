import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.spring) apply false
	alias(libs.plugins.kotlin.jpa) apply false
	alias(libs.plugins.spring.boot) apply false
	alias(libs.plugins.spring.dependency.management)

	checkstyle
	jacoco
}

java.sourceCompatibility = JavaVersion.valueOf("VERSION_${property("javaVersion")}")

allprojects {
	group = "${property("projectGroup")}"
	version = "${property("applicationVersion")}"

	repositories {
		mavenCentral()
	}
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")
	apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
	apply(plugin = "checkstyle")
	apply(plugin = "jacoco")

	configurations {
		compileOnly {
			extendsFrom(configurations.annotationProcessor.get())
		}
	}

	if (project.name == "cakk-admin" ||
		project.name == "cakk-external" ||
		project.name == "cakk-core" ||
		project.name == "cakk-domain" ||
		project.name == "cakk-common" ||
		project.name == "cakk-api"
	) {
		apply(plugin = "org.jetbrains.kotlin.jvm")
		apply(plugin = "org.jetbrains.kotlin.plugin.spring")
		apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

		dependencies {
			implementation(libs.kotlin.stdlib.jdk8)
			implementation(libs.kotlin.reflect)
		}

		tasks.withType<KotlinCompile> {
			compilerOptions {
				freeCompilerArgs.add("-Xjsr305=strict")
				jvmTarget.set(JvmTarget.JVM_21)
			}
		}
	} else {
		dependencies {
			compileOnly("org.projectlombok:lombok")
			annotationProcessor("org.projectlombok:lombok")
		}
	}

	checkstyle {
		toolVersion = "10.3.3"
	}

	tasks.withType<Checkstyle> {
		maxWarnings = 0
		configFile = file("$rootDir/config/checkstyle/checkstyle-rules.xml")
		configProperties = mapOf("suppressionFile" to "$rootDir/config/checkstyle/checkstyle-suppressions.xml")

		tasks.checkstyleMain {
			source = fileTree("src/main/java")
		}

		tasks.checkstyleTest {
			source = fileTree("src/test/java")
		}
	}

	tasks.test {
		useJUnitPlatform()
		finalizedBy(tasks.jacocoTestReport)
	}

	jacoco {
		toolVersion = "0.8.12"
	}

	tasks.test {
		useJUnitPlatform()
		finalizedBy(tasks.jacocoTestReport)
	}

	tasks.withType<JacocoReport> {
		reports {
			html.required.set(true)
			csv.required.set(false)
			xml.required.set(true)
		}

		finalizedBy("jacocoTestCoverageVerification")
	}

	tasks.withType<JacocoCoverageVerification> {
		val excludeList = mutableListOf(
			"com.cakk.api.ApplicationKt",
			"com.cakk.api.service.slack.SlackService",
			"com.cakk.api.provider.oauth.PublicKeyProvider",
			"com.cakk.api.dto.**",
			"com.cakk.api.mapper.**",
			"com.cakk.api.listener.**",
			"com.cakk.api.vo.**",
			"com.cakk.core.**",
			"com.cakk.infrastructure.**"
		)

		for (qPattern in 'A'..'Z') {
			excludeList.add("*.Q$qPattern*")
		}

		violationRules {
			rule {
				enabled = true
				element = "CLASS"

				limit {
					counter = "LINE"
					value = "COVEREDRATIO"
					minimum = "0.70".toBigDecimal()
				}

				limit {
					counter = "BRANCH"
					value = "COVEREDRATIO"
					minimum = "0.70".toBigDecimal()
				}

				limit {
					counter = "LINE"
					value = "TOTALCOUNT"
					maximum = "200".toBigDecimal()
				}

				excludes = excludeList
			}
		}
	}
}
