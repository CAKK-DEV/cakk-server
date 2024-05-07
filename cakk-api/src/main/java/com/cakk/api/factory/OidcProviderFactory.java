package com.cakk.api.factory;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cakk.api.provider.oauth.OidcProvider;
import com.cakk.api.provider.oauth.impl.AppleAuthProvider;
import com.cakk.api.provider.oauth.impl.GoogleAuthProvider;
import com.cakk.api.provider.oauth.impl.KakaoAuthProvider;
import com.cakk.common.enums.Provider;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;

@Component
public class OidcProviderFactory {

	private final Map<Provider, OidcProvider> authProviderMap;
	private final AppleAuthProvider appleAuthProvider;
	private final KakaoAuthProvider kakaoAuthProvider;
	private final GoogleAuthProvider googleAuthProvider;

	public OidcProviderFactory(AppleAuthProvider appleAuthProvider, KakaoAuthProvider kakaoAuthProvider, GoogleAuthProvider googleAuthProvider) {
		authProviderMap = new EnumMap<>(Provider.class);

		this.appleAuthProvider = appleAuthProvider;
		this.kakaoAuthProvider = kakaoAuthProvider;
		this.googleAuthProvider = googleAuthProvider;

		initialize();
	}

	private void initialize() {
		authProviderMap.put(Provider.APPLE, appleAuthProvider);
		authProviderMap.put(Provider.KAKAO, kakaoAuthProvider);
		authProviderMap.put(Provider.GOOGLE, googleAuthProvider);
	}

	public String getProviderId(Provider provider, String idToken) {
		return getProvider(provider).getProviderId(idToken);
	}

	private OidcProvider getProvider(Provider provider) {
		OidcProvider oidcProvider = authProviderMap.get(provider);

		if (oidcProvider == null) {
			throw new CakkException(ReturnCode.WRONG_PROVIDER);
		}

		return oidcProvider;
	}
}


