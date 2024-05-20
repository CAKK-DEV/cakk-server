package com.cakk.api.service.shop;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.shop.CreateShopRequest;
import com.cakk.api.dto.request.shop.PromotionRequest;
import com.cakk.api.mapper.ShopMapper;
import com.cakk.domain.dto.param.user.CertificationParam;
import com.cakk.domain.entity.cake.CakeShopOperation;
import com.cakk.domain.entity.shop.CakeShop;
import com.cakk.domain.entity.user.BusinessInformation;
import com.cakk.domain.entity.user.User;
import com.cakk.domain.event.shop.CertificationEvent;
import com.cakk.domain.repository.reader.CakeShopReader;
import com.cakk.domain.repository.reader.UserReader;
import com.cakk.domain.repository.writer.CakeShopWriter;

@Service
@RequiredArgsConstructor
public class ShopService {

	private final CakeShopWriter cakeShopWriter;
	private final UserReader userReader;
	private final CakeShopReader cakeShopReader;
	private final ApplicationEventPublisher publisher;

	@Transactional
	public void createCakeShopByCertification(CreateShopRequest request) {
		CakeShop cakeShop = ShopMapper.supplyCakeShopBy(request);
		BusinessInformation businessInformation = ShopMapper.supplyBusinessInformationBy(request, cakeShop);
		List<CakeShopOperation> cakeShopOperations = ShopMapper.supplyCakeShopOperationsBy(cakeShop, request.startTimes(), request.endTimes());

		cakeShopWriter.createCakeShop(cakeShop, cakeShopOperations, businessInformation);
	}

	@Transactional
	public void promoteUserToBusinessOwner(PromotionRequest request) {
		User user = userReader.findByUserId(request.userId());
		BusinessInformation businessInformation = cakeShopReader.findBusinessInformationByShopId(request.cakeShopId());

		businessInformation.promotedByBusinessOwner(user);
	}

	@Transactional(readOnly = true)
	public void requestCertificationBusinessOwner(CertificationParam param) {
		BusinessInformation businessInformation;

		if (param.cakeShopId() != null) {
			businessInformation = cakeShopReader.findBusinessInformationByShopId(param.cakeShopId());
		} else {
			businessInformation = ShopMapper.supplyBusinessInformationBy();
		}

		CertificationEvent certificationEvent = businessInformation.getRequestCertificationMessage(param);
		publisher.publishEvent(certificationEvent);
	}

}
