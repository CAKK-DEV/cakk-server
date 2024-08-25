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
import com.cakk.domain.mysql.dto.param.like.HeartCakeImageResponseParam;
import com.cakk.domain.mysql.dto.param.like.HeartCakeShopResponseParam;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopHeart;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.CakeHeartReader;
import com.cakk.domain.mysql.repository.reader.CakeReader;
import com.cakk.domain.mysql.repository.reader.CakeShopHeartReader;
import com.cakk.domain.mysql.repository.reader.CakeShopReader;
import com.cakk.domain.mysql.repository.writer.CakeShopHeartWriter;

@RequiredArgsConstructor
@Service
public class HeartService {

	private final CakeReader cakeReader;
	private final CakeShopReader cakeShopReader;
	private final CakeHeartReader cakeHeartReader;
	private final CakeShopHeartReader cakeShopHeartReader;
	private final CakeShopHeartWriter cakeShopHeartWriter;

	@Transactional(readOnly = true)
	public HeartCakeImageListResponse searchCakeImagesByCursorAndHeart(
		final HeartCakeSearchRequest dto,
		final User signInUser
	) {
		final List<HeartCakeImageResponseParam> cakeImages = cakeHeartReader.searchCakeImagesByCursorAndHeart(
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
		final List<HeartCakeShopResponseParam> cakeShops = cakeShopHeartReader.searchAllByCursorAndHeart(
			dto.cakeShopHeartId(),
			signInUser.getId(),
			dto.pageSize()
		);

		return ShopMapper.supplyHeartCakeShopListResponseBy(cakeShops);
	}

	@Transactional(readOnly = true)
	public HeartResponse isHeartCake(final User user, final Long cakeId) {
		final Cake cake = cakeReader.findByIdWithHeart(cakeId);
		final boolean isHeart = cake.isHeartedBy(user);

		return HeartMapper.supplyHeartResponseBy(isHeart);
	}

	@Transactional(readOnly = true)
	public HeartResponse isHeartCakeShop(final User user, final Long cakeShopId) {
		final CakeShop cakeShop = cakeShopReader.findById(cakeShopId);
		final boolean isHeart = cakeShopHeartReader.existsByUserAndCakeShop(user, cakeShop);

		return HeartMapper.supplyHeartResponseBy(isHeart);
	}

	@DistributedLock(key = "#cakeId")
	public void heartCake(final User user, final Long cakeId) {
		final Cake cake = cakeReader.findById(cakeId);

		if (!cake.isHeartedBy(user)) {
			user.heartCake(cake);
		} else {
			user.unHeartCake(cake);
		}
	}

	@DistributedLock(key = "#cakeShopId")
	public void heartCakeShop(final User user, final Long cakeShopId) {
		final CakeShop cakeShop = cakeShopReader.findById(cakeShopId);
		final CakeShopHeart cakeShopHeart = cakeShopHeartReader.findOrNullByUserAndCakeShop(user, cakeShop);

		cakeShopHeartWriter.heartOrCancel(cakeShopHeart, user, cakeShop);
	}
}
