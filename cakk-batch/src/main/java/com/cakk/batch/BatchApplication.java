package com.cakk.batch;

import java.util.TimeZone;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.RequiredArgsConstructor;

import com.cakk.batch.job.launcher.WeeklyJobLauncher;

@RequiredArgsConstructor
@SpringBootApplication
public class BatchApplication implements ApplicationRunner {

	private final WeeklyJobLauncher jobLauncher;

	@PostConstruct
	public static void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		started();

		final ConfigurableApplicationContext context = SpringApplication.run(BatchApplication.class, args);
		final int exitCode = SpringApplication.exit(context, () -> 0);

		System.exit(exitCode);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		jobLauncher.launch();
	}
}
