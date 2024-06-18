package com.cakk.domain.mysql.entity.cake;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cakk.domain.mysql.entity.audit.AuditCreatedEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tag")
public class Tag extends AuditCreatedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tag_id", nullable = false)
	private Long id;

	@Column(name = "tag_name", length = 20, nullable = false)
	private String tagName;

	@Builder
	public Tag(String tagName) {
		this.tagName = tagName;
	}
}
