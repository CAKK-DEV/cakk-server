plugins {
	id("java")
	id("org.springframework.boot") apply false
	id("io.spring.dependency-management")

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
	apply(plugin = "checkstyle")
	apply(plugin = "jacoco")

	configurations {
		compileOnly {
			extendsFrom(configurations.annotationProcessor.get())
		}
	}

	dependencies {
		// lombok
		compileOnly("org.projectlombok:lombok")
		annotationProcessor("org.projectlombok:lombok")

		testImplementation("junit:junit:4.13.1")
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

	tasks.withType<JacocoReport>  {
		reports {
			html.required.set(true)
			csv.required.set(false)
			xml.required.set(true)
		}

		finalizedBy("jacocoTestCoverageVerification")
	}

	tasks.withType<JacocoCoverageVerification>  {
		val excludeList = mutableListOf(
			"com.cakk.api.Application",
			"com.cakk.api.service.slack.SlackService",
			"com.cakk.api.provider.oauth.PublicKeyProvider",
			"com.cakk.api.dto.**",
			"com.cakk.api.mapper.**",
			"com.cakk.api.vo.**",
			"com.cakk.domain.**"
		)

		for (qPattern in 'A' .. 'Z') {
			excludeList.add("*.Q${qPattern}*")
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

