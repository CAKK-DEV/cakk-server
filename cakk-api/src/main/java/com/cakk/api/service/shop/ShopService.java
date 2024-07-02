package com.cakk.api.service.shop;

import static java.util.Objects.*;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.shop.CakeShopSearchByViewsRequest;
import com.cakk.api.dto.request.shop.CakeShopSearchRequest;
import com.cakk.api.dto.request.shop.CreateShopRequest;
import com.cakk.api.dto.request.shop.PromotionRequest;
import com.cakk.api.dto.request.shop.SearchShopByLocationRequest;
import com.cakk.api.dto.response.shop.CakeShopByMapResponse;
import com.cakk.api.dto.response.shop.CakeShopByMineResponse;
import com.cakk.api.dto.response.shop.CakeShopCreateResponse;
import com.cakk.api.dto.response.shop.CakeShopDetailResponse;
import com.cakk.api.dto.response.shop.CakeShopInfoResponse;
import com.cakk.api.dto.response.shop.CakeShopOwnerResponse;
import com.cakk.api.dto.response.shop.CakeShopSearchResponse;
import com.cakk.api.dto.response.shop.CakeShopSimpleResponse;
import com.cakk.api.mapper.LinkMapper;
import com.cakk.api.mapper.PointMapper;
import com.cakk.api.mapper.ShopMapper;
import com.cakk.domain.mysql.bo.CakeShops;
import com.cakk.domain.mysql.dto.param.link.UpdateLinkParam;
import com.cakk.domain.mysql.dto.param.operation.UpdateShopOperationParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopByLocationParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopBySearchParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopDetailParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopInfoParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopSimpleParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopUpdateParam;
import com.cakk.domain.mysql.dto.param.shop.UpdateShopAddressParam;
import com.cakk.domain.mysql.dto.param.user.CertificationParam;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopLink;
import com.cakk.domain.mysql.entity.shop.CakeShopOperation;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.event.EventMapper;
import com.cakk.domain.mysql.event.shop.CertificationEvent;
import com.cakk.domain.mysql.event.views.CakeShopIncreaseViewsEvent;
import com.cakk.domain.mysql.repository.reader.BusinessInformationReader;
import com.cakk.domain.mysql.repository.reader.CakeShopReader;
import com.cakk.domain.mysql.repository.reader.UserReader;
import com.cakk.domain.mysql.repository.writer.CakeShopWriter;
import com.cakk.domain.redis.repository.CakeShopViewsRedisRepository;

@Service
@RequiredArgsConstructor
public class ShopService {

	private final UserReader userReader;
	private final CakeShopReader cakeShopReader;
	private final BusinessInformationReader businessInformationReader;
	private final CakeShopWriter cakeShopWriter;
	private final CakeShopViewsRedisRepository cakeShopViewsRedisRepository;

	private final ApplicationEventPublisher publisher;

	@Transactional
	public CakeShopCreateResponse createCakeShopByCertification(final CreateShopRequest request) {
		final CakeShop cakeShop = ShopMapper.supplyCakeShopBy(request);
		final BusinessInformation businessInformation = ShopMapper.supplyBusinessInformationBy(request, cakeShop);
		final List<CakeShopOperation> cakeShopOperations = ShopMapper.supplyCakeShopOperationsBy(cakeShop, request.operationDays());
		final List<CakeShopLink> cakeShopLinks = LinkMapper.supplyCakeShopLinksBy(cakeShop, request.links());

		final CakeShop result = cakeShopWriter.createCakeShop(cakeShop, cakeShopOperations, businessInformation, cakeShopLinks);

		return ShopMapper.supplyCakeShopCreateResponseBy(result);
	}

	@Transactional
	public void promoteUserToBusinessOwner(PromotionRequest request) {
		User user = userReader.findByUserId(request.userId());
		BusinessInformation businessInformation = cakeShopReader
			.findBusinessInformationWithShop(request.cakeShopId());

		businessInformation.promotedByBusinessOwner(user);
	}

	@Transactional
	public void updateDefaultInformation(CakeShopUpdateParam param) {
		final CakeShop cakeShop = cakeShopReader.findWithBusinessInformationAndOwnerById(param.user(), param.cakeShopId());

		cakeShop.updateDefaultInformation(param);
	}

	@Transactional
	public void updateShopLinks(UpdateLinkParam param) {
		final CakeShop cakeShop = cakeShopReader.findWithShopLinks(param.user(), param.cakeShopId());
		cakeShop.updateShopLinks(param.cakeShopLinks());
	}

	@Transactional
	public void updateShopAddress(UpdateShopAddressParam param) {
		final CakeShop cakeShop = cakeShopReader.findWithBusinessInformationAndOwnerById(param.user(), param.cakeShopId());
		cakeShop.updateShopAddress(param);
	}

