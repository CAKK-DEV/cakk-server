package com.cakk.api.common.container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class RedisTestContainer {

	private static final String REDIS_IMAGE = "redis:7.2-alpine";
	private static final int REDIS_PORT = 6379;

	@Container
	public static final GenericContainer REDIS_CONTAINER = new GenericContainer(REDIS_IMAGE)
		.withExposedPorts(REDIS_PORT)
		.withReuse(true);

	static {
		REDIS_CONTAINER.start();
	}
}
