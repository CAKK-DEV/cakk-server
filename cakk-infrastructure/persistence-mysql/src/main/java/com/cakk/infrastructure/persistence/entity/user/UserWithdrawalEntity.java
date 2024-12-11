package com.cakk.infrastructure.persistence.entity.user;

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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cakk.common.enums.Gender;
import com.cakk.common.enums.Role;
import com.cakk.infrastructure.persistence.entity.audit.AuditCreatedEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_withdrawal")
public class UserWithdrawalEntity extends AuditCreatedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "email", length = 50, nullable = false)
	private String email;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "gender", length = 7, nullable = false)
	private Gender gender;

	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@Column(name = "birthday")
	private LocalDate birthday;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "role", length = 15, nullable = false)
	private Role role;

	@Column(name = "withdrawal_date", nullable = false)
	private LocalDateTime withdrawalDate;

	@Builder
	public UserWithdrawalEntity(
		String email,
		Gender gender,
		LocalDate birthday,
		Role role,
		LocalDateTime withdrawalDate
	) {
		this.email = email;
		this.gender = gender;
		this.birthday = birthday;
		this.role = role;
		this.withdrawalDate = withdrawalDate;
	}
}
