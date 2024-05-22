package com.cakk.api.service.cake;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.cake.CakeSearchByCategoryRequest;
import com.cakk.api.dto.response.cake.CakeImageListResponse;
import com.cakk.domain.repository.reader.CakeReader;

@Service
@RequiredArgsConstructor
public class CakeService {

	private final CakeReader cakeReader;

	public CakeImageListResponse findCakeImagesByCursorAndCategory(final CakeSearchByCategoryRequest dto) {
		return CakeImageListResponse.from(cakeReader.findCakeImagesByCursorAndCategory(dto.cakeId(), dto.category(), dto.pageSize()));
	}
}
