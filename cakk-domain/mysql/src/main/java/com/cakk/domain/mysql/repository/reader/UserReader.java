package com.cakk.domain.mysql.repository.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.UserJpaRepository;

@Reader
@RequiredArgsConstructor
public class UserReader {

	private final UserJpaRepository userJpaRepository;

	public User findByUserId(final Long userId) {
		return userJpaRepository.findById(userId).orElseThrow(() -> new CakkException(ReturnCode.NOT_EXIST_USER));
	}

	public User findByProviderId(final String providerId) {
		return userJpaRepository.findByProviderId(providerId).orElseThrow(() -> new CakkException(ReturnCode.NOT_EXIST_USER));
	}

	public List<User> findAll() {
		return userJpaRepository.findAll();
	}
}
