package com.cakk.batch.tasklet;

import static java.util.Objects.*;

import java.util.List;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.cakk.batch.utils.BatchConstants;
import com.cakk.batch.utils.BatchUtils;
import com.cakk.domain.redis.repository.CakeViewRedisRepository;

@StepScope
@Component
@RequiredArgsConstructor
public class ClearViewsCakeIdListTasklet implements Tasklet {

	private final CakeViewRedisRepository cakeViewRedisRepository;

	@Override
	public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) {
		final ExecutionContext executionContext = BatchUtils.getExecutionContext(chunkContext);
		final List<Long> cakeIds = (List<Long>) executionContext.get(BatchConstants.CAKE_ID_LIST);

		if (nonNull(cakeIds) && !cakeIds.isEmpty()) {
			cakeIds.forEach(cakeViewRedisRepository::deleteByValue);
		}

		contribution.setExitStatus(ExitStatus.COMPLETED);
		return RepeatStatus.FINISHED;
	}
}
