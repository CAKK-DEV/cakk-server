package com.cakk.infrastructure.persistence.entity.cake;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.infrastructure.persistence.entity.audit.AuditEntity;
import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity;
import com.cakk.infrastructure.persistence.entity.user.UserEntity;
import com.cakk.infrastructure.persistence.mapper.CakeHeartMapper;
import com.cakk.infrastructure.persistence.mapper.CakeTagMapper;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cake")
public class CakeEntity extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cake_id", nullable = false)
	private Long id;

	@Column(name = "cake_image_url", nullable = false, length = 200)
	private String cakeImageUrl;

	@Column(name = "heart_count", nullable = false, columnDefinition = "MEDIUMINT")
	private Integer heartCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shop_id", referencedColumnName = "shop_id")
	private CakeShopEntity cakeShop;

	@OneToMany(mappedBy = "cake", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<CakeCategoryEntity> cakeCategories = new HashSet<>();

	@OneToMany(mappedBy = "cake", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<CakeTagEntity> cakeTags = new HashSet<>();

	@OneToMany(mappedBy = "cake", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<CakeHeartEntity> cakeHearts = new HashSet<>();

	@ColumnDefault("null")
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Builder
	public CakeEntity(String cakeImageUrl, CakeShopEntity cakeShop) {
		this.cakeImageUrl = cakeImageUrl;
		this.cakeShop = cakeShop;
		this.heartCount = 0;
	}

	public void heart(final UserEntity userEntity) {
		cakeHearts.add(CakeHeartMapper.supplyCakeHeartBy(this, userEntity));
		this.increaseHeartCount();
	}

	public void unHeart(final UserEntity userEntity) {
		cakeHearts.removeIf(it -> it.getUser().equals(userEntity));
		this.decreaseHeartCount();
	}

	public boolean isHeartedBy(final UserEntity userEntity) {
		return cakeHearts.stream().anyMatch(it -> it.getUser().equals(userEntity));
	}

	public void updateCakeImageUrl(final String cakeImageUrl) {
		this.cakeImageUrl = cakeImageUrl;
	}

	public void updateCakeCategories(final List<CakeCategoryEntity> cakeCategories) {
		this.cakeCategories.clear();
		registerCategories(cakeCategories);
	}

	public void updateCakeTags(final List<TagEntity> tags) {
		this.cakeTags.clear();
		registerTags(tags);
	}

	public void registerTags(final List<TagEntity> tags) {
		tags.forEach(tag -> this.cakeTags.add(CakeTagMapper.supplyCakeTagBy(this, tag)));
	}

	public void registerCategories(final List<CakeCategoryEntity> cakeCategories) {
		cakeCategories.forEach(cakeCategory -> {
			cakeCategory.updateCake(this);
			this.cakeCategories.add(cakeCategory);
		});
	}

	public void updateCakeShop(final CakeShopEntity cakeShop) {
		this.cakeShop = cakeShop;
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
