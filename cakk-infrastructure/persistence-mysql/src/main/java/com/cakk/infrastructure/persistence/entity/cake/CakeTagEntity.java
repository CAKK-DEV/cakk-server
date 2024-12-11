package com.cakk.infrastructure.persistence.entity.cake;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import com.cakk.infrastructure.persistence.entity.audit.AuditCreatedEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cake_tag")
public class CakeTagEntity extends AuditCreatedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cake_tag_id", nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cake_id", referencedColumnName = "cake_id", nullable = false)
	private CakeEntity cake;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id", referencedColumnName = "tag_id", nullable = false)
	private TagEntity tag;

	@Builder
	public CakeTagEntity(CakeEntity cake, TagEntity tag) {
		this.cake = cake;
		this.tag = tag;
	}
}
