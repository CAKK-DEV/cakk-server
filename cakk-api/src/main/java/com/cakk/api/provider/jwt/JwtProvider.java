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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.RequiredArgsConstructor;

import com.cakk.api.vo.JsonWebToken;
import com.cakk.api.vo.OAuthUserDetails;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.entity.user.User;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	private final Key key;

	@Value("${jwt.expiration.access-token}")
	private Long accessTokenExpiredSecond;
	@Value("${jwt.expiration.access-token}")
	private Long refreshTokenExpiredSecond;
	@Value("${jwt.grant-type}")
	private String grantType;
	@Value("${jwt.user-key}")
	private String userKey;

	public JsonWebToken generateToken(final User user) {
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
	}

	public Authentication getAuthentication(String token) {
		final Claims claims = parseClaims(token);

		if (isNull(claims.get(userKey)) || !(claims.get(userKey) instanceof User)) {
			throw new CakkException(EMPTY_AUTH_JWT);
		}

		OAuthUserDetails userDetails = new OAuthUserDetails((User) claims.get(userKey));

		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
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
