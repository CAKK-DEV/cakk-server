package com.cakk.domain.repository.writer;

import com.cakk.domain.annotation.Writer;
import com.cakk.domain.entity.user.User;
import com.cakk.domain.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;

@Writer
@RequiredArgsConstructor
public class UserWriter {

	private final UserJpaRepository userJpaRepository;

	public User create(User user) {
		return userJpaRepository.save(user);
	}
}
