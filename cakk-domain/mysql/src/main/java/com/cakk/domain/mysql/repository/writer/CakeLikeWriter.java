package com.cakk.domain.mysql.repository.writer;

import static java.util.Objects.*;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Writer;
import com.cakk.domain.mysql.entity.cake.CakeLike;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.jpa.CakeLikeJpaRepository;

@Writer
@RequiredArgsConstructor
public class CakeLikeWriter {

	private final CakeLikeJpaRepository cakeLikeJpaRepository;

	public void deleteAllByUser(final User user) {
		final List<CakeLike> cakeLikes = cakeLikeJpaRepository.findAllByUser(user);

		if (isNull(cakeLikes) || cakeLikes.isEmpty()) {
			return;
		}

		cakeLikeJpaRepository.deleteAllInBatch(cakeLikes);
	}
}
