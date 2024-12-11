package com.cakk.infrastructure.persistence.entity.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cakk.common.enums.Gender;
import com.cakk.common.enums.ProviderType;
import com.cakk.common.enums.Role;
import com.cakk.infrastructure.persistence.entity.audit.AuditEntity;
import com.cakk.infrastructure.persistence.entity.cake.CakeEntity;
import com.cakk.infrastructure.persistence.entity.cake.CakeHeartEntity;
import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity;
import com.cakk.infrastructure.persistence.entity.shop.CakeShopHeartEntity;
import com.cakk.infrastructure.persistence.entity.shop.CakeShopLikeEntity;
import com.cakk.infrastructure.persistence.param.user.ProfileUpdateParam;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class UserEntity extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private Long id;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "provider", length = 10, nullable = false)
	private ProviderType providerType;

	@Column(name = "provider_id", length = 50, nullable = false)
	private String providerId;

	@Column(name = "nickname", length = 20, nullable = false)
	private String nickname;

	@Column(name = "profile_image_url", length = 200)
	private String profileImageUrl;

	@Column(name = "email", length = 50, nullable = false)
	private String email;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "gender", length = 7, nullable = false)
	private Gender gender;

	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@Column(name = "birthday")
	private LocalDate birthday;

	@Column(name = "device_os", length = 8)
	private String deviceOs;

	@Column(name = "device_token", length = 170)
	private String deviceToken;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "role", length = 15, nullable = false)
	private Role role;

	@ColumnDefault("null")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	private Set<BusinessInformationEntity> businessInformationSet = new HashSet<>();

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<CakeHeartEntity> cakeHearts = new HashSet<>();

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<CakeShopHeartEntity> cakeShopHearts = new HashSet<>();

	@JsonIgnore
	@OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<CakeShopLikeEntity> cakeShopLikes = new HashSet<>();

	@Builder
	public UserEntity(
		ProviderType providerType,
		String providerId,
		String nickname,
		String profileImageUrl,
		String email,
		Gender gender,
		LocalDate birthday,
		String deviceOs,
		String deviceToken,
		Role role
	) {
		this.providerType = providerType;
		this.providerId = providerId;
		this.nickname = nickname;
		this.profileImageUrl = profileImageUrl;
		this.email = email;
		this.gender = gender;
		this.birthday = birthday;
		this.deviceOs = deviceOs;
		this.deviceToken = deviceToken;
		this.role = role;
	}

	public void updateProfile(final ProfileUpdateParam param) {
		this.profileImageUrl = param.profileImageUrl();
		this.nickname = param.nickname();
		this.email = param.email();
		this.gender = param.gender();
		this.birthday = param.birthday();
	}

	public void heartCake(final CakeEntity cake) {
		cake.heart(this);
	}

	public void unHeartCake(final CakeEntity cake) {
		cake.unHeart(this);
	}

	public void likeCakeShop(final CakeShopEntity cakeShop) {
		cakeShop.like(this);
	}

	public void heartCakeShop(final CakeShopEntity cakeShop) {
		cakeShop.heart(this);
	}

	public void unHeartCakeShop(final CakeShopEntity cakeShop) {
		cakeShop.unHeart(this);
	}

	public void unHeartAndLikeAll() {
		cakeHearts.clear();
		cakeShopHearts.clear();
		cakeShopLikes.clear();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof UserEntity that)) {
			return false;
		}
		return this.getId() != null && Objects.equals(this.getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
