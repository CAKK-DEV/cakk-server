package com.cakk.api.service.user;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cakk.domain.dto.param.user.CertificationParam;
import com.cakk.domain.entity.shop.CakeShop;
import com.cakk.domain.entity.user.User;
import com.cakk.domain.repository.reader.CakeShopReader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final CakeShopReader cakeShopReader;
	private final ApplicationEventPublisher publisher;


	@Transactional(readOnly = true)
	public void requestCertificationShopKeeper(CertificationParam param) {
		User user = param.user();
		CakeShop cakeShop = param.cakeShopId() != null ? cakeShopReader.findById(param.cakeShopId()) : null;

		user.requestCertificationToApp(param, cakeShop, publisher);
	}
}
