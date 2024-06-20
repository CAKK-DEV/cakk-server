package com.cakk.batch.job.launcher;

import java.time.LocalDateTime;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.cakk.batch.job.config.ClearKeywordListJobConfig;
import com.cakk.batch.job.config.ClearViewsCakeIdListJobConfig;

@RequiredArgsConstructor
@Component
public class WeeklyJobLauncher {

	private final JobLauncher jobLauncher;

	private final ClearKeywordListJobConfig clearKeywordListJobConfig;
	private final ClearViewsCakeIdListJobConfig clearViewsCakeIdListJobConfig;

	public void launch() throws Exception {
		final JobParameters clearKeywordListJobParams = new JobParametersBuilder()
			.addLocalDateTime("clearKeywordListDate", LocalDateTime.now())
			.toJobParameters();

		final JobParameters clearViewsCakeIdListJobParams = new JobParametersBuilder()
			.addLocalDateTime("clearViewsCakeIdListDate", LocalDateTime.now())
			.toJobParameters();

		jobLauncher.run(clearKeywordListJobConfig.runJob(), clearKeywordListJobParams);
		jobLauncher.run(clearViewsCakeIdListJobConfig.runJob(), clearViewsCakeIdListJobParams);
	}
}
