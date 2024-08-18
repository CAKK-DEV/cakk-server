package com.cakk.domain.mysql.entity.user;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cakk.common.enums.VerificationStatus;
import com.cakk.domain.mysql.bo.user.VerificationPolicy;
import com.cakk.domain.mysql.converter.VerificationStatusConverter;
import com.cakk.domain.mysql.dto.param.user.CertificationParam;
import com.cakk.domain.mysql.entity.audit.AuditEntity;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.event.shop.CertificationEvent;
import com.cakk.domain.mysql.mapper.EventMapper;

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

	@Column(name = "business_registration_image_url", length = 200)
	private String businessRegistrationImageUrl;

	@Column(name = "id_card_image_url", length = 200)
	private String idCardImageUrl;

	@Column(name = "emergency_contact", length = 20)
	private String emergencyContact;

	@ColumnDefault("0")
	@Convert(converter = VerificationStatusConverter.class)
	@Column(name = "verification_status", nullable = false)
	private VerificationStatus verificationStatus = VerificationStatus.UNREQUESTED;

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
		this.verificationStatus = VerificationStatus.UNREQUESTED;
	}

	public void updateBusinessOwner(final VerificationPolicy verificationPolicy, final User businessOwner) {
		user = businessOwner;
		verificationStatus = verificationPolicy.approveToBusinessOwner(verificationStatus);
	}

	public boolean isBusinessOwnerCandidate(VerificationPolicy verificationPolicy) {
		return verificationPolicy.isCandidate(Objects.requireNonNull(verificationStatus));
	}

	public CertificationEvent registerCertificationInformation(CertificationParam param) {
		businessRegistrationImageUrl = param.businessRegistrationImageUrl();
		idCardImageUrl = param.idCardImageUrl();
		emergencyContact = param.emergencyContact();
		verificationStatus = verificationStatus.makePending();
		return EventMapper.supplyCertificationInfoWithCakeShopInfo(param, cakeShop);
	}

	public boolean isImPossibleRequestCertification() {
		return isAlreadyApproved() || isProcessingVerification() || isRejectVerification();
	}

	private boolean isAlreadyApproved() {
		return verificationStatus.isApproved();
	}

	private boolean isProcessingVerification() {
		return verificationStatus.isCandidate();
	}

	private boolean isRejectVerification() {
		return verificationStatus.isRejected();
	}
}

