description = "redis module"

dependencies {
	implementation(project(":cakk-common"))

	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.redisson:redisson-spring-boot-starter:3.31.0")
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
