package com.cakk.api.service.shop;

import static java.util.Objects.*;

import java.util.List;
import java.util.Objects;

import org.locationtech.jts.geom.Point;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.event.IncreaseSearchCountEvent;
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
import com.cakk.api.dto.response.shop.CakeShopOwnerCandidateResponse;
import com.cakk.api.dto.response.shop.CakeShopOwnerCandidatesResponse;
import com.cakk.api.dto.response.shop.CakeShopOwnerResponse;
import com.cakk.api.dto.response.shop.CakeShopSearchResponse;
import com.cakk.api.dto.response.shop.CakeShopSimpleResponse;
import com.cakk.api.mapper.BusinessInformationMapper;
import com.cakk.api.mapper.LinkMapper;
import com.cakk.api.mapper.PointMapper;
import com.cakk.api.mapper.ShopMapper;
import com.cakk.core.facade.cake.BusinessInformationReadFacade;
import com.cakk.core.facade.cake.CakeShopReadFacade;
import com.cakk.core.facade.shop.CakeShopManageFacade;
import com.cakk.core.facade.user.UserReadFacade;
import com.cakk.domain.mysql.bo.shop.CakeShopByLocationParam;
import com.cakk.domain.mysql.bo.shop.CakeShopBySearchParam;
import com.cakk.domain.mysql.bo.shop.CakeShops;
import com.cakk.domain.mysql.bo.user.VerificationPolicy;
import com.cakk.domain.mysql.dto.param.link.UpdateLinkParam;
import com.cakk.domain.mysql.dto.param.operation.UpdateShopOperationParam;
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
import com.cakk.domain.mysql.event.shop.CertificationEvent;
import com.cakk.domain.mysql.event.views.CakeShopIncreaseViewsEvent;
import com.cakk.domain.mysql.mapper.EventMapper;
import com.cakk.domain.redis.repository.CakeShopViewsRedisRepository;

@Service
@RequiredArgsConstructor
public class ShopService {

	private final UserReadFacade userReadFacade;
	private final CakeShopReadFacade cakeShopReadFacade;
	private final BusinessInformationReadFacade businessInformationReadFacade;
	private final CakeShopManageFacade cakeShopManageFacade;
	private final CakeShopViewsRedisRepository cakeShopViewsRedisRepository;

	private final VerificationPolicy verificationPolicy;
	private final ApplicationEventPublisher publisher;

	@Transactional
	public CakeShopCreateResponse createCakeShopByCertification(final CreateShopRequest request) {
		final CakeShop cakeShop = ShopMapper.supplyCakeShopBy(request);
		final BusinessInformation businessInformation = ShopMapper.supplyBusinessInformationBy(request, cakeShop);
		final List<CakeShopOperation> cakeShopOperations = ShopMapper.supplyCakeShopOperationsBy(cakeShop, request.operationDays());
		final List<CakeShopLink> cakeShopLinks = LinkMapper.supplyCakeShopLinksBy(cakeShop, request.links());

		final CakeShop result = cakeShopManageFacade.create(cakeShop, cakeShopOperations, businessInformation, cakeShopLinks);

		return ShopMapper.supplyCakeShopCreateResponseBy(result);
	}

	@Transactional
	public void promoteUserToBusinessOwner(final PromotionRequest request) {
		final User user = userReadFacade.findByUserId(request.userId());
		final BusinessInformation businessInformation = cakeShopReadFacade.findBusinessInformationWithShop(request.cakeShopId());

		businessInformation.updateBusinessOwner(verificationPolicy, user);
	}

	@Transactional
	public void updateBasicInformation(final CakeShopUpdateParam param) {
		final CakeShop cakeShop = cakeShopReadFacade.searchByIdAndOwner(param.cakeShopId(), param.user());

		cakeShop.updateBasicInformation(param);
	}

	@Transactional
	public void updateShopLinks(final UpdateLinkParam param) {
		final CakeShop cakeShop = cakeShopReadFacade.searchWithShopLinks(param.user(), param.cakeShopId());
		cakeShop.updateShopLinks(param.cakeShopLinks());
	}

	@Transactional
	public void updateShopAddress(final UpdateShopAddressParam param) {
		final CakeShop cakeShop = cakeShopReadFacade.searchByIdAndOwner(param.cakeShopId(), param.user());
		cakeShop.updateShopAddress(param);
	}

	@Transactional
	public void updateShopOperationDays(final UpdateShopOperationParam param) {
		final CakeShop cakeShop = cakeShopReadFacade.searchWithOperations(param.user(), param.cakeShopId());
		cakeShop.updateShopOperationDays(param.cakeShopOperations());
	}

	@Transactional(readOnly = true)
	public CakeShopByMineResponse getMyBusinessId(final User user) {
		final List<BusinessInformation> result = businessInformationReadFacade.findAllWithCakeShopByUser(user);
		return ShopMapper.supplyCakeShopByMineResponseBy(result);
	}

	@Transactional
	public void requestCertificationBusinessOwner(final CertificationParam param) {
		final BusinessInformation businessInformation = cakeShopReadFacade.findBusinessInformationByCakeShopId(
			param.cakeShopId());
		final CertificationEvent certificationEvent = verificationPolicy
			.requestCertificationBusinessOwner(businessInformation, param);

		publisher.publishEvent(certificationEvent);
	}

