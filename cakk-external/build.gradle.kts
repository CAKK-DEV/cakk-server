description = "external module"

dependencies {
	implementation(project(":cakk-common"))

	// Basic
	implementation("org.springframework:spring-context")
	implementation("org.springframework:spring-web")

	// AWS
	implementation("com.amazonaws:aws-java-sdk-s3:1.12.715")

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
