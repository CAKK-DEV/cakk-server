package com.cakk.domain.repository.writer;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.annotation.Writer;
import com.cakk.domain.entity.user.User;
import com.cakk.domain.repository.jpa.UserJpaRepository;

@Writer
@RequiredArgsConstructor
public class UserWriter {

	private final UserJpaRepository userJpaRepository;

	public User create(User user) {
		userJpaRepository.findByProviderId(user.getProviderId())
			.ifPresent(it -> {
				throw new CakkException(ReturnCode.ALREADY_EXIST_USER);
			});

		return userJpaRepository.save(user);
	}
}
