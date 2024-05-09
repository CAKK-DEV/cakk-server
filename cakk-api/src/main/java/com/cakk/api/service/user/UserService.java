package com.cakk.api.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cakk.api.dto.request.user.UserSignInRequest;
import com.cakk.api.dto.request.user.UserSignUpRequest;
import com.cakk.api.dto.response.user.JwtResponse;
import com.cakk.api.factory.OidcProviderFactory;
import com.cakk.api.mapper.UserMapper;
import com.cakk.api.provider.jwt.JwtProvider;
import com.cakk.domain.entity.user.User;
import com.cakk.domain.repository.reader.UserReader;
import com.cakk.domain.repository.writer.UserWriter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final OidcProviderFactory oidcProviderFactory;
	private final JwtProvider jwtProvider;

	private final UserReader userReader;
	private final UserWriter userWriter;

	@Transactional
	public JwtResponse signUp(UserSignUpRequest dto) {
		final String providerId = oidcProviderFactory.getProviderId(dto.provider(), dto.idToken());
		final User user = userWriter.create(UserMapper.supplyUserBy(dto, providerId));

		return JwtResponse.from(jwtProvider.generateToken(user));
	}

	@Transactional(readOnly = true)
	public JwtResponse signIn(UserSignInRequest dto) {
		final String providerId = oidcProviderFactory.getProviderId(dto.provider(), dto.idToken());
		final User user = userReader.findByProviderId(providerId);

		return JwtResponse.from(jwtProvider.generateToken(user));
	}
}
