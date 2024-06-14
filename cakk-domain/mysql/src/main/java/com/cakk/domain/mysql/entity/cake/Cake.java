package com.cakk.domain.mysql.entity.cake;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cakk.domain.mysql.entity.audit.AuditEntity;
import com.cakk.domain.mysql.entity.shop.CakeShop;

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

	@ColumnDefault("null")
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

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
}
