package com.cakk.api.service.like;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.DistributedLock;
import com.cakk.api.dto.request.like.HeartCakeSearchRequest;
import com.cakk.api.dto.request.like.HeartCakeShopSearchRequest;
import com.cakk.api.dto.response.like.HeartCakeImageListResponse;
import com.cakk.api.dto.response.like.HeartCakeShopListResponse;
import com.cakk.api.dto.response.like.HeartResponse;
import com.cakk.api.mapper.CakeMapper;
import com.cakk.api.mapper.HeartMapper;
import com.cakk.api.mapper.ShopMapper;
import com.cakk.core.facade.cake.CakeReadFacade;
import com.cakk.core.facade.cake.CakeShopReadFacade;
import com.cakk.core.facade.cake.CakeShopUserReadFacade;
import com.cakk.core.facade.user.UserHeartFacade;
import com.cakk.domain.mysql.dto.param.like.HeartCakeImageResponseParam;
import com.cakk.domain.mysql.dto.param.like.HeartCakeShopResponseParam;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;

@RequiredArgsConstructor
@Service
public class HeartService {

	private final CakeReadFacade cakeReadFacade;
	private final CakeShopReadFacade cakeShopReadFacade;
	private final CakeShopUserReadFacade cakeShopUserReadFacade;
	private final UserHeartFacade userHeartFacade;

	@Transactional(readOnly = true)
	public HeartCakeImageListResponse searchCakeImagesByCursorAndHeart(
		final HeartCakeSearchRequest dto,
		final User signInUser
	) {
		final List<HeartCakeImageResponseParam> cakeImages = cakeShopUserReadFacade.searchCakeImagesByCursorAndHeart(
			dto.cakeHeartId(),
			signInUser.getId(),
			dto.pageSize()
		);

		return CakeMapper.supplyHeartCakeImageListResponseBy(cakeImages);
	}

	@Transactional(readOnly = true)
	public HeartCakeShopListResponse searchCakeShopByCursorAndHeart(
		final HeartCakeShopSearchRequest dto,
		final User signInUser
	) {
		final List<HeartCakeShopResponseParam> cakeShops = cakeShopUserReadFacade.searchAllByCursorAndHeart(
			dto.cakeShopHeartId(),
			signInUser.getId(),
			dto.pageSize()
		);

		return ShopMapper.supplyHeartCakeShopListResponseBy(cakeShops);
	}

	@Transactional(readOnly = true)
	public HeartResponse isHeartCake(final User user, final Long cakeId) {
		final Cake cake = cakeReadFacade.findByIdWithHeart(cakeId);
		final boolean isHeart = cake.isHeartedBy(user);

		return HeartMapper.supplyHeartResponseBy(isHeart);
	}

	@Transactional(readOnly = true)
	public HeartResponse isHeartCakeShop(final User user, final Long cakeShopId) {
		final CakeShop cakeShop = cakeShopReadFacade.findByIdWithHeart(cakeShopId);
		final boolean isHeart = cakeShop.isHeartedBy(user);

		return HeartMapper.supplyHeartResponseBy(isHeart);
	}

	@DistributedLock(key = "#cakeId")
	public void heartCake(final User user, final Long cakeId) {
		final Cake cake = cakeReadFacade.findByIdWithHeart(cakeId);

		userHeartFacade.heartCake(user, cake);
	}

	@DistributedLock(key = "#cakeShopId")
	public void heartCakeShop(final User user, final Long cakeShopId) {
		final CakeShop cakeShop = cakeShopReadFacade.findByIdWithHeart(cakeShopId);

		userHeartFacade.heartCakeShop(user, cakeShop);
	}
}
