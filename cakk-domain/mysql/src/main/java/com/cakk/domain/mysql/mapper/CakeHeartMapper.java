package com.cakk.domain.mysql.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.cake.CakeHeart;
import com.cakk.domain.mysql.entity.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CakeHeartMapper {

	public static CakeHeart supplyCakeHeartBy(final Cake cake, User user) {
		return new CakeHeart(cake, user);
	}
}
