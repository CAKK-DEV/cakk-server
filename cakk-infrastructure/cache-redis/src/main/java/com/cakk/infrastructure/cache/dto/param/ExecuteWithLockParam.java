package com.cakk.infrastructure.cache.dto.param;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import lombok.Builder;

import com.cakk.common.enums.RedisKey;

@Builder
public record ExecuteWithLockParam(
	String key,
	Supplier<Object> supplier,
	long waitTime,
	long leaseTime,
	TimeUnit timeUnit
) {
}
