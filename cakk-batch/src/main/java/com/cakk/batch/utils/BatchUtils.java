package com.cakk.batch.utils;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchUtils {

	public static ExecutionContext getExecutionContext(final ChunkContext chunkContext) {
		return chunkContext.getStepContext()
			.getStepExecution()
			.getJobExecution()
			.getExecutionContext();
	}
}
