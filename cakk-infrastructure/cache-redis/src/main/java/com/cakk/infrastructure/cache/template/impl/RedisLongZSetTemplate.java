package com.cakk.infrastructure.cache.template.impl;

import static java.util.Objects.*;

import java.util.List;
import java.util.Set;

import jakarta.annotation.PostConstruct;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import lombok.RequiredArgsConstructor;

import com.cakk.infrastructure.cache.annotation.RedisCustomTemplate;
import com.cakk.infrastructure.cache.template.RedisZSetTemplate;

@RedisCustomTemplate
@RequiredArgsConstructor
public class RedisLongZSetTemplate implements RedisZSetTemplate<Long> {

	private final RedisTemplate<String, String> redisTemplate;

	private ZSetOperations<String, String> zSetOperations;

	@PostConstruct
	private void init() {
		zSetOperations = redisTemplate.opsForZSet();
	}

	@Override
	public void save(final String key, final Long value) {
		zSetOperations.addIfAbsent(key, String.valueOf(value), 0);
	}

	@Override
	public void increaseScore(final String key, final Long value, final int delta) {
		zSetOperations.incrementScore(key, String.valueOf(value), delta);
	}

	@Override
	public List<Long> findAll(final String key) {
		final Set<String> data = zSetOperations.rangeByScore(key, 0, Double.MAX_VALUE);

		if (isNull(data) || data.isEmpty()) {
			return List.of();
		}

		return List.copyOf(data).stream().map(Long::parseLong).toList();
	}

	@Override
	public List<Long> findAllReverseScore(final String key, final long count) {
		final Set<String> data = zSetOperations.reverseRangeByScore(key, 0, Double.MAX_VALUE, 0, count);

		if (isNull(data) || data.isEmpty()) {
			return List.of();
		}

		return List.copyOf(data).stream().map(Long::parseLong).toList();
	}

	@Override
	public List<Long> findAllReverseScore(final String key, final long offset, final long count) {
		final Set<String> data = zSetOperations.reverseRangeByScore(key, 0, Double.MAX_VALUE, offset, count);

		if (isNull(data) || data.isEmpty()) {
			return List.of();
		}

		return List.copyOf(data).stream().map(Long::parseLong).toList();
	}

	@Override
	public void remove(final String key, final Long value) {
		zSetOperations.remove(key, String.valueOf(value));
	}

	@Override
	public void removeAll(String key) {
		zSetOperations.removeRangeByScore(key, 0, Double.MAX_VALUE);
	}
}
