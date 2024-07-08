package com.cakk.domain.redis.repository;

import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.RedisKey;
import com.cakk.domain.redis.annotation.RedisRepository;
import com.cakk.domain.redis.template.impl.RedisStringValueTemplate;

@RedisRepository
@RequiredArgsConstructor
public class TokenRedisRepository {

	private final RedisStringValueTemplate redisStringValueTemplate;

	private final String key = RedisKey.REFRESH_TOKEN.getValue();

	public void registerBlackList(final String token, final long timeout) {
		redisStringValueTemplate.save(key + token, "token", timeout, TimeUnit.MILLISECONDS);
	}

	public Boolean isBlackListToken(final String token) {
		return redisStringValueTemplate.existByKey(key + token);
	}

	public void deleteByToken(final String token) {
		redisStringValueTemplate.delete(key + token);
	}
}
