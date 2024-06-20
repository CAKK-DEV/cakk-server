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
import com.cakk.batch.tasklet.ClearKeywordListTasklet;
import com.cakk.batch.tasklet.FindKeywordListTaskLet;

@RequiredArgsConstructor
@Configuration
public class ClearKeywordListJobConfig {

	private static final String JOB_NAME = "clearKeywordListJob";
	private static final String FIND_KEYWORD_LIST_STEP_NAME = "findKeywordListStep";
	private static final String CLEAR_KEYWORD_LIST_STEP_NAME = "clearKeywordListStep";

	private final FindKeywordListTaskLet findKeywordListTaskLet;
	private final ClearKeywordListTasklet clearKeywordListTasklet;

	private final JobLoggingListener jobLoggingListener;
	private final PlatformTransactionManager transactionManager;
	private final JobRepository jobRepository;

	@Bean(name = JOB_NAME)
	public Job runJob() {
		return new JobBuilder(JOB_NAME, jobRepository)
			.listener(jobLoggingListener)
			.start(findKeywordListStep())
			.next(clearKeywordListTasklet())
			.build();
	}

	@JobScope
	@Bean(name = FIND_KEYWORD_LIST_STEP_NAME)
	public Step findKeywordListStep() {
		return new StepBuilder(FIND_KEYWORD_LIST_STEP_NAME, jobRepository)
			.tasklet(findKeywordListTaskLet, transactionManager)
			.build();
	}

	@JobScope
	@Bean(name = CLEAR_KEYWORD_LIST_STEP_NAME)
	public Step clearKeywordListTasklet() {
		return new StepBuilder(CLEAR_KEYWORD_LIST_STEP_NAME, jobRepository)
			.tasklet(clearKeywordListTasklet, transactionManager)
			.build();
	}
}
