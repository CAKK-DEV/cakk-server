package com.cakk.domain.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {

	@Bean
	@ConfigurationProperties(prefix = "storage.datasource.core")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}

	@Bean
	@Primary
	public DataSource dataSource() {
		return new HikariDataSource(hikariConfig());
	}
}
