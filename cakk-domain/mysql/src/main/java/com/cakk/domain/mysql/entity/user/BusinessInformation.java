package com.cakk.domain.mysql.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cakk.domain.mysql.dto.param.user.CertificationParam;
import com.cakk.domain.mysql.entity.audit.AuditEntity;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.event.EventMapper;
import com.cakk.domain.mysql.event.shop.CertificationEvent;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "business_information")
public class BusinessInformation extends AuditEntity {

	@Id
	@Column(name = "shop_id")
	private Long id;

	@Column(name = "business_number", length = 20)
	private String businessNumber;

	@OneToOne
	@MapsId
	@JoinColumn(name = "shop_id")
	private CakeShop cakeShop;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;

	@Builder
	public BusinessInformation(
		CakeShop cakeShop,
		String businessNumber,
		User user
	) {
		this.id = cakeShop.getId();
		this.cakeShop = cakeShop;
		this.businessNumber = businessNumber;
		this.user = user;
	}

	public CertificationEvent getRequestCertificationMessage(final CertificationParam param) {
		if (isExistMyCakeShop()) {
			return EventMapper.supplyCertificationInfoWithCakeShopInfo(param, cakeShop);
		}
		return EventMapper.supplyCertificationInfo(param);

	}

	public void promotedByBusinessOwner(final User businessOwner) {
		user = businessOwner;
		user.upgradedRoleToBusinessOwner();
		cakeShop.ownedByUser();
	}

	private boolean isExistMyCakeShop() {
		return cakeShop != null && user == null;
	}

}
