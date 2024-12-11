package com.cakk.api.provider.jwt

import java.security.Key
import java.security.PublicKey
import java.util.*

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.InvalidKeyException

import com.fasterxml.jackson.databind.ObjectMapper

import com.cakk.core.vo.JsonWebToken
import com.cakk.api.vo.OAuthUserDetails
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.provider.jwt.JwtProvider
import com.cakk.infrastructure.cache.repository.TokenRedisRepository
import com.cakk.infrastructure.persistence.entity.user.UserEntity

@Component
class JwtProviderImpl(
	private val key: Key,
	private val tokenRedisRepository: TokenRedisRepository,
	@Value("\${jwt.expiration.access-token}")
	private val accessTokenExpiredSecond: Long,
	@Value("\${jwt.expiration.refresh-token}")
	private val refreshTokenExpiredSecond: Long,
	@Value("\${jwt.grant-type}")
	private val grantType: String,
	@Value("\${jwt.user-key}")
	private val userKey: String
): JwtProvider {

	override fun generateToken(userEntity: UserEntity): JsonWebToken {
		try {
			val accessToken = Jwts.builder()
				.claim(userKey, userEntity)
				.setExpiration(Date(System.currentTimeMillis() + accessTokenExpiredSecond))
				.signWith(key, SignatureAlgorithm.HS512)
				.compact()
			val refreshToken = Jwts.builder()
				.claim(userKey, userEntity)
				.setExpiration(Date(System.currentTimeMillis() + refreshTokenExpiredSecond))
				.signWith(key, SignatureAlgorithm.HS512)
				.compact()

			return JsonWebToken(
				accessToken = accessToken,
				refreshToken = refreshToken,
				grantType = grantType
			)
		} catch (e: InvalidKeyException) {
			throw CakkException(ReturnCode.INVALID_KEY)
		}
	}

	fun getAuthentication(token: String): Authentication {
		val user = getUser(token)
		val userDetails = OAuthUserDetails(user)

		return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
	}

	override fun getUser(token: String): UserEntity {
		val parseUser = parseClaims(token)[userKey] ?: throw CakkException(ReturnCode.EMPTY_AUTH_JWT)

		return ObjectMapper().convertValue(parseUser, UserEntity::class.java)
	}

	override fun getTokenExpiredSecond(token: String): Long {
		val claims = parseClaims(token)
		return claims.expiration.time
	}

	override fun parseClaims(token: String): Claims {
		val isBlackList = tokenRedisRepository.isBlackListToken(token)

		if (isBlackList) {
			throw CakkException(ReturnCode.BLACK_LIST_TOKEN)
		}

		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.body
		} catch (e: ExpiredJwtException) {
			throw CakkException(ReturnCode.EXPIRED_JWT_TOKEN)
		} catch (e: RuntimeException) {
			throw CakkException(ReturnCode.WRONG_JWT_TOKEN)
		}
	}

	override fun parseClaims(token: String, publicKey: PublicKey): Claims {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(publicKey)
				.build()
				.parseClaimsJws(token)
				.body
		} catch (e: ExpiredJwtException) {
			throw CakkException(ReturnCode.EXPIRED_JWT_TOKEN)
		} catch (e: RuntimeException) {
			throw CakkException(ReturnCode.WRONG_JWT_TOKEN)
		}
	}
}
