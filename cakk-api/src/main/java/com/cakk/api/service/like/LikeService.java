package com.cakk.api.service.like;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.like.LikeCakeSearchRequest;
import com.cakk.api.dto.response.like.LikeCakeImageListResponse;
import com.cakk.api.mapper.CakeMapper;
import com.cakk.domain.mysql.dto.param.like.LikeCakeImageResponseParam;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.CakeLikeReader;

@RequiredArgsConstructor
@Service
public class LikeService {

	private final CakeLikeReader cakeLikeReader;

	public LikeCakeImageListResponse findCakeImagesByCursorAndLike(final LikeCakeSearchRequest dto, final User signInUser) {
		final List<LikeCakeImageResponseParam> cakeImages = cakeLikeReader.searchCakeImagesByCursorAndLike(
			dto.cakeLikeId(),
			signInUser.getId(),
			dto.pageSize()
		);

		return CakeMapper.supplyLikeCakeImageListResponseBy(cakeImages);
	}
}
