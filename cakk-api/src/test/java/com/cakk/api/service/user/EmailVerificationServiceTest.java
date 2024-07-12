package com.cakk.api.service.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ServiceTest;
import com.cakk.api.dto.request.user.GenerateCodeRequest;
import com.cakk.api.dto.request.user.VerifyEmailRequest;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.redis.repository.EmailVerificationRedisRepository;

public class EmailVerificationServiceTest extends ServiceTest {

	@InjectMocks
	private EmailVerificationService emailVerificationService;

	@Mock
	private EmailVerificationRedisRepository emailVerificationRedisRepository;

	@Mock
	private ApplicationEventPublisher applicationEventPublisher;

	@TestWithDisplayName("이메일 인증 코드를 생성하고 메일을 전송한다.")
	void sendEmailForVerification() {
		// given
		final GenerateCodeRequest dto = getConstructorMonkey().giveMeOne(GenerateCodeRequest.class);

		// when
		emailVerificationService.sendEmailForVerification(dto);

		// then
		verify(emailVerificationRedisRepository, times(1)).save(anyString(), anyString());
	}

	@TestWithDisplayName("이메일 인증 코드를 검증한다.")
	void checkEmailVerificationCode() {
		// given
		final VerifyEmailRequest dto = getConstructorMonkey().giveMeOne(VerifyEmailRequest.class);

		doReturn(dto.code()).when(emailVerificationRedisRepository).findCodeByEmail(dto.email());

		// when
		emailVerificationService.checkEmailVerificationCode(dto);

		// then
		verify(emailVerificationRedisRepository, times(1)).findCodeByEmail(anyString());
	}

	@TestWithDisplayName("이메일 인증 코드가 올바르지 않으면 검증 시 에러를 반환한다.")
	void checkEmailVerificationCode2() {
		// given
		final VerifyEmailRequest dto = getConstructorMonkey().giveMeOne(VerifyEmailRequest.class);

		doReturn("올바른인증번호").when(emailVerificationRedisRepository).findCodeByEmail(dto.email());

		// when & then
		assertThrows(
			CakkException.class,
			() -> emailVerificationService.checkEmailVerificationCode(dto),
			ReturnCode.WRONG_VERIFICATION_CODE.getMessage()
		);

		verify(emailVerificationRedisRepository, times(1)).findCodeByEmail(anyString());
	}
}
