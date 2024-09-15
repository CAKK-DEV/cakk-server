package com.cakk.api.service.user;

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
import com.cakk.core.facade.user.UserManageFacade;
import com.cakk.core.facade.user.UserReadFacade;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.redis.repository.TokenRedisRepository;

@Service
@RequiredArgsConstructor
public class SignService {

	private final OidcProviderFactory oidcProviderFactory;
	private final JwtProvider jwtProvider;

	private final UserReadFacade userReadFacade;
	private final UserManageFacade userManageFacade;
	private final TokenRedisRepository tokenRedisRepository;

	@Transactional
	public JwtResponse signUp(final UserSignUpRequest dto) {
		final String providerId = oidcProviderFactory.getProviderId(dto.provider(), dto.idToken());
		final User user = userManageFacade.create(UserMapper.supplyUserBy(dto, providerId));

		return JwtResponse.from(jwtProvider.generateToken(user));
	}

	@Transactional(readOnly = true)
	public JwtResponse signIn(final UserSignInRequest dto) {
		final String providerId = oidcProviderFactory.getProviderId(dto.provider(), dto.idToken());
		final User user = userReadFacade.findByProviderId(providerId);

		return JwtResponse.from(jwtProvider.generateToken(user));
	}

	public void signOut(final String accessToken, final String refreshToken) {
		final long accessTokenExpiredSecond = jwtProvider.getTokenExpiredSecond(accessToken) - System.currentTimeMillis();
		final long refreshTokenExpiredSecond = jwtProvider.getTokenExpiredSecond(refreshToken) - System.currentTimeMillis();

		tokenRedisRepository.registerBlackList(accessToken, accessTokenExpiredSecond);
		tokenRedisRepository.registerBlackList(refreshToken, refreshTokenExpiredSecond);
	}

	@Transactional(readOnly = true)
	public JwtResponse recreateToken(final String refreshToken) {
		final boolean isBlackList = tokenRedisRepository.isBlackListToken(refreshToken);

		if (isBlackList) {
			throw new CakkException(ReturnCode.BLACK_LIST_TOKEN);
		}

		final User user = jwtProvider.getUser(refreshToken);
		final long expiredSecond = jwtProvider.getTokenExpiredSecond(refreshToken) - System.currentTimeMillis();

		tokenRedisRepository.registerBlackList(refreshToken, expiredSecond);

		return JwtResponse.from(jwtProvider.generateToken(user));
	}
}
