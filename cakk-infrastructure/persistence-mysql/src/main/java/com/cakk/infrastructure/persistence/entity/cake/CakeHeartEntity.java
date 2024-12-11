package com.cakk.infrastructure.persistence.entity.cake;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cakk.infrastructure.persistence.entity.audit.AuditCreatedEntity;
import com.cakk.infrastructure.persistence.entity.user.UserEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cake_heart")
public class CakeHeartEntity extends AuditCreatedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cake_heart_id", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "cake_id", referencedColumnName = "cake_id", nullable = false)
	private CakeEntity cake;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	private UserEntity user;

	public CakeHeartEntity(CakeEntity cake, UserEntity userEntity) {
		this.cake = cake;
		this.user = userEntity;
	}
}
