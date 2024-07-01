package com.cakk.domain.redis.repository;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.RedisKey;
import com.cakk.domain.redis.annotation.RedisRepository;
import com.cakk.domain.redis.template.impl.RedisLongZSetTemplate;

@RedisRepository
@RequiredArgsConstructor
public class CakeShopViewsRedisRepository {

	private final RedisLongZSetTemplate redisLongZSetTemplate;

	private final String key = RedisKey.VIEWS_CAKE_SHOP.getValue();

	public void saveOrIncreaseSearchCount(final Long value) {
		redisLongZSetTemplate.save(key, value);
		redisLongZSetTemplate.increaseScore(key, value, 1);
	}

	public List<Long> findTopShopIdsByOffsetAndCount(final long offset, final long count) {
		return redisLongZSetTemplate.findAllReverseScore(key, offset, count);
	}

	public List<Long> findAll() {
		return redisLongZSetTemplate.findAll(key);
	}

	public void deleteByValue(final Long value) {
		redisLongZSetTemplate.remove(key, value);
	}

	public void clear() {
		redisLongZSetTemplate.removeAll(key);
	}
}
