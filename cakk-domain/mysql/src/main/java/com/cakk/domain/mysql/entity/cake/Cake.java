package com.cakk.domain.mysql.entity.cake;

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

import com.cakk.domain.mysql.entity.audit.AuditEntity;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.event.EventMapper;
import com.cakk.domain.mysql.event.views.CakeIncreaseViewsEvent;
import com.cakk.domain.mysql.mapper.CakeTagMapper;

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

	@ColumnDefault("null")
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Builder
	public Cake(String cakeImageUrl, CakeShop cakeShop) {
		this.cakeImageUrl = cakeImageUrl;
		this.cakeShop = cakeShop;
		this.heartCount = 0;
	}

	public void increaseHeartCount() {
		this.heartCount++;
	}

	public void decreaseHeartCount() {
		this.heartCount--;
	}

	public void updateCakeImageUrl(String cakeImageUrl) {
		this.cakeImageUrl = cakeImageUrl;
	}

	public void updateCakeCategories(List<CakeCategory> cakeCategories) {
		this.cakeCategories.clear();

		cakeCategories.forEach(cakeCategory -> {
			cakeCategory.updateCake(this);
			this.cakeCategories.add(cakeCategory);
		});
	}

	public void updateCakeTags(List<Tag> tags) {
		this.cakeTags.clear();

		tags.forEach(tag -> this.cakeTags.add(CakeTagMapper.supplyCakeTagBy(this, tag)));
	}

	public void removeCakeCategories() {
		this.cakeCategories.clear();
	}

	public void removeCakeTags() {
		this.cakeTags.clear();
	}

	public void registerTags(List<Tag> tags) {
		tags.forEach(tag -> this.cakeTags.add(CakeTagMapper.supplyCakeTagBy(this, tag)));
	}

	public void registerCategories(List<CakeCategory> cakeCategories) {
		cakeCategories.forEach(cakeCategory -> {
			cakeCategory.updateCake(this);
			this.cakeCategories.add(cakeCategory);
		});
	}

	public void updateCakeShop(CakeShop cakeShop) {
		this.cakeShop = cakeShop;
	}

	public CakeIncreaseViewsEvent getInCreaseViewsEvent() {
		return EventMapper.supplyCakeIncreaseViewsEvent(this.id);
	}
}
