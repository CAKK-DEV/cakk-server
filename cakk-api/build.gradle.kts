description = "api module"

dependencies {
	implementation(project(":cakk-common"))
	implementation(project(":cakk-domain:mysql"))
	implementation(project(":cakk-domain:redis"))
	implementation(project(":cakk-external"))
	implementation(project(":cakk-core"))

	// basic
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-aop:3.3.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	// Security & OAuth
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("com.google.api-client:google-api-client-jackson2:2.2.0")
	implementation("com.google.api-client:google-api-client:2.2.0")

	// Jwt
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// test
	testImplementation("com.tngtech.archunit:archunit-junit5:1.1.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
	testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter:1.0.23")
	testImplementation("io.kotest:kotest-runner-junit5:${property("kotestVersion")}")
	testImplementation("io.mockk:mockk:${property("mockKVersion")}")

	// test container
	testImplementation("org.testcontainers:junit-jupiter:1.19.7")
	testImplementation("org.testcontainers:mysql:1.19.7")

	// Point
	implementation("org.locationtech.jts:jts-core:1.18.2")
}

tasks.bootJar {
	enabled = true
}

tasks.jar {
	enabled = false
}
