description = "admin module"

tasks.bootJar {
	enabled = true
}

tasks.jar {
	enabled = false
}

dependencies {
	implementation(project(":cakk-common"))
	implementation(project(":cakk-domain:mysql"))
	implementation(project(":cakk-core"))

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

	implementation("org.springframework:spring-tx")

	// Point
	implementation("org.locationtech.jts:jts-core:1.18.2")
}
