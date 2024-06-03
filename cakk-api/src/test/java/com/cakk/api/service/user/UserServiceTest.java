package com.cakk.api.service.user;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import net.jqwik.api.Arbitraries;
import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ServiceTest;
import com.cakk.api.dto.request.user.ProfileUpdateRequest;
import com.cakk.common.enums.Provider;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.UserReader;
import com.cakk.domain.mysql.repository.writer.UserWriter;

@DisplayName("유저 관련 비즈니스 로직 테스트")
class UserServiceTest extends ServiceTest {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserReader userReader;

	@Mock
	private UserWriter userWriter;

	@TestWithDisplayName("유저 프로필을 수정한다.")
	void updateInformation() {
		// given
		final User user = getReflectionMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("provider", Arbitraries.of(Provider.class))
			.set("providerId", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.sample();
		final ProfileUpdateRequest request = getConstructorMonkey().giveMeOne(ProfileUpdateRequest.class);

		doReturn(user).when(userReader).findByUserId(user.getId());

		// when & then
		Assertions.assertDoesNotThrow(() -> userService.updateInformation(user, request));

		verify(userReader, times(1)).findByUserId(user.getId());
	}
}
