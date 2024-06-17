package com.cakk.domain.mysql.repository.writer;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Writer;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.repository.jpa.CakeJpaRepository;

@Writer
@RequiredArgsConstructor
public class CakeWriter {

	private final CakeJpaRepository cakeJpaRepository;

	public void deleteCake(Cake cake) {
		cakeJpaRepository.delete(cake);
	}
}
