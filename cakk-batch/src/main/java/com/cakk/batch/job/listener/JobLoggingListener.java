package com.cakk.batch.job.listener;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.cakk.batch.service.SlackService;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobLoggingListener implements JobExecutionListener {

	private static final String BEFORE_MESSAGE = "{} Job is starting.";
	private static final String AFTER_MESSAGE = "{} Job is finished. (Status: {})";

	private final SlackService slackService;

	@Override
	public void beforeJob(final JobExecution jobExecution) {
		log.info(BEFORE_MESSAGE, jobExecution.getJobInstance().getJobName());
		slackService.sendJobStartMessage(jobExecution.getJobInstance().getJobName());
	}

	@Override
	public void afterJob(final JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.FAILED) {
			log.error("{} Job Failed", jobExecution.getJobInstance().getJobName());
			slackService.sendJobFailMessage(jobExecution.getJobInstance().getJobName());
		}

		log.info(AFTER_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
		slackService.sendJobFinishMessage(jobExecution.getJobInstance().getJobName());
	}
}
