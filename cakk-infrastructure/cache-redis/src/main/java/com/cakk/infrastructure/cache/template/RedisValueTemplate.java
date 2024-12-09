package com.cakk.infrastructure.cache.template;

import java.util.concurrent.TimeUnit;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RedisValueTemplate<T> {

	void save(String key, T value, long timeout, TimeUnit unit);

	Boolean saveIfAbsent(String key, T value, long timeout, TimeUnit unit);

	T findByKey(String key);

	Boolean existByKey(String key);

	Boolean delete(String key);
}
