package com.cakk.domain.mysql.entity.shop;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.locationtech.jts.geom.Point;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.dto.param.shop.CakeShopUpdateParam;
import com.cakk.domain.mysql.entity.audit.AuditEntity;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.entity.user.User;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cake_shop")
public class CakeShop extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "shop_id", nullable = false)
	private Long id;

	@Column(name = "thumbnail_url", length = 200)
	private String thumbnailUrl;

	@Column(name = "shop_name", length = 30, nullable = false)
	private String shopName;

	@Column(name = "shop_address", length = 50)
	private String shopAddress;

	@Column(name = "shop_bio", length = 40)
	private String shopBio;

	@Column(name = "shop_description", length = 500)
	private String shopDescription;

	@Column(name = "location", nullable = false, columnDefinition = "POINT SRID 4326")
	private Point location;

	@ColumnDefault("0")
	@Column(name = "like_count", nullable = false, columnDefinition = "MEDIUMINT")
	private Integer likeCount;

	@ColumnDefault("0")
	@Column(name = "heart_count", nullable = false, columnDefinition = "MEDIUMINT")
	private Integer heartCount;

	@ColumnDefault("false")
	@Column(name = "linked_flag", nullable = false, columnDefinition = "TINYINT(1)")
	private Boolean linkedFlag;

	@ColumnDefault("null")
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@OneToOne(mappedBy = "cakeShop")
	private BusinessInformation businessInformation;

	@Builder
	public CakeShop(
		String shopName,
		String thumbnailUrl,
		String shopAddress,
		String shopBio,
		String shopDescription,
		Point location
	) {
		this.shopName = shopName;
		this.thumbnailUrl = thumbnailUrl;
		this.shopAddress = shopAddress;
		this.shopBio = shopBio;
		this.shopDescription = shopDescription;
		this.location = location;
		this.likeCount = 0;
		this.heartCount = 0;
		this.linkedFlag = false;
	}

	public void ownedByUser() {
		linkedFlag = true;
	}

	public void increaseHeartCount() {
		this.heartCount++;
	}

	public void decreaseHeartCount() {
		this.heartCount--;
	}

	public void updateDefaultInformation(CakeShopUpdateParam param) {
		User owner = param.user();

		if (isNotOwner(owner)) {
			throw new CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER);
		}

		shopName = param.shopName();
		shopBio = param.shopBio();
		shopDescription = param.shopDescription();
	}

	private boolean isNotOwner(User owner) {
		return businessInformation.getUser().getId() != owner.getId();
	}
}
