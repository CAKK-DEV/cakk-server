package com.cakk.infrastructure.persistence.param.operation;

import java.util.List;

import lombok.Builder;

import com.cakk.infrastructure.persistence.entity.shop.CakeShopOperation;
import com.cakk.infrastructure.persistence.entity.user.User;

@Builder
public record UpdateShopOperationParam(
	List<CakeShopOperation> cakeShopOperations,
	User user,
	Long cakeShopId
) {
}
