package com.cakk.domain.entity.user;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import net.jqwik.api.Arbitraries;

import com.cakk.common.enums.Provider;
import com.cakk.domain.base.DomainTest;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.entity.user.UserWithdrawal;

class UserTest extends DomainTest {

	private User getUserFixture() {
		return getConstructorMonkey().giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().greaterOrEqual(10))
			.set("provider", Arbitraries.of(Provider.class))
			.set("providerId", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.sample();
	}

	@Test
	void 유저_탈퇴_정보를_유저로부터_공급_받는다() {
		// given
		User user = getUserFixture();
		// when
		UserWithdrawal userWithdrawal = user.toWithdrawEntity();

		// then
		assertThat(userWithdrawal.getEmail()).isEqualTo(user.getEmail());
		assertThat(userWithdrawal.getGender()).isEqualTo(user.getGender());
		assertThat(userWithdrawal.getBirthday()).isEqualTo(user.getBirthday());
		assertThat(userWithdrawal.getRole()).isEqualTo(user.getRole());
		assertThat(userWithdrawal.getWithdrawalDate().toLocalDate()).isEqualTo(LocalDate.now());
	}
}
