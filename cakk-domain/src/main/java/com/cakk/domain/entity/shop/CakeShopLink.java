package com.cakk.domain.entity.shop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import com.cakk.common.enums.LinkKind;
import com.cakk.domain.entity.audit.AuditEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cake_shop_link")
public class CakeShopLink extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "link_id", nullable = false)
	private Long id;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "link_kind", nullable = false, length = 20)
	private LinkKind linkKind;

	@Column(name = "link_path", nullable = false, length = 200)
	private String linkPath;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shop_id", referencedColumnName = "shop_id", nullable = false)
	private CakeShop cakeShop;

	@Builder
	public CakeShopLink(LinkKind linkKind, String linkPath, CakeShop cakeShop) {
		this.linkKind = linkKind;
		this.linkPath = linkPath;
		this.cakeShop = cakeShop;
	}
}
