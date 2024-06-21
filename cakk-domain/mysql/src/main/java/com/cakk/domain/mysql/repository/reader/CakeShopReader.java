package com.cakk.domain.mysql.repository.reader;

import static java.util.Objects.*;

import java.util.List;

import org.locationtech.jts.geom.Point;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.dto.param.shop.CakeShopByLocationParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopDetailParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopInfoParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopSearchParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopSimpleParam;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.BusinessInformationJpaRepository;
import com.cakk.domain.mysql.repository.jpa.CakeShopJpaRepository;
import com.cakk.domain.mysql.repository.query.CakeShopQueryRepository;

@Reader
@RequiredArgsConstructor
public class CakeShopReader {

	private final CakeShopJpaRepository cakeShopJpaRepository;
	private final CakeShopQueryRepository cakeShopQueryRepository;
	private final BusinessInformationJpaRepository businessInformationJpaRepository;

	public CakeShop findById(Long cakeShopId) {
		return cakeShopJpaRepository.findById(cakeShopId).orElseThrow(() -> new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP));
	}

	public CakeShopSimpleParam searchSimpleById(final Long cakeShopId) {
		final CakeShopSimpleParam response = cakeShopQueryRepository.searchSimpleById(cakeShopId);

		if (isNull(response)) {
			throw new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP);
		}

		return response;
	}

	public CakeShopDetailParam searchDetailById(final Long cakeShopId) {
		final CakeShopDetailParam response = cakeShopQueryRepository.searchDetailById(cakeShopId);

		if (isNull(response)) {
			throw new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP);
		}

		return response;
	}

	public CakeShopInfoParam searchInfoById(final Long cakeShopId) {
		final CakeShopInfoParam response = cakeShopQueryRepository.searchInfoById(cakeShopId);

		if (isNull(response)) {
			throw new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP);
		}

		return response;
	}

	public BusinessInformation findBusinessInformationWithShop(Long cakeShopId) {
		return businessInformationJpaRepository
			.findBusinessInformationWithCakeShop(cakeShopId)
			.orElseThrow(() -> new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP));
	}

	public BusinessInformation findBusinessInformationByCakeShopId(Long cakeShopId) {
		return businessInformationJpaRepository.findBusinessInformationByCakeShopId(cakeShopId)
			.orElseThrow(() -> new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP));
	}

	public List<CakeShopByLocationParam> searchShopByLocationBased(Point point) {
		return cakeShopQueryRepository.findShopsByLocationBased(point);
	}

	public List<CakeShop> searchShopBySearch(CakeShopSearchParam param) {
		return cakeShopQueryRepository.findByKeywordWithLocation(
			param.cakeShopId(),
			param.keyword(),
			param.location(),
			param.pageSize()
		);
	}

	public CakeShop findWithBusinessInformationAndOwnerById(User owner, Long cakeShopId) {
		return cakeShopQueryRepository.findWithBusinessInformationAndOwnerById(owner, cakeShopId)
			.orElseThrow(() -> new CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER));
	}

	public CakeShop findWithShopLinks(User owner, Long cakeShopId) {
		return cakeShopQueryRepository.findWithShopLinks(owner, cakeShopId)
			.orElseThrow(() -> new CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER));
	}

	public CakeShop findByIdAndOwner(Long cakeShopId, User owner) {
		return cakeShopJpaRepository.findCakeShopByIdAndBusinessInformation_User(cakeShopId, owner)
			.orElseThrow(() -> new CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER));
	}

	public CakeShop findWithOperations(User owner, Long cakeShopId) {
		return cakeShopQueryRepository.findWithOperations(owner, cakeShopId)
			.orElseThrow(() -> new CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER));
	}
}
