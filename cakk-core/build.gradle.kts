description = "core module"

dependencies {
	implementation(projects.common)
	implementation(projects.persistence)
	implementation(projects.cache)
	implementation(projects.external)

	// basic
	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.spring.boot.starter.aop)
	annotationProcessor(libs.spring.boot.configuration.processor)

	// test
	testImplementation(libs.archunit)
	testImplementation(libs.spring.boot.starter.test)
	testImplementation(libs.mockito.kotlin)
	testImplementation(libs.fixture.monkey.starter)
	testImplementation(libs.kotest.junit)
	testImplementation(libs.mockk)

	// Jwt
	implementation(libs.jwt.api)
	implementation(libs.jwt.impl)
	implementation(libs.jwt.jackson)

	// Point
	implementation(libs.jts.core)

	// Mail
	implementation(libs.spring.boot.starter.mail)

	// Slack
	implementation(libs.slack.webhook)
}

tasks.bootJar {
	enabled = false
}

tasks.jar {
	enabled = true
}
