package com.cakk.core.provider.jwt

import com.cakk.core.vo.JsonWebToken
import com.cakk.infrastructure.persistence.entity.user.UserEntity
import io.jsonwebtoken.Claims
import java.security.PublicKey

interface JwtProvider {

    fun generateToken(userEntity: UserEntity): JsonWebToken

    fun getUser(token: String): UserEntity

	fun getTokenExpiredSecond(token: String): Long

	fun parseClaims(token: String): Claims

	fun parseClaims(token: String, publicKey: PublicKey): Claims
}
