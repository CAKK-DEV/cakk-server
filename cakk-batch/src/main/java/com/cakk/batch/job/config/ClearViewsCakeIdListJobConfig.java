package com.cakk.batch.job.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

import com.cakk.batch.job.listener.JobLoggingListener;
import com.cakk.batch.tasklet.ClearViewsCakeIdListTasklet;
import com.cakk.batch.tasklet.FindViewsCakeIdListTaskLet;

@RequiredArgsConstructor
@Configuration
public class ClearViewsCakeIdListJobConfig {

	private static final String JOB_NAME = "clearViewsCakeIdListJob";
	private static final String FIND_ID_LIST_STEP_NAME = "findViewsCakeIdListStep";
	private static final String CLEAR_ID_LIST_STEP_NAME = "clearViewsCakeIdListStep";

	private final FindViewsCakeIdListTaskLet findViewsCakeIdListTaskLet;
	private final ClearViewsCakeIdListTasklet clearViewsCakeIdListTasklet;

	private final JobLoggingListener jobLoggingListener;
	private final PlatformTransactionManager transactionManager;
	private final JobRepository jobRepository;

	@Bean(name = JOB_NAME)
	public Job runJob() {
		return new JobBuilder(JOB_NAME, jobRepository)
			.listener(jobLoggingListener)
			.start(findViewsCakeIdListStep())
			.next(clearViewsCakeIdListTasklet())
			.build();
	}

	@JobScope
	@Bean(name = FIND_ID_LIST_STEP_NAME)
	public Step findViewsCakeIdListStep() {
		return new StepBuilder(FIND_ID_LIST_STEP_NAME, jobRepository)
			.tasklet(findViewsCakeIdListTaskLet, transactionManager)
			.build();
	}

	@JobScope
	@Bean(name = CLEAR_ID_LIST_STEP_NAME)
	public Step clearViewsCakeIdListTasklet() {
		return new StepBuilder(CLEAR_ID_LIST_STEP_NAME, jobRepository)
			.tasklet(clearViewsCakeIdListTasklet, transactionManager)
			.build();
	}
}
