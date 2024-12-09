package com.cakk.infrastructure.persistence.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.infrastructure.persistence.entity.cake.Cake;
import com.cakk.infrastructure.persistence.entity.cake.CakeHeart;
import com.cakk.infrastructure.persistence.entity.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CakeHeartMapper {

	public static CakeHeart supplyCakeHeartBy(final Cake cake, User user) {
		return new CakeHeart(cake, user);
	}
}
