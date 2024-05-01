package com.cakk.domain.entity.cake;

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

import com.cakk.domain.entity.audit.AuditCreatedEntity;
import com.cakk.domain.entity.user.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cake_like")
public class CakeLike extends AuditCreatedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cake_like_id", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "cake_id", referencedColumnName = "cake_id", nullable = false)
	private Cake cake;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	private User user;

	public CakeLike(Cake cake, User user) {
		this.cake = cake;
		this.user = user;
	}
}
