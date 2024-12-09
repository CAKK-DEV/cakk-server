package com.cakk.core.provider.jwt

import com.cakk.core.vo.JsonWebToken
import com.cakk.infrastructure.persistence.entity.user.User
import io.jsonwebtoken.Claims
import java.security.PublicKey

interface JwtProvider {

    fun generateToken(user: com.cakk.infrastructure.persistence.entity.user.User): JsonWebToken

    fun getUser(token: String): com.cakk.infrastructure.persistence.entity.user.User

	fun getTokenExpiredSecond(token: String): Long

	fun parseClaims(token: String): Claims

	fun parseClaims(token: String, publicKey: PublicKey): Claims
}
