package com.cakk.api.service.user;

import static com.cakk.common.utils.RandomUtilsKt.*;
import static java.util.Objects.*;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.event.EmailWithVerificationCodeSendEvent;
import com.cakk.api.dto.request.user.GenerateCodeRequest;
import com.cakk.api.dto.request.user.VerifyEmailRequest;
import com.cakk.api.mapper.EventMapper;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.redis.repository.EmailVerificationRedisRepository;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

	private final EmailVerificationRedisRepository emailVerificationRedisRepository;

	private final ApplicationEventPublisher applicationEventPublisher;

	public void sendEmailForVerification(final GenerateCodeRequest dto) {
		final String email = dto.email();
		final String code = generateRandomStringOnlyNumber(6);
		emailVerificationRedisRepository.save(email, code);

		final EmailWithVerificationCodeSendEvent emailEvent = EventMapper.supplyEmailWithVerificationCodeSendEventBy(email, code);
		applicationEventPublisher.publishEvent(emailEvent);
	}

	public void checkEmailVerificationCode(final VerifyEmailRequest dto) {
		final String email = dto.email();
		final String code = dto.code();
		final String verificationCode = emailVerificationRedisRepository.findCodeByEmail(email);

		if (isNull(verificationCode) || !verificationCode.equals(code)) {
			throw new CakkException(ReturnCode.WRONG_VERIFICATION_CODE);
		}

		emailVerificationRedisRepository.deleteByEmail(email);
	}
}
