package com.cakk.api.common.container;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class MysqlTestContainer {

	private static final String MYSQL_VERSION = "mysql:8";
	private static final String DATABASE_NAME = "cakk";

	@Container
	public static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer(MYSQL_VERSION)
		.withDatabaseName(DATABASE_NAME);

	static {
		MYSQL_CONTAINER.start();
	}
}
