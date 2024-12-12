package com.cakk.infrastructure.persistence.entity.user;

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
import com.cakk.infrastructure.persistence.bo.user.VerificationPolicy;
import com.cakk.infrastructure.persistence.converter.VerificationStatusConverter;
import com.cakk.infrastructure.persistence.entity.audit.AuditEntity;
import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity;
import com.cakk.infrastructure.persistence.mapper.EventMapper;
import com.cakk.infrastructure.persistence.param.user.CertificationParam;
import com.cakk.infrastructure.persistence.shop.CertificationEvent;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "business_information")
public class BusinessInformationEntity extends AuditEntity {

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
	private VerificationStatus verificationStatus;

	@OneToOne
	@MapsId
	@JoinColumn(name = "shop_id")
	private CakeShopEntity cakeShop;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private UserEntity user;

	@Builder
	public BusinessInformationEntity(
		CakeShopEntity cakeShop,
		String businessNumber,
		UserEntity userEntity
	) {
		this.id = cakeShop.getId();
		this.cakeShop = cakeShop;
		this.businessNumber = businessNumber;
		this.user = userEntity;
		this.verificationStatus = VerificationStatus.UNREQUESTED;
	}

	public void updateBusinessOwner(final VerificationPolicy verificationPolicy, final UserEntity businessOwner) {
		user = businessOwner;
		verificationStatus = verificationPolicy.approveToBusinessOwner(verificationStatus);
	}

	public void unLinkBusinessOwner() {
		user = null;
		verificationStatus = VerificationStatus.UNREQUESTED;
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
