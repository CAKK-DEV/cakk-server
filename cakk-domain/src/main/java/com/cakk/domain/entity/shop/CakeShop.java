package com.cakk.domain.entity.shop;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cakk.domain.entity.audit.AuditEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cake_shop")
public class CakeShop extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shop_id", nullable = false)
	private Long id;

	@Column(name = "shop_name", length = 30, nullable = false)
	private String shopName;

	@Column(name = "shop_bio", length = 40)
	private String shopBio;

	@Column(name = "shop_description", length = 500)
	private String shopDescription;

	@Column(name = "latitude", nullable = false, columnDefinition = "DECIMAL(18,10)")
	private Double latitude;

	@Column(name = "longitude", nullable = false, columnDefinition = "DECIMAL(18,10)")
	private Double longitude;

	@ColumnDefault("0")
	@Column(name = "like_count", nullable = false, columnDefinition = "MEDIUMINT")
	private Integer likeCount;

	@ColumnDefault("false")
	@Column(name = "linkedFlag", nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean linkedFlag;

	@ColumnDefault("null")
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Builder
	public CakeShop(String shopName, String shopBio, String shopDescription, Double latitude, Double longitude) {
		this.shopName = shopName;
		this.shopBio = shopBio;
		this.shopDescription = shopDescription;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public void ownedByUser() {
		linkedFlag = true;
	}

}
