package com.cakk.domain.redis.repository;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.RedisKey;
import com.cakk.domain.redis.annotation.RedisRepository;
import com.cakk.domain.redis.template.impl.RedisStringZSetTemplate;

@RedisRepository
@RequiredArgsConstructor
public class KeywordRedisRepository {

	private final RedisStringZSetTemplate redisStringZSetTemplate;

	private final String key = RedisKey.SEARCH_KEYWORD.getValue();

	public void saveOrIncreaseSearchCount(final String value) {
		redisStringZSetTemplate.save(key, value);
		redisStringZSetTemplate.increaseScore(key, value, 1);
	}

	public List<String> findTopSearchedLimitCount(final long count) {
		return redisStringZSetTemplate.findAllReverseScore(key, count);
	}

	public void clear() {
		redisStringZSetTemplate.removeAll(key);
	}
}
