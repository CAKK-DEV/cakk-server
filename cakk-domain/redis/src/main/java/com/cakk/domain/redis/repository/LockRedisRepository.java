package com.cakk.domain.redis.repository;

import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.RedisKey;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.redis.annotation.RedisRepository;
import com.cakk.domain.redis.template.impl.RedisStringValueTemplate;

@RedisRepository
@RequiredArgsConstructor
public class LockRedisRepository {

	private final RedisStringValueTemplate redisStringValueTemplate;

	public void executeWithLock(final RedisKey key, final long timeout, Runnable task) {
		final String lockName = key.getValue();

		try {
			while (!getLock(lockName, timeout)) {
				Thread.sleep(100);
			}
			task.run();
		} catch (InterruptedException e) {
			throw new CakkException(ReturnCode.LOCK_RESOURCES_ERROR);
		} finally {
			releaseLock(lockName);
		}
	}

	private boolean getLock(final String key, final long timeout) {
		return redisStringValueTemplate.saveIfAbsent(key, "lock", timeout, TimeUnit.MILLISECONDS);
	}

	private void releaseLock(final String key) {
		final boolean result = redisStringValueTemplate.delete(key);

		checkResult(result);
	}

	private void checkResult(final boolean result) {
		if (!result) {
			throw new CakkException(ReturnCode.LOCK_RESOURCES_ERROR);
		}
	}
}
