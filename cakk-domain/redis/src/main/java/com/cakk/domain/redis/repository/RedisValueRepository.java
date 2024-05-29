package com.cakk.domain.redis.repository;

import java.util.concurrent.TimeUnit;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RedisValueRepository<T> {

	void save(String key, T value, long timeout, TimeUnit unit);

	T findByKey(String key);

	Boolean existByKey(String key);

	Boolean delete(String key);
}
