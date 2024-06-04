package com.cakk.domain.redis.repository;

import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.redis.annotation.RedisRepository;
import com.cakk.domain.redis.template.impl.RedisStringValueTemplate;

@RedisRepository
@RequiredArgsConstructor
public class TokenRedisRepository {

	private final RedisStringValueTemplate redisStringValueTemplate;

	public void registerBlackList(final String token, final long timeout) {
		redisStringValueTemplate.save(token, "token", timeout, TimeUnit.MILLISECONDS);
	}

	public Boolean isBlackListToken(final String token) {
		return redisStringValueTemplate.existByKey(token);
	}
}
