package com.cakk.domain.mysql.bo.shop;

import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import com.cakk.domain.mysql.dto.param.shop.CakeShopOperationParam;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class CakeShopBySearchParam extends CakeShopParam {

	private Set<CakeShopOperationParam> operationDays;
}
