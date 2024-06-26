package com.cakk.domain.redis.template.impl;

import static java.util.Objects.*;

import java.util.List;
import java.util.Set;

import jakarta.annotation.PostConstruct;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.redis.annotation.RedisCustomTemplate;
import com.cakk.domain.redis.template.RedisZSetTemplate;

@RedisCustomTemplate
@RequiredArgsConstructor
public class RedisStringZSetTemplate implements RedisZSetTemplate<String> {

	private final RedisTemplate<String, String> redisTemplate;

	private ZSetOperations<String, String> zSetOperations;

	@PostConstruct
	private void init() {
		zSetOperations = redisTemplate.opsForZSet();
	}

	@Override
	public void save(final String key, final String value) {
		zSetOperations.addIfAbsent(key, value, 0);
	}

	@Override
	public void increaseScore(final String key, final String value, final int delta) {
		zSetOperations.incrementScore(key, value, delta);
	}

	@Override
	public List<String> findAll(String key) {
		final Set<String> data = zSetOperations.rangeByScore(key, 0, Double.MAX_VALUE);

		if (isNull(data) || data.isEmpty()) {
			return List.of();
		}

		return List.copyOf(data);
	}

	@Override
	public List<String> findAllReverseScore(final String key, final long count) {
		final Set<String> data = zSetOperations.reverseRangeByScore(key, 0, Double.MAX_VALUE, 0, count);

		if (isNull(data) || data.isEmpty()) {
			return List.of();
		}

		return List.copyOf(data);
	}

	@Override
	public List<String> findAllReverseScore(final String key, final long offset, final long count) {
		final Set<String> data = zSetOperations.reverseRangeByScore(key, 0, Double.MAX_VALUE, offset, count);

		if (isNull(data) || data.isEmpty()) {
			return List.of();
		}

		return List.copyOf(data);
	}

	@Override
	public void remove(String key, String value) {
		zSetOperations.remove(key, value);
	}

	@Override
	public void removeAll(String key) {
		zSetOperations.removeRangeByScore(key, 0, Double.MAX_VALUE);
	}
}
