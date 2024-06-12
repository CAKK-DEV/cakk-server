package com.cakk.api.service.like;

import static java.util.Objects.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.like.LikeCakeSearchRequest;
import com.cakk.api.dto.response.like.LikeCakeImageListResponse;
import com.cakk.api.mapper.CakeMapper;
import com.cakk.common.enums.RedisKey;
import com.cakk.domain.mysql.dto.param.like.LikeCakeImageResponseParam;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.cake.CakeLike;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.CakeLikeReader;
import com.cakk.domain.mysql.repository.reader.CakeReader;
import com.cakk.domain.mysql.repository.writer.CakeLikeWriter;
import com.cakk.domain.redis.repository.LockRedisRepository;

@RequiredArgsConstructor
@Service
public class LikeService {

	private final CakeReader cakeReader;
	private final CakeLikeReader cakeLikeReader;
	private final CakeLikeWriter cakeLikeWriter;

	private final LockRedisRepository lockRedisRepository;

	@Transactional(readOnly = true)
	public LikeCakeImageListResponse findCakeImagesByCursorAndLike(final LikeCakeSearchRequest dto,
		final User signInUser) {
		final List<LikeCakeImageResponseParam> cakeImages = cakeLikeReader.searchCakeImagesByCursorAndLike(
			dto.cakeLikeId(),
			signInUser.getId(),
			dto.pageSize()
		);

		return CakeMapper.supplyLikeCakeImageListResponseBy(cakeImages);
	}

	@Transactional
	public void likeCakeWithLock(final User user, final Long cakeId) {
		lockRedisRepository.executeWithLock(RedisKey.LOCK_CAKE_LIKE, 100L,
			() -> likeCake(user, cakeId)
		);
	}

	@Transactional
	public void likeCake(final User user, final Long cakeId) {
		final Cake cake = cakeReader.findById(cakeId);
		final CakeLike cakeLike = cakeLikeReader.findOrNullByUserAndCake(user, cake);

		cakeLikeWriter.likeCakeOrCancel(cakeLike, user, cake);
	}
}
