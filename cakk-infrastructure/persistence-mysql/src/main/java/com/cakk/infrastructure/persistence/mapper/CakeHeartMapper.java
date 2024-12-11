package com.cakk.infrastructure.persistence.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.infrastructure.persistence.entity.cake.CakeEntity;
import com.cakk.infrastructure.persistence.entity.cake.CakeHeartEntity;
import com.cakk.infrastructure.persistence.entity.user.UserEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CakeHeartMapper {

	public static CakeHeartEntity supplyCakeHeartBy(final CakeEntity cake, UserEntity userEntity) {
		return new CakeHeartEntity(cake, userEntity);
	}
}