	@Transactional
	public void updateShopOperationDays(UpdateShopOperationParam param) {
		final CakeShop cakeShop = cakeShopReader.findWithOperations(param.user(), param.cakeShopId());
		cakeShop.updateShopOperationDays(param.cakeShopOperations());
	}

	@Transactional(readOnly = true)
	public CakeShopByMineResponse getMyBusinessId(User user) {
		final List<BusinessInformation> result = businessInformationReader.findAllWithCakeShopByUser(user);
		return ShopMapper.supplyCakeShopByMineResponseBy(result);
	}

	@Transactional(readOnly = true)
	public void requestCertificationBusinessOwner(CertificationParam param) {
		BusinessInformation businessInformation;

		if (param.cakeShopId() != null) {
			businessInformation = cakeShopReader.findBusinessInformationByCakeShopId(param.cakeShopId());
		} else {
			businessInformation = ShopMapper.supplyBusinessInformationBy();
		}

		CertificationEvent certificationEvent = businessInformation.getRequestCertificationMessage(param);
		publisher.publishEvent(certificationEvent);
	}

	@Transactional(readOnly = true)
	public CakeShopSimpleResponse searchSimpleById(final Long cakeShopId) {
		final CakeShopSimpleParam cakeShop = cakeShopReader.searchSimpleById(cakeShopId);

		return ShopMapper.cakeShopSimpleResponseFromParam(cakeShop);
	}

	@Transactional(readOnly = true)
	public CakeShopDetailResponse searchDetailById(final Long cakeShopId) {
		final CakeShopDetailParam cakeShop = cakeShopReader.searchDetailById(cakeShopId);
		final CakeShopIncreaseViewsEvent event = EventMapper.supplyCakeShopIncreaseViewsEvent(cakeShopId);

		publisher.publishEvent(event);
		return ShopMapper.cakeShopDetailResponseFromParam(cakeShop);
	}

	@Transactional(readOnly = true)
	public CakeShopInfoResponse searchInfoById(final Long cakeShopId) {
		final CakeShopInfoParam cakeShopInfo = cakeShopReader.searchInfoById(cakeShopId);

		return ShopMapper.supplyCakeShopInfoResponseBy(cakeShopInfo);
	}

	@Transactional(readOnly = true)
	public CakeShopByMapResponse searchShop(final SearchShopByLocationRequest request) {
		Double longitude = request.longitude();
		Double latitude = request.latitude();
		final List<CakeShopByLocationParam> result = cakeShopReader
			.searchShopByLocationBased(PointMapper.supplyPointBy(latitude, longitude));

		final CakeShops<CakeShopByLocationParam> cakeShops = new CakeShops<>(result, 4);

		return ShopMapper.supplyCakeShopByMapResponseBy(cakeShops.getCakeShops());
	}

	@Transactional(readOnly = true)
	public CakeShopSearchResponse searchShopByKeyword(CakeShopSearchRequest dto) {
		Integer pageSize = dto.pageSize();
		final List<CakeShop> result = cakeShopReader.searchShopBySearch(dto.toParam());
		final List<CakeShopBySearchParam> cakeShopBySearchParams = ShopMapper.supplyCakeShopBySearchParamListBy(result);

		final CakeShops<CakeShopBySearchParam> cakeShops = new CakeShops<>(cakeShopBySearchParams, 4, pageSize);

		return ShopMapper.supplyCakeShopSearchResponseBy(cakeShops.getCakeShops());
	}

	@Transactional(readOnly = true)
	public CakeShopSearchResponse searchCakeShopsByCursorAndViews(CakeShopSearchByViewsRequest dto) {
		final long offset = isNull(dto.offset()) ? 0 : dto.offset();
		final int pageSize = dto.pageSize();
		final List<Long> cakeShopIds = cakeShopViewsRedisRepository.findTopShopIdsByOffsetAndCount(offset, pageSize);

		if (isNull(cakeShopIds) || cakeShopIds.isEmpty()) {
			return ShopMapper.supplyCakeShopSearchResponseBy(List.of());
		}

		final List<CakeShop> result = cakeShopReader.searchShopsByShopIds(cakeShopIds);
		final List<CakeShopBySearchParam> cakeShopBySearchParams = ShopMapper.supplyCakeShopBySearchParamListBy(result);
		final CakeShops<CakeShopBySearchParam> cakeShops = new CakeShops<>(cakeShopBySearchParams, 6, pageSize);

		return ShopMapper.supplyCakeShopSearchResponseBy(cakeShops.getCakeShops());
	}

	@Transactional(readOnly = true)
	public CakeShopOwnerResponse isExistBusinessInformation(User owner, Long cakeShopId) {
		final Boolean isOwned = businessInformationReader.isExistBusinessInformation(owner, cakeShopId);

		return ShopMapper.supplyCakeShopOwnerResponseBy(isOwned);
	}
}
