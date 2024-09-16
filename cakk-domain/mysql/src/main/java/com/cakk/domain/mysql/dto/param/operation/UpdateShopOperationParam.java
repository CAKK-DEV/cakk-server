package com.cakk.domain.mysql.dto.param.operation;

import java.util.List;

import lombok.Builder;

import com.cakk.domain.mysql.entity.shop.CakeShopOperation;
import com.cakk.domain.mysql.entity.user.User;

@Builder
public record UpdateShopOperationParam(
	List<CakeShopOperation> cakeShopOperations,
	User user,
	Long cakeShopId
) {
}
