package com.cakk.domain.mysql.entity.cake;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.domain.mysql.entity.audit.AuditCreatedEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cake_category", catalog = "cakk")
public class CakeCategory extends AuditCreatedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cake_category_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cake_id", referencedColumnName = "cake_id")
	private Cake cake;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "cake_design_category")
	private CakeDesignCategory cakeDesignCategory;

	@Builder
	public CakeCategory(Cake cake, CakeDesignCategory cakeDesignCategory) {
		this.cake = cake;
		this.cakeDesignCategory = cakeDesignCategory;
	}

	public void updateCake(Cake cake) {
		this.cake = cake;
	}
}
