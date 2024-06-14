package com.cakk.domain.redis.template;

import java.util.List;

public interface RedisZSetTemplate<T> {

	void save(String key, T value);

	void increaseScore(String key, T value, int delta);

	List<T> findAllReverseScore(String key, long count);

	List<T> findAllReverseScore(String key, long offset, long count);

	void removeAll(String key);
}
