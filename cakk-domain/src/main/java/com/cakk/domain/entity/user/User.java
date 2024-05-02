package com.cakk.domain.entity.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cakk.common.enums.Gender;
import com.cakk.common.enums.Provider;
import com.cakk.common.enums.Role;
import com.cakk.domain.entity.audit.AuditEntity;

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

	@Column(name = "birthday")
	private LocalDate birthday;

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
		Role role
	) {
		this.provider = provider;
		this.providerId = providerId;
		this.nickname = nickname;
		this.profileImageUrl = profileImageUrl;
		this.email = email;
		this.gender = gender;
		this.birthday = birthday;
		this.role = role;
	}
}
