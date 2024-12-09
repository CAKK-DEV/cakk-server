package com.cakk.infrastructure.cache.repository;

import java.util.function.Supplier;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import lombok.RequiredArgsConstructor;

import com.cakk.infrastructure.cache.annotation.RedisRepository;
import com.cakk.infrastructure.cache.dto.param.ExecuteWithLockParam;

@RedisRepository
@RequiredArgsConstructor
public class LockRedisRepository {

	private final RedissonClient redissonClient;

	public Object executeWithLock(final ExecuteWithLockParam param) {
		final String lockName = param.key();
		final Supplier<Object> supplier = param.supplier();
		final RLock rLock = redissonClient.getLock(lockName);

		try {
			boolean available = rLock.tryLock(param.waitTime(), param.leaseTime(), param.timeUnit());

			if (!available) {
				return false;
			}

			return supplier.get();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			rLock.unlock();
		}
	}
}
