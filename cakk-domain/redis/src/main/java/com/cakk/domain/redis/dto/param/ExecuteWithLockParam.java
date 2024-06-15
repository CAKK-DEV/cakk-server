package com.cakk.domain.redis.dto.param;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import lombok.Builder;

import com.cakk.common.enums.RedisKey;

@Builder
public record ExecuteWithLockParam(
	RedisKey key,
	Supplier<Object> supplier,
	long waitTime,
	long leaseTime,
	TimeUnit timeUnit
) {

	public String keyAsString() {
		return key.getValue();
	}
}
