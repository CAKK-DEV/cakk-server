package com.cakk.domain.mysql.repository.reader;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.entity.cake.Tag;
import com.cakk.domain.mysql.repository.jpa.TagJpaRepository;

@Reader
@RequiredArgsConstructor
public class TagReader {

	private final TagJpaRepository tagJpaRepository;

	public Optional<Tag> findByTagName(final String tagName) {
		return tagJpaRepository.findTagByTagName(tagName);
	}
}
