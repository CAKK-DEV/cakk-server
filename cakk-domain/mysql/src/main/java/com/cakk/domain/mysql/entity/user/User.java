package com.cakk.domain.mysql.entity.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cakk.common.enums.Gender;
import com.cakk.common.enums.Provider;
import com.cakk.common.enums.Role;
import com.cakk.domain.mysql.dto.param.user.ProfileUpdateParam;
import com.cakk.domain.mysql.entity.audit.AuditEntity;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.shop.CakeShop;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends AuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private Long id;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "provider", length = 10, nullable = false)
	private Provider provider;

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
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Builder
	public User(
		Provider provider,
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
		this.provider = provider;
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

	public void heartCake(final Cake cake) {
		cake.heart(this);
	}

	public void unHeartCake(final Cake cake) {
		cake.unHeart(this);
	}

	public void likeCakeShop(final CakeShop cakeShop) {
		cakeShop.like(this);
	}

	public void heartCakeShop(final CakeShop cakeShop) {
		cakeShop.heart(this);
	}

	public void unHeartCakeShop(final CakeShop cakeShop) {
		cakeShop.unHeart(this);
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof User that)) {
			return false;
		}
		return this.getId() != null && Objects.equals(this.getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
