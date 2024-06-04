package com.cakk.domain.redis.template.impl;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PostConstruct;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.redis.annotation.RedisCustomTemplate;
import com.cakk.domain.redis.template.RedisValueTemplate;

@RedisCustomTemplate
@RequiredArgsConstructor
public class RedisStringValueTemplate implements RedisValueTemplate<String> {

	private final RedisTemplate<String, String> redisTemplate;

	private ValueOperations<String, String> valueOperations;

	@PostConstruct
	private void init() {
		valueOperations = redisTemplate.opsForValue();
	}

	@Override
	public void save(final String key, final String value, final long timeout, final TimeUnit unit) {
		valueOperations.set(key, value, timeout, unit);
	}

	@Override
	public String findByKey(final String key) {
		final String value = valueOperations.get(key);

		return Objects.isNull(value) ? null : value;
	}

	@Override
	public Boolean existByKey(final String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	@Override
	public Boolean delete(final String key) {
		return Boolean.TRUE.equals(redisTemplate.delete(key));
	}
}
