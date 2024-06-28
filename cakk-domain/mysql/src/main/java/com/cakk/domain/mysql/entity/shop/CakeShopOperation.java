package com.cakk.domain.mysql.entity.shop;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cakk.common.enums.Days;
import com.cakk.domain.mysql.converter.DayOfWeekConverter;
import com.cakk.domain.mysql.entity.audit.AuditEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cake_shop_operation")
public class CakeShopOperation extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "operation_id", nullable = false)
	private Long id;

	@Column(name = "operation_day", nullable = false)
	@Convert(converter = DayOfWeekConverter.class)
	private Days operationDay;

	@Column(name = "start_time", nullable = false)
	private LocalTime operationStartTime;

	@Column(name = "end_time", nullable = false)
	private LocalTime operationEndTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shop_id", referencedColumnName = "shop_id")
	private CakeShop cakeShop;

	@Builder
	public CakeShopOperation(Days operationDay, LocalTime operationStartTime, LocalTime operationEndTime, CakeShop cakeShop) {
		this.operationDay = operationDay;
		this.operationStartTime = operationStartTime;
		this.operationEndTime = operationEndTime;
		this.cakeShop = cakeShop;
	}

	public void updateCakeShop(CakeShop cakeShop) {
		this.cakeShop = cakeShop;
	}
}
