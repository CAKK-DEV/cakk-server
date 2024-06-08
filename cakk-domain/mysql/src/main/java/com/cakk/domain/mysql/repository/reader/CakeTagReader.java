package com.cakk.domain.mysql.repository.reader;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.repository.query.CakeTagQueryRepository;

@Reader
@RequiredArgsConstructor
public class CakeTagReader {

	private final CakeTagQueryRepository cakeTagQueryRepository;
}
