package com.cakk.infrastructure.persistence.entity.shop;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.locationtech.jts.geom.Point;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.infrastructure.persistence.entity.audit.AuditEntity;
import com.cakk.infrastructure.persistence.entity.cake.CakeEntity;
import com.cakk.infrastructure.persistence.entity.user.BusinessInformationEntity;
import com.cakk.infrastructure.persistence.entity.user.UserEntity;
import com.cakk.infrastructure.persistence.mapper.CakeShopHeartMapper;
import com.cakk.infrastructure.persistence.mapper.CakeShopLikeMapper;
import com.cakk.infrastructure.persistence.param.shop.CakeShopUpdateParam;
import com.cakk.infrastructure.persistence.param.shop.UpdateShopAddressParam;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cake_shop")
public class CakeShopEntity extends AuditEntity {

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

	@ColumnDefault("null")
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@OneToOne(mappedBy = "cakeShop", cascade = CascadeType.PERSIST)
	private BusinessInformationEntity businessInformation;

	@OneToMany(mappedBy = "cakeShop", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<CakeShopLinkEntity> cakeShopLinks = new ArrayList<>();

	@OneToMany(mappedBy = "cakeShop", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<CakeShopOperationEntity> cakeShopOperations = new HashSet<>();

	@OneToMany(mappedBy = "cakeShop", cascade = CascadeType.PERSIST)
	private Set<CakeEntity> cakes = new HashSet<>();

	@OneToMany(mappedBy = "cakeShop", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<CakeShopHeartEntity> shopHearts = new HashSet<>();

	@OneToMany(mappedBy = "cakeShop", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<CakeShopLikeEntity> shopLikes = new HashSet<>();

	@Builder
	public CakeShopEntity(
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
		this.heartCount = 0;
		this.likeCount = 0;
	}

	public void like(final UserEntity userEntity) {
		if (isLikedOverMaxBy(userEntity)) {
			throw new CakkException(ReturnCode.MAX_CAKE_SHOP_LIKE);
		}

		shopLikes.add(CakeShopLikeMapper.supplyCakeShopLikeBy(this, userEntity));
		this.increaseLikeCount();
	}

	public void heart(final UserEntity userEntity) {
		shopHearts.add(CakeShopHeartMapper.supplyCakeShopHeartBy(this, userEntity));
		this.increaseHeartCount();
	}

	public void unHeart(final UserEntity userEntity) {
		shopHearts.removeIf(it -> it.getUser().equals(userEntity));
		this.decreaseHeartCount();
	}

	public boolean isLikedOverMaxBy(final UserEntity userEntity) {
		long count = shopLikes.stream().map(it -> it.getUser().equals(userEntity)).count();

		return count >= 50;
	}

	public boolean isHeartedBy(final UserEntity userEntity) {
		return shopHearts.stream().anyMatch(it -> it.getUser().equals(userEntity));
	}

	public void updateBasicInformation(final CakeShopUpdateParam param) {
		thumbnailUrl = param.thumbnailUrl();
		shopName = param.shopName();
		shopBio = param.shopBio();
		shopDescription = param.shopDescription();
	}

	public void registerBusinessInformation(final BusinessInformationEntity businessInformationEntity) {
		this.businessInformation = businessInformationEntity;
	}

	public void addShopLinks(final List<CakeShopLinkEntity> cakeShopLinks) {
		this.cakeShopLinks.addAll(cakeShopLinks);
	}

	public void updateShopLinks(final List<CakeShopLinkEntity> cakeShopLinks) {
		this.cakeShopLinks.clear();

		cakeShopLinks.forEach(cakeShopLink -> {
			cakeShopLink.updateCakeShop(this);
			this.cakeShopLinks.add(cakeShopLink);
		});
	}

	public void updateShopAddress(final UpdateShopAddressParam param) {
		shopAddress = param.shopAddress();
		location = param.location();
	}

	public void addShopOperationDays(final List<CakeShopOperationEntity> cakeShopOperations) {
		this.cakeShopOperations.addAll(cakeShopOperations);
	}

	public void updateShopOperationDays(final List<CakeShopOperationEntity> cakeShopOperations) {
		this.cakeShopOperations.clear();

		cakeShopOperations.forEach(cakeShopOperation -> {
			cakeShopOperation.updateCakeShop(this);
			this.cakeShopOperations.add(cakeShopOperation);
		});
	}

	public void registerCake(final CakeEntity cake) {
		cake.updateCakeShop(this);
		this.cakes.add(cake);
	}

	private void increaseLikeCount() {
		this.likeCount++;
	}

	private void increaseHeartCount() {
		this.heartCount++;
	}

	private void decreaseHeartCount() {
		if (this.heartCount == 0) {
			throw new CakkException(ReturnCode.INTERNAL_SERVER_ERROR);
		}

		this.heartCount--;
	}
}
