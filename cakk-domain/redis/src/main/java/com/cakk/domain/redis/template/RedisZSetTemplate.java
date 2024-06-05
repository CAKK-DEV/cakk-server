package com.cakk.domain.redis.template;

import java.util.List;

public interface RedisZSetTemplate<T> {

	void save(String key, String value);

	void increaseScore(String key, String value, int delta);

	List<T> findAllReverseScore(String key, long count);

	void removeAll(String key);
}
