package com.cakk.domain.mysql.dto.param.operation;

import java.util.List;

import com.cakk.domain.mysql.entity.shop.CakeShopOperation;
import com.cakk.domain.mysql.entity.user.User;

public record UpdateShopOperationParam(
	List<CakeShopOperation> cakeShopOperations,
	User user,
	Long cakeShopId
) {
}
