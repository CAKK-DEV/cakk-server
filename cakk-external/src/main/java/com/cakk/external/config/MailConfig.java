package com.cakk.external.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

	private final String host;
	private final int port;
	private final String username;
	private final String password;
	private final boolean auth;
	private final boolean starttlsEnable;
	private final boolean starttlsRequired;
	private final int connectionTimeout;
	private final int timeout;
	private final int writeTimeout;

	public MailConfig(
		@Value("${spring.mail.host}") String host,
		@Value("${spring.mail.port}") int port,
		@Value("${spring.mail.username}") String username,
		@Value("${spring.mail.password}") String password,
		@Value("${spring.mail.properties.mail.smtp.auth}") boolean auth,
		@Value("${spring.mail.properties.mail.smtp.starttls.enable}") boolean starttlsEnable,
		@Value("${spring.mail.properties.mail.smtp.starttls.required}") boolean starttlsRequired,
		@Value("${spring.mail.properties.mail.smtp.connectiontimeout}") int connectionTimeout,
		@Value("${spring.mail.properties.mail.smtp.timeout}") int timeout,
		@Value("${spring.mail.properties.mail.smtp.writetimeout}") int writeTimeout
	) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.auth = auth;
		this.starttlsEnable = starttlsEnable;
		this.starttlsRequired = starttlsRequired;
		this.connectionTimeout = connectionTimeout;
		this.timeout = timeout;
		this.writeTimeout = writeTimeout;
	}

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		mailSender.setDefaultEncoding("UTF-8");
		mailSender.setJavaMailProperties(getMailProperties());

		return mailSender;
	}

	private Properties getMailProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", auth);
		properties.put("mail.smtp.starttls.enable", starttlsEnable);
		properties.put("mail.smtp.starttls.required", starttlsRequired);
		properties.put("mail.smtp.connectiontimeout", connectionTimeout);
		properties.put("mail.smtp.timeout", timeout);
		properties.put("mail.smtp.writetimeout", writeTimeout);

		return properties;
	}
}
