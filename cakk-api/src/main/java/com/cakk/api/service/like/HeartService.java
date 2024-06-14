package com.cakk.api.service.like;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.like.HeartCakeSearchRequest;
import com.cakk.api.dto.response.like.HeartCakeImageListResponse;
import com.cakk.api.mapper.CakeMapper;
import com.cakk.common.enums.RedisKey;
import com.cakk.domain.mysql.dto.param.like.HeartCakeImageResponseParam;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.cake.CakeHeart;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopHeart;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.CakeHeartReader;
import com.cakk.domain.mysql.repository.reader.CakeReader;
import com.cakk.domain.mysql.repository.reader.CakeShopHeartReader;
import com.cakk.domain.mysql.repository.reader.CakeShopReader;
import com.cakk.domain.mysql.repository.writer.CakeHeartWriter;
import com.cakk.domain.mysql.repository.writer.CakeShopHeartWriter;
import com.cakk.domain.redis.repository.LockRedisRepository;

@RequiredArgsConstructor
@Service
public class HeartService {

	private final CakeReader cakeReader;
	private final CakeShopReader cakeShopReader;
	private final CakeHeartReader cakeHeartReader;
	private final CakeHeartWriter cakeHeartWriter;
	private final CakeShopHeartReader cakeShopHeartReader;
	private final CakeShopHeartWriter cakeShopHeartWriter;

	private final LockRedisRepository lockRedisRepository;

	@Transactional(readOnly = true)
	public HeartCakeImageListResponse findCakeImagesByCursorAndHeart(final HeartCakeSearchRequest dto,
																	final User signInUser) {
		final List<HeartCakeImageResponseParam> cakeImages = cakeHeartReader.searchCakeImagesByCursorAndHeart(
			dto.cakeHeartId(),
			signInUser.getId(),
			dto.pageSize()
		);

		return CakeMapper.supplyHeartCakeImageListResponseBy(cakeImages);
	}

	@Transactional
	public void heartCakeWithLock(final User user, final Long cakeId) {
		lockRedisRepository.executeWithLock(RedisKey.LOCK_CAKE_HEART, 100L,
			() -> heartCake(user, cakeId)
		);
	}

	@Transactional
	public void heartCakeShopWithLock(final User user, final Long cakeShopId) {
		lockRedisRepository.executeWithLock(RedisKey.LOCK_SHOP_HEART, 500L,
			() -> heartCakeShop(user, cakeShopId)
		);
	}

	@Transactional
	public void heartCake(final User user, final Long cakeId) {
		final Cake cake = cakeReader.findById(cakeId);
		final CakeHeart cakeHeart = cakeHeartReader.findOrNullByUserAndCake(user, cake);

		cakeHeartWriter.heartOrCancel(cakeHeart, user, cake);
	}

	@Transactional
	public void heartCakeShop(final User user, final Long cakeShopId) {
		final CakeShop cakeShop = cakeShopReader.findById(cakeShopId);
		final CakeShopHeart cakeShopHeart = cakeShopHeartReader.findOrNullByUserAndCakeShop(user, cakeShop);

		cakeShopHeartWriter.heartOrCancel(cakeShopHeart, user, cakeShop);
	}
}