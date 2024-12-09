package com.cakk.infrastructure.cache.repository;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.RedisKey;
import com.cakk.infrastructure.cache.annotation.RedisRepository;
import com.cakk.infrastructure.cache.template.RedisZSetTemplate;

@RedisRepository
@RequiredArgsConstructor
public class CakeShopViewsRedisRepository {

	private final RedisZSetTemplate<Long> redisZSetTemplate;

	private final String key = RedisKey.VIEWS_CAKE_SHOP.getValue();

	public void saveOrIncreaseSearchCount(final Long value) {
		redisZSetTemplate.save(key, value);
		redisZSetTemplate.increaseScore(key, value, 1);
	}

	public List<Long> findTopShopIdsByOffsetAndCount(final long offset, final long count) {
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
