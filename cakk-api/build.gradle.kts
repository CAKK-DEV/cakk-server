description = "api module"

dependencies {
	implementation(projects.common)
	implementation(projects.persistence)
	implementation(projects.cache)
	implementation(projects.external)
	implementation(projects.application)

	// basic
	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.spring.boot.starter.aop)
	annotationProcessor(libs.spring.boot.configuration.processor)
	implementation(libs.kotlin.jackson)

	// Security & OAuth
	implementation(libs.spring.boot.starter.security)
	implementation(libs.spring.boot.starter.oauth)
	implementation(libs.google.api.client)
	implementation(libs.google.api.client.jackson2)

	// Jwt
	implementation(libs.jwt.api)
	implementation(libs.jwt.impl)
	implementation(libs.jwt.jackson)

	// test
	testImplementation(libs.archunit)
	testImplementation(libs.spring.boot.starter.test)
	testImplementation(libs.spring.security.test)
	testImplementation(libs.mockito.kotlin)
	testImplementation(libs.fixture.monkey.starter)
	testImplementation(libs.kotest.junit)
	testImplementation(libs.mockk)

	// test container
	testImplementation(libs.testcontainers.junit)
	testImplementation(libs.testcontainers.mysql)

	// Point
	implementation(libs.jts.core)
}

tasks.bootJar {
	enabled = true
}

tasks.jar {
	enabled = false
}
