package com.cakk.domain.entity.cake;

import java.time.LocalTime;

import com.cakk.domain.entity.audit.AuditEntity;
import com.cakk.domain.entity.shop.CakeShop;

import jakarta.persistence.Column;
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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cake_shop_operation")
public class CakeShopOperation extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "operation_id", nullable = false)
	private Long id;

	@Column(name = "operation_day", nullable = false, length = 3)
	private String operationDay;

	@Column(name = "start_time", nullable = false)
	private LocalTime operationStartTime;

	@Column(name = "end_time", nullable = false)
	private LocalTime operationEndTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shop_id", referencedColumnName = "shop_id")
	private CakeShop cakeShop;

	@Builder
	public CakeShopOperation(String operationDay, LocalTime operationStartTime, LocalTime operationEndTime, CakeShop cakeShop) {
		this.operationDay = operationDay;
		this.operationStartTime = operationStartTime;
		this.operationEndTime = operationEndTime;
		this.cakeShop = cakeShop;
	}
}
