package com.cakk.domain.redis.repository.impl;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PostConstruct;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.redis.annotation.RedisRepository;
import com.cakk.domain.redis.repository.RedisValueRepository;

@RedisRepository
@RequiredArgsConstructor
public class TokenRedisRepository implements RedisValueRepository<String> {

	private final RedisTemplate<String, String> redisTemplate;

	private ValueOperations<String, String> valueOperations;

	@PostConstruct
	private void init() {
		valueOperations = redisTemplate.opsForValue();
	}

	public void registerBlackList(String token, long timeout) {
		save(token, "token", timeout, TimeUnit.MILLISECONDS);
	}

	public Boolean isBlackListToken(String token) {
		return existByKey(token);
	}

	@Override
	public void save(String key, String value, long timeout, TimeUnit unit) {
		valueOperations.set(key, value, timeout, unit);
	}

	@Override
	public String findByKey(String key) {
		String value = valueOperations.get(key);

		return Objects.isNull(value) ? null : value;
	}

	@Override
	public Boolean existByKey(String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	@Override
	public Boolean delete(String key) {
		return Boolean.TRUE.equals(redisTemplate.delete(key));
	}
}
