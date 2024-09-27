package com.cakk.core.provider.jwt

import com.cakk.core.vo.JsonWebToken
import com.cakk.domain.mysql.entity.user.User
import io.jsonwebtoken.Claims
import java.security.PublicKey

interface JwtProvider {

    fun generateToken(user: User): JsonWebToken

    fun getUser(token: String): User

	fun getTokenExpiredSecond(token: String): Long

	fun parseClaims(token: String): Claims

	fun parseClaims(token: String, publicKey: PublicKey): Claims
}
