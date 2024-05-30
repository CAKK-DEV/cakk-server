package com.cakk.api.provider.jwt;

import static com.cakk.common.enums.ReturnCode.*;
import static java.util.Objects.*;

import java.security.Key;
import java.security.PublicKey;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.InvalidKeyException;

import com.cakk.api.vo.JsonWebToken;
import com.cakk.api.vo.OAuthUserDetails;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.entity.user.User;

@Component
public class JwtProvider {

	private final Key key;

	private final Long accessTokenExpiredSecond;
	private final Long refreshTokenExpiredSecond;
	private final String grantType;
	private final String userKey;

	public JwtProvider(
		Key key,
		@Value("${jwt.expiration.access-token}")
		Long accessTokenExpiredSecond,
		@Value("${jwt.expiration.refresh-token}")
		Long refreshTokenExpiredSecond,
		@Value("${jwt.grant-type}")
		String grantType,
		@Value("${jwt.user-key}")
		String userKey
	) {
		this.key = key;
		this.accessTokenExpiredSecond = accessTokenExpiredSecond;
		this.refreshTokenExpiredSecond = refreshTokenExpiredSecond;
		this.grantType = grantType;
		this.userKey = userKey;
	}

	public JsonWebToken generateToken(final User user) {
		if (isNull(user)) {
			throw new CakkException(EMPTY_USER);
		}

		try {
			final String accessToken = Jwts.builder()
				.claim(userKey, user)
				.setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiredSecond))
				.signWith(key, SignatureAlgorithm.HS512)
				.compact();
			final String refreshToken = Jwts.builder()
				.setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiredSecond))
				.signWith(key, SignatureAlgorithm.HS512)
				.compact();

			return JsonWebToken.builder()
				.grantType(grantType)
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
		} catch (InvalidKeyException e) {
			throw new CakkException(INVALID_KEY);
		}
	}

	public Authentication getAuthentication(String token) {
		final User user = getUser(token);
		final OAuthUserDetails userDetails = new OAuthUserDetails(user);

		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public User getUser(String token) {
		final Claims claims = parseClaims(token);

		if (isNull(claims.get(userKey))) {
			throw new CakkException(EMPTY_AUTH_JWT);
		}

		return new ObjectMapper().convertValue(claims.get(userKey), User.class);
	}

	public long getRefreshTokenExpiredSecond() {
		return this.refreshTokenExpiredSecond;
	}

	public Claims parseClaims(String token) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			throw new CakkException(EXPIRED_JWT_TOKEN);
		} catch (RuntimeException e) {
			throw new CakkException(WRONG_JWT_TOKEN);
		}
	}

	public Claims parseClaims(String token, PublicKey publicKey) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(publicKey)
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (ExpiredJwtException e) {
			throw new CakkException(EXPIRED_JWT_TOKEN);
		} catch (RuntimeException e) {
			throw new CakkException(WRONG_JWT_TOKEN);
		}
	}
}
