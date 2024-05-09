package com.cakk.domain.repository.reader;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.annotation.Reader;
import com.cakk.domain.entity.user.User;
import com.cakk.domain.repository.jpa.UserJpaRepository;

@Reader
@RequiredArgsConstructor
public class UserReader {

	private final UserJpaRepository userJpaRepository;

	public User findByUserId(Long userId) {
		return userJpaRepository.findById(userId).orElseThrow(() -> new CakkException(ReturnCode.WRONG_PARAMETER));
	}
}
