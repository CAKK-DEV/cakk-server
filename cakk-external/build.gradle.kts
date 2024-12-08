description = "external module"

dependencies {
	implementation(projects.common)

	// Basic
	implementation(libs.spring.context)
	implementation(libs.spring.web)

	// AWS
	implementation(libs.aws.java.sdk)

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
