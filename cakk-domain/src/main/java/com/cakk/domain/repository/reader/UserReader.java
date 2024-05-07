package com.cakk.domain.repository.reader;

import com.cakk.domain.annotation.Reader;
import com.cakk.domain.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;

@Reader
@RequiredArgsConstructor
public class UserReader {
	private final UserJpaRepository userJpaRepository;
}
