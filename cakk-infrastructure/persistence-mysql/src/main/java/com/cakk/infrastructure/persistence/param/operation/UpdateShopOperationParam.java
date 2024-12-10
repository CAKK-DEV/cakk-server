package com.cakk.infrastructure.persistence.param.operation;

import java.util.List;

import lombok.Builder;

import com.cakk.infrastructure.persistence.entity.shop.CakeShopOperationEntity;
import com.cakk.infrastructure.persistence.entity.user.UserEntity;

@Builder
public record UpdateShopOperationParam(
	List<CakeShopOperationEntity> cakeShopOperations,
	UserEntity userEntity,
	Long cakeShopId
) {
}
