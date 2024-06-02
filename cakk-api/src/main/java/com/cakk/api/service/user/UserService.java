package com.cakk.api.service.user;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.user.UserSignInRequest;
import com.cakk.api.dto.request.user.UserSignUpRequest;
import com.cakk.api.dto.response.user.JwtResponse;
import com.cakk.api.factory.OidcProviderFactory;
import com.cakk.api.mapper.UserMapper;
import com.cakk.api.provider.jwt.JwtProvider;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.UserReader;
import com.cakk.domain.mysql.repository.writer.UserWriter;
import com.cakk.domain.redis.repository.impl.TokenRedisRepository;

@Service
@RequiredArgsConstructor
public class UserService {

	private final OidcProviderFactory oidcProviderFactory;
	private final JwtProvider jwtProvider;

	private final UserReader userReader;
	private final UserWriter userWriter;
	private final TokenRedisRepository tokenRedisRepository;

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

	@Transactional(readOnly = true)
	public JwtResponse recreateToken(final String refreshToken) {
		final boolean isBlackList = tokenRedisRepository.isBlackListToken(refreshToken);

		if (isBlackList) {
			throw new CakkException(ReturnCode.BLACK_LIST_TOKEN);
		}

		final User user = jwtProvider.getUser(refreshToken);
		final long expiredSecond = jwtProvider.getRefreshTokenExpiredSecond();

		tokenRedisRepository.save(refreshToken, "refresh", expiredSecond, TimeUnit.MILLISECONDS);

		return JwtResponse.from(jwtProvider.generateToken(user));
	}
}
