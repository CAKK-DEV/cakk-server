package com.cakk.api.service.shop;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cakk.api.dto.request.shop.CreateShopRequest;
import com.cakk.api.dto.request.shop.PromotionRequest;
import com.cakk.domain.entity.shop.CakeShop;
import com.cakk.domain.entity.user.BusinessInformation;
import com.cakk.domain.entity.user.User;
import com.cakk.domain.mapper.ShopMapper;
import com.cakk.domain.repository.reader.CakeShopReader;
import com.cakk.domain.repository.reader.UserReader;
import com.cakk.domain.repository.writer.CakeShopWriter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopService {

	private final CakeShopWriter cakeShopWriter;
	private final UserReader userReader;
	private final CakeShopReader cakeShopReader;

	@Transactional
	public void createCakeShopByCertification(CreateShopRequest request) {
		CakeShop cakeShop = ShopMapper.supplyCakeShopBy(request);
		BusinessInformation businessInformation = ShopMapper.supplyBusinessInformationBy(request, cakeShop);

		cakeShopWriter.createCakeShop(cakeShop, businessInformation);
	}

	@Transactional
	public void promoteUserToShopKeeper(PromotionRequest request) {
		User user = userReader.findByUserId(request.userId());
		BusinessInformation businessInformation = cakeShopReader.findBusinessInformationByShopId(request.cakeShopId());

		businessInformation.promotedByShopKeeper(user);
	}
}
