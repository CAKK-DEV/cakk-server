package com.cakk.infrastructure.persistence.entity.shop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cakk.infrastructure.persistence.entity.audit.AuditCreatedEntity;
import com.cakk.infrastructure.persistence.entity.user.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cake_shop_heart")
public class CakeShopHeart extends AuditCreatedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shop_heart_id", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "shop_id", referencedColumnName = "shop_id", nullable = false)
	private CakeShop cakeShop;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	private User user;

	public CakeShopHeart(CakeShop cakeShop, User user) {
		this.cakeShop = cakeShop;
		this.user = user;
	}
}
