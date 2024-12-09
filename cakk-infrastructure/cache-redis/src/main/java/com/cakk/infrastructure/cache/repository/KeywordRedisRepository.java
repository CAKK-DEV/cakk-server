package com.cakk.infrastructure.cache.repository;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.RedisKey;
import com.cakk.infrastructure.cache.annotation.RedisRepository;
import com.cakk.infrastructure.cache.template.RedisZSetTemplate;

@RedisRepository
@RequiredArgsConstructor
public class KeywordRedisRepository {

	private final RedisZSetTemplate<String> redisZSetTemplate;

	private final String key = RedisKey.SEARCH_KEYWORD.getValue();

	public void saveOrIncreaseSearchCount(final String value) {
		redisZSetTemplate.save(key, value);
		redisZSetTemplate.increaseScore(key, value, 1);
	}

	public List<String> findTopSearchedLimitCount(final long count) {
		return redisZSetTemplate.findAllReverseScore(key, count);
	}

	public List<String> findAll() {
		return redisZSetTemplate.findAll(key);
	}

	public void deleteByValue(final String value) {
		redisZSetTemplate.remove(key, value);
	}

	public void clear() {
		redisZSetTemplate.removeAll(key);
	}
}
