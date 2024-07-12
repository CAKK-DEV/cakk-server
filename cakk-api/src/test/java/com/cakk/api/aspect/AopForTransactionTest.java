package com.cakk.api.aspect;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.common.exception.CakkException;

@ActiveProfiles("test")
@SpringBootTest(properties = "spring.profiles.active=test")
public class AopForTransactionTest {

	@Autowired
	private AopForTransaction aopForTransaction;

	@TestWithDisplayName("proceed 메소드가 정상적으로 실행된다.")
	void proceed() throws Throwable {
		// given
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

		doReturn(Object.class).when(joinPoint).proceed();

		// when
		aopForTransaction.proceed(joinPoint);

		// then
		verify(joinPoint, times(1)).proceed();
	}

	@TestWithDisplayName("proceed 메소드 실행 시 CakkException 에러가 터지면 CakkException 에러를 던진다.")
	void proceed2() throws Throwable {
		// given
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

		doThrow(CakkException.class).when(joinPoint).proceed();

		// when & then
		assertThrows(
			CakkException.class,
			() -> aopForTransaction.proceed(joinPoint)
		);

		verify(joinPoint, times(1)).proceed();
	}

	@TestWithDisplayName("proceed 메소드 실행 시 다른 에러가 터지면 RuntimeException 에러를 던진다.")
	void proceed3() throws Throwable {
		// given
		ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);

		doThrow(RuntimeException.class).when(joinPoint).proceed();

		// when & then
		assertThrows(
			RuntimeException.class,
			() -> aopForTransaction.proceed(joinPoint)
		);

		verify(joinPoint, times(1)).proceed();
	}
}
