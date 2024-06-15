package com.cakk.domain.redis.repository;

import java.util.function.Supplier;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.redis.annotation.RedisRepository;
import com.cakk.domain.redis.dto.param.ExecuteWithLockParam;

@RedisRepository
@RequiredArgsConstructor
public class LockRedisRepository {

	private final RedissonClient redissonClient;

	public Object executeWithLock(final ExecuteWithLockParam param) {
		final String lockName = param.keyAsString();
		final Supplier<Object> supplier = param.supplier();
		final RLock rLock = redissonClient.getLock(lockName);

		try {
			boolean available = rLock.tryLock(param.waitTime(), param.leaseTime(), param.timeUnit());

			if (!available) {
				System.out.println("못 얻음");
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
