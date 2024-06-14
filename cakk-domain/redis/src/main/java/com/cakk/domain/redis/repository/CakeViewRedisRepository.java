package com.cakk.domain.redis.repository;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.RedisKey;
import com.cakk.domain.redis.annotation.RedisRepository;
import com.cakk.domain.redis.template.impl.RedisLongZSetTemplate;

@RedisRepository
@RequiredArgsConstructor
public class CakeViewRedisRepository {

	private final RedisLongZSetTemplate redisLongZSetTemplate;

	private final String key = RedisKey.VIEW_CAKE.getValue();

	public void saveOrIncreaseSearchCount(final Long value) {
		redisLongZSetTemplate.save(key, value);
		redisLongZSetTemplate.increaseScore(key, value, 1);
	}

	public List<Long> findTopCakeIdsByOffsetAndCount(final long offset, final long count) {
		return redisLongZSetTemplate.findAllReverseScore(key, offset, count);
	}

	public void clear() {
		redisLongZSetTemplate.removeAll(key);
	}
}
