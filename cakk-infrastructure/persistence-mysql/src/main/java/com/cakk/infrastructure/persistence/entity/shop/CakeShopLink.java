package com.cakk.infrastructure.persistence.entity.shop;

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

import com.cakk.common.enums.LinkKind;
import com.cakk.infrastructure.persistence.converter.LinkKindConverter;
import com.cakk.infrastructure.persistence.entity.audit.AuditEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cake_shop_link")
public class CakeShopLink extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "link_id", nullable = false)
	private Long id;

	@Convert(converter = LinkKindConverter.class)
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

	public void updateCakeShop(final CakeShop cakeShop) {
		this.cakeShop = cakeShop;
	}
}
