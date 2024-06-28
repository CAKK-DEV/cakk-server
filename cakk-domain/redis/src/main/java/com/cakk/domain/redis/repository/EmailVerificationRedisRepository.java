package com.cakk.domain.redis.repository;

import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.RedisKey;
import com.cakk.domain.redis.annotation.RedisRepository;
import com.cakk.domain.redis.template.impl.RedisStringValueTemplate;

@RedisRepository
@RequiredArgsConstructor
public class EmailVerificationRedisRepository {

	private final RedisStringValueTemplate redisStringValueTemplate;

	private final String key = RedisKey.EMAIL_VERIFICATION.getValue();

	public void save(final String email, final String verificationCode) {
		deleteByEmail(email);
		redisStringValueTemplate.save(key + email, verificationCode, 180, TimeUnit.SECONDS);
	}

	public String findCodeByEmail(final String email) {
		return redisStringValueTemplate.findByKey(key + email);
	}

	public Boolean existByEmail(final String email) {
		return redisStringValueTemplate.existByKey(key + email);
	}

	public Boolean deleteByEmail(final String email) {
		return redisStringValueTemplate.delete(key + email);
	}
}
