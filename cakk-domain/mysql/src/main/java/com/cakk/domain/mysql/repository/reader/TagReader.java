package com.cakk.domain.mysql.repository.reader;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.entity.cake.Tag;
import com.cakk.domain.mysql.mapper.TagMapper;
import com.cakk.domain.mysql.repository.jpa.TagJpaRepository;

@Reader
@RequiredArgsConstructor
public class TagReader {

	private final TagJpaRepository tagJpaRepository;

	public Optional<Tag> findByTagName(final String tagName) {
		return tagJpaRepository.findTagByTagName(tagName);
	}

	public List<Tag> getTagsByTagName(final List<String> tagNames) {
		List<Tag> tags = tagJpaRepository.findTagsByTagNameIsIn(tagNames);

		return tagNames.stream()
			.map(tagName -> tags
				.stream()
				.filter(tag -> tag.getTagName().equals(tagName))
				.findAny()
				.orElse(tagJpaRepository.save(TagMapper.supplyTagBy(tagName))))
			.toList();
	}
}
