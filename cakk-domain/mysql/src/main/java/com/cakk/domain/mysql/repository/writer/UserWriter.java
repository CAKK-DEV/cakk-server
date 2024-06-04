package com.cakk.domain.mysql.repository.writer;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.annotation.Writer;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.UserJpaRepository;
import com.cakk.domain.mysql.repository.jpa.UserWithdrawalJpaRepository;

@Writer
@RequiredArgsConstructor
public class UserWriter {

	private final UserJpaRepository userJpaRepository;
	private final UserWithdrawalJpaRepository userWithdrawalJpaRepository;

	public User create(final User user) {
		userJpaRepository.findByProviderId(user.getProviderId())
			.ifPresent(it -> {
				throw new CakkException(ReturnCode.ALREADY_EXIST_USER);
			});

		return userJpaRepository.save(user);
	}

	public void delete(final User user) {
		userWithdrawalJpaRepository.save(user.toWithdrawEntity());
		userJpaRepository.delete(user);
	}
}
