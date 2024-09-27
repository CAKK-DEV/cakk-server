description = "core module"

dependencies {
	implementation(project(":cakk-common"))
	implementation(project(":cakk-domain:mysql"))
	implementation(project(":cakk-domain:redis"))
	implementation(project(":cakk-external"))

	// basic
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-aop:3.3.0")
	implementation("org.springframework:spring-tx")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	// test
	testImplementation("com.tngtech.archunit:archunit-junit5:1.1.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
	testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter:1.0.23")
	testImplementation("io.kotest:kotest-runner-junit5:${property("kotestVersion")}")
	testImplementation("io.mockk:mockk:${property("mockKVersion")}")

	// Jwt
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// Point
	implementation("org.locationtech.jts:jts-core:1.18.2")

	// Mail
	implementation("org.springframework.boot:spring-boot-starter-mail")

	// Slack
	implementation("net.gpedro.integrations.slack:slack-webhook:1.4.0")
}

tasks.bootJar {
	enabled = false
}

tasks.jar {
	enabled = true
}
