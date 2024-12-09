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
import com.cakk.infrastructure.persistence.entity.shop.CakeShop;
import com.cakk.infrastructure.persistence.entity.user.User;
import com.cakk.infrastructure.persistence.mapper.CakeHeartMapper;
import com.cakk.infrastructure.persistence.mapper.CakeTagMapper;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cake")
public class Cake extends AuditEntity {

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
	private CakeShop cakeShop;

	@OneToMany(mappedBy = "cake", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<CakeCategory> cakeCategories = new HashSet<>();

	@OneToMany(mappedBy = "cake", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<CakeTag> cakeTags = new HashSet<>();

	@OneToMany(mappedBy = "cake", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<CakeHeart> cakeHearts = new HashSet<>();

	@ColumnDefault("null")
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Builder
	public Cake(String cakeImageUrl, CakeShop cakeShop) {
		this.cakeImageUrl = cakeImageUrl;
		this.cakeShop = cakeShop;
		this.heartCount = 0;
	}

	public void heart(final User user) {
		cakeHearts.add(CakeHeartMapper.supplyCakeHeartBy(this, user));
		this.increaseHeartCount();
	}

	public void unHeart(final User user) {
		cakeHearts.removeIf(it -> it.getUser().equals(user));
		this.decreaseHeartCount();
	}

	public boolean isHeartedBy(final User user) {
		return cakeHearts.stream().anyMatch(it -> it.getUser().equals(user));
	}

	public void updateCakeImageUrl(final String cakeImageUrl) {
		this.cakeImageUrl = cakeImageUrl;
	}

	public void updateCakeCategories(final List<CakeCategory> cakeCategories) {
		this.cakeCategories.clear();
		registerCategories(cakeCategories);
	}

	public void updateCakeTags(final List<Tag> tags) {
		this.cakeTags.clear();
		registerTags(tags);
	}

	public void registerTags(final List<Tag> tags) {
		tags.forEach(tag -> this.cakeTags.add(CakeTagMapper.supplyCakeTagBy(this, tag)));
	}

	public void registerCategories(final List<CakeCategory> cakeCategories) {
		cakeCategories.forEach(cakeCategory -> {
			cakeCategory.updateCake(this);
			this.cakeCategories.add(cakeCategory);
		});
	}

	public void updateCakeShop(final CakeShop cakeShop) {
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
