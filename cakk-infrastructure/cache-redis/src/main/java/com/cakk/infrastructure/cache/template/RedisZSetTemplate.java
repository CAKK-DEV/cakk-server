package com.cakk.infrastructure.cache.template;

import java.util.List;

public interface RedisZSetTemplate<T> {

	void save(String key, T value);

	void increaseScore(String key, T value, int delta);

	List<T> findAll(String key);

	List<T> findAllReverseScore(String key, long count);

	List<T> findAllReverseScore(String key, long offset, long count);

	void remove(String key, T value);

	void removeAll(String key);
}
