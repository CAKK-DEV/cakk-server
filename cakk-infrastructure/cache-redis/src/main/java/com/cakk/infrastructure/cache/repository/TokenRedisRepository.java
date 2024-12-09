package com.cakk.infrastructure.cache.repository;

import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.RedisKey;
import com.cakk.infrastructure.cache.annotation.RedisRepository;
import com.cakk.infrastructure.cache.template.RedisValueTemplate;

@RedisRepository
@RequiredArgsConstructor
public class TokenRedisRepository {

	private final RedisValueTemplate<String> redisValueTemplate;

	private final String key = RedisKey.REFRESH_TOKEN.getValue();

	public void registerBlackList(final String token, final long timeout) {
		redisValueTemplate.save(key + token, "token", timeout, TimeUnit.MILLISECONDS);
	}

	public Boolean isBlackListToken(final String token) {
		return redisValueTemplate.existByKey(key + token);
	}

	public void deleteByToken(final String token) {
		redisValueTemplate.delete(key + token);
	}
}
