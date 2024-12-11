package com.cakk.core.service.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.common.utils.currentTimeMillis
import com.cakk.core.dispatcher.OidcProviderDispatcher
import com.cakk.core.dto.param.user.UserSignInParam
import com.cakk.core.dto.param.user.UserSignUpParam
import com.cakk.core.dto.response.user.JwtResponse
import com.cakk.core.facade.user.UserManageFacade
import com.cakk.core.facade.user.UserReadFacade
import com.cakk.core.mapper.supplyJwtResponseBy
import com.cakk.core.mapper.supplyUserBy
import com.cakk.core.provider.jwt.JwtProvider
import com.cakk.infrastructure.cache.repository.TokenRedisRepository

@Service
class SignService(
    private val userReadFacade: UserReadFacade,
    private val userManageFacade: UserManageFacade,
    private val tokenRedisRepository: TokenRedisRepository,
    private val oidcProviderDispatcher: OidcProviderDispatcher,
    private val jwtProvider: JwtProvider
) {


	@Transactional
	fun signUp(param: UserSignUpParam): JwtResponse {
		val providerId = oidcProviderDispatcher.getProviderId(param.providerType, param.idToken)
		val user = userManageFacade.create(supplyUserBy(param, providerId))

		return supplyJwtResponseBy(jwtProvider.generateToken(user))
	}

	@Transactional(readOnly = true)
	fun signIn(param: UserSignInParam): JwtResponse {
		val providerId = oidcProviderDispatcher.getProviderId(param.providerType, param.idToken)
		val user = userReadFacade.findByProviderId(providerId)

		return supplyJwtResponseBy(jwtProvider.generateToken(user))
	}

	fun signOut(accessToken: String, refreshToken: String) {
		val accessTokenExpiredSecond = jwtProvider.getTokenExpiredSecond(accessToken) - currentTimeMillis()
		val refreshTokenExpiredSecond = jwtProvider.getTokenExpiredSecond(refreshToken) - currentTimeMillis()

		tokenRedisRepository.registerBlackList(accessToken, accessTokenExpiredSecond)
		tokenRedisRepository.registerBlackList(refreshToken, refreshTokenExpiredSecond)
	}

	@Transactional(readOnly = true)
	fun recreateToken(refreshToken: String): JwtResponse {
		val isBlackList = tokenRedisRepository.isBlackListToken(refreshToken)

		if (isBlackList) {
			throw CakkException(ReturnCode.BLACK_LIST_TOKEN)
		}

		val user = jwtProvider.getUser(refreshToken)
		val expiredSecond = jwtProvider.getTokenExpiredSecond(refreshToken) - currentTimeMillis()

		tokenRedisRepository.registerBlackList(refreshToken, expiredSecond)

		return supplyJwtResponseBy(jwtProvider.generateToken(user))
	}
}
