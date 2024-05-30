package com.cakk.domain.redis.repository;

import java.util.List;

public interface RedisSetRepository<T> {

	void save(String value);

	void increaseScore(String value, int delta);

	List<T> findReverseByScoreRange(double min, double max);
}