	@Transactional(readOnly = true)
	public CakeShopSimpleResponse searchSimpleById(final Long cakeShopId) {
		final CakeShopSimpleParam cakeShop = cakeShopReadFacade.searchSimpleById(cakeShopId);

		return ShopMapper.cakeShopSimpleResponseFromParam(cakeShop);
	}

	@Transactional(readOnly = true)
	public CakeShopDetailResponse searchDetailById(final Long cakeShopId) {
		final CakeShopDetailParam cakeShop = cakeShopReadFacade.searchDetailById(cakeShopId);
		final CakeShopIncreaseViewsEvent event = EventMapper.supplyCakeShopIncreaseViewsEvent(cakeShopId);

		publisher.publishEvent(event);
		return ShopMapper.cakeShopDetailResponseFromParam(cakeShop);
	}

	@Transactional(readOnly = true)
	public CakeShopInfoResponse searchInfoById(final Long cakeShopId) {
		final CakeShopInfoParam cakeShopInfo = cakeShopReadFacade.searchInfoById(cakeShopId);

		return ShopMapper.supplyCakeShopInfoResponseBy(cakeShopInfo);
	}

	@Transactional(readOnly = true)
	public CakeShopByMapResponse searchShop(final SearchShopByLocationRequest request) {
		final Double longitude = request.longitude();
		final Double latitude = request.latitude();
		final Double distance = request.distance();
		final Point point = PointMapper.supplyPointBy(latitude, longitude);

		final List<CakeShopByLocationParam> result = cakeShopReadFacade
			.searchShopByLocationBased(point, Objects.requireNonNullElse(distance, 1000.0));
		final CakeShops<CakeShopByLocationParam> cakeShops = new CakeShops<>(result, 4);

		return ShopMapper.supplyCakeShopByMapResponseBy(cakeShops.getCakeShops());
	}

	@Transactional(readOnly = true)
	public CakeShopSearchResponse searchShopByKeyword(final CakeShopSearchRequest dto) {
		final int pageSize = dto.pageSize();
		final List<CakeShop> result = cakeShopReadFacade.searchShopBySearch(dto.toParam());
		final List<CakeShopBySearchParam> cakeShopBySearchParams = ShopMapper.supplyCakeShopBySearchParamListBy(result);

		final CakeShops<CakeShopBySearchParam> cakeShops = new CakeShops<>(cakeShopBySearchParams, 4, pageSize);

		if (nonNull(dto.keyword())) {
			final IncreaseSearchCountEvent event = com.cakk.api.mapper.EventMapper.supplyIncreaseSearchCountEventBy(dto.keyword());
			publisher.publishEvent(event);
		}

		return ShopMapper.supplyCakeShopSearchResponseBy(cakeShops.getCakeShops());
	}

	@Transactional(readOnly = true)
	public CakeShopSearchResponse searchCakeShopsByCursorAndViews(final CakeShopSearchByViewsRequest dto) {
		final long offset = isNull(dto.offset()) ? 0 : dto.offset();
		final int pageSize = dto.pageSize();
		final List<Long> cakeShopIds = cakeShopViewsRedisRepository.findTopShopIdsByOffsetAndCount(offset, pageSize);

		if (isNull(cakeShopIds) || cakeShopIds.isEmpty()) {
			return ShopMapper.supplyCakeShopSearchResponseBy(List.of());
		}

		final List<CakeShop> result = cakeShopReadFacade.searchShopsByShopIds(cakeShopIds);
		final List<CakeShopBySearchParam> cakeShopBySearchParams = ShopMapper.supplyCakeShopBySearchParamListBy(result);
		final CakeShops<CakeShopBySearchParam> cakeShops = new CakeShops<>(cakeShopBySearchParams, 6, pageSize);

		return ShopMapper.supplyCakeShopSearchResponseBy(cakeShops.getCakeShops());
	}

	@Transactional(readOnly = true)
	public CakeShopOwnerResponse isExistBusinessInformation(final User owner, final Long cakeShopId) {
		final Boolean isOwned = businessInformationReadFacade.isExistBusinessInformation(owner, cakeShopId);

		return ShopMapper.supplyCakeShopOwnerResponseBy(isOwned);
	}

	@Transactional(readOnly = true)
	public CakeShopOwnerCandidatesResponse getBusinessOwnerCandidates() {
		List<BusinessInformation> businessInformations = businessInformationReadFacade.findAllCakeShopBusinessOwnerCandidates();

		businessInformations = businessInformations
			.stream()
			.filter(businessInformation -> businessInformation.isBusinessOwnerCandidate(verificationPolicy))
			.toList();

		return BusinessInformationMapper.supplyCakeShopOwnerCandidatesResponseBy(businessInformations);
	}

	@Transactional(readOnly = true)
	public CakeShopOwnerCandidateResponse getCandidateInformation(final Long userId) {
		BusinessInformation businessInformation = businessInformationReadFacade.findByUserId(userId);

		return BusinessInformationMapper.supplyCakeShopOwnerCandidateResponseBy(businessInformation);
	}
}
