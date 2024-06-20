package com.cakk.batch.tasklet;

import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.cakk.batch.utils.BatchConstants;
import com.cakk.domain.redis.repository.KeywordRedisRepository;

@StepScope
@Component
@RequiredArgsConstructor
public class FindKeywordListTaskLet implements Tasklet {

	private final KeywordRedisRepository keywordRedisRepository;

	@Override
	public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) {
		final ExecutionContext executionContext = chunkContext.getStepContext()
			.getStepExecution()
			.getJobExecution()
			.getExecutionContext();

		final List<String> keywords = keywordRedisRepository.findAll();
		executionContext.put(BatchConstants.KEYWORD_LIST, keywords);

		return RepeatStatus.FINISHED;
	}
}
