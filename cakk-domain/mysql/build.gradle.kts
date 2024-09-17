description = "mysql module"

dependencies {
	implementation(project(":cakk-common"))

    // jpa
    api("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.7.4")

	// test
	testImplementation("com.navercorp.fixturemonkey:fixture-monkey-starter:1.0.23")
	testImplementation("org.assertj:assertj-core")
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.mockito:mockito-core")
	testImplementation("org.mockito:mockito-junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // querydsl
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    annotationProcessor("com.querydsl:querydsl-apt:5.0.0:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
	implementation("org.hibernate.orm:hibernate-spatial:6.4.4.Final")
	implementation("com.querydsl:querydsl-spatial")

    // database
    runtimeOnly("com.mysql:mysql-connector-j:8.2.0")
    runtimeOnly("com.h2database:h2")

	//serialize
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	implementation("com.fasterxml.jackson.core:jackson-databind")

	// log
	implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.9.0")
}

tasks.bootJar {
	enabled = false
}

tasks.jar {
	enabled = true
}

tasks.test {
	testLogging {
		showStandardStreams = true
		showCauses = true
		showExceptions = true
		showStackTraces = true
		exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
	}
}
