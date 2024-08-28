package com.cakk.domain.redis.repository;

import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.RedisKey;
import com.cakk.domain.redis.annotation.RedisRepository;
import com.cakk.domain.redis.template.RedisValueTemplate;

@RedisRepository
@RequiredArgsConstructor
public class EmailVerificationRedisRepository {

	private final RedisValueTemplate<String> redisValueTemplate;

	private final String key = RedisKey.EMAIL_VERIFICATION.getValue();

	public void save(final String email, final String verificationCode) {
		deleteByEmail(email);
		redisValueTemplate.save(key + email, verificationCode, 180, TimeUnit.SECONDS);
	}

	public String findCodeByEmail(final String email) {
		return redisValueTemplate.findByKey(key + email);
	}

	public Boolean existByEmail(final String email) {
		return redisValueTemplate.existByKey(key + email);
	}

	public Boolean deleteByEmail(final String email) {
		return redisValueTemplate.delete(key + email);
	}
}
