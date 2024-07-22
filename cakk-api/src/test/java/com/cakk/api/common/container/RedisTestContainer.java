package com.cakk.api.common.container;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@Configuration
public class RedisTestContainer {

	private static final String REDIS_IMAGE = "redis:7.2-alpine";
	private static final int REDIS_PORT = 6379;
	private static final String REDIS_PASSWORD = "test_redis_password";

	@Container
	public static final GenericContainer<?> REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE))
		.withExposedPorts(REDIS_PORT)
		.withReuse(true);

	static {
		REDIS_CONTAINER.start();

		System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
		System.setProperty("spring.data.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
		System.setProperty("spring.data.redis.password", REDIS_PASSWORD);
	}
}
