package com.cakk.domain.redis.repository;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.RedisKey;
import com.cakk.domain.redis.annotation.RedisRepository;
import com.cakk.domain.redis.template.RedisZSetTemplate;

@RedisRepository
@RequiredArgsConstructor
public class CakeViewsRedisRepository {

	private final RedisZSetTemplate<Long> redisZSetTemplate;

	private final String key = RedisKey.VIEWS_CAKE.getValue();

	public void saveOrIncreaseSearchCount(final Long value) {
		redisZSetTemplate.save(key, value);
		redisZSetTemplate.increaseScore(key, value, 1);
	}

	public List<Long> findTopCakeIdsByOffsetAndCount(final long offset, final long count) {
		return redisZSetTemplate.findAllReverseScore(key, offset, count);
	}

	public List<Long> findAll() {
		return redisZSetTemplate.findAll(key);
	}

	public void deleteByValue(final Long value) {
		redisZSetTemplate.remove(key, value);
	}

	public void clear() {
		redisZSetTemplate.removeAll(key);
	}
}
