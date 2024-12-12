package com.cakk.domain.aggregate.shop

import com.cakk.common.enums.VerificationStatus
import com.cakk.domain.base.Domain

class BusinessInformation(
	shopId: Long,
	ownerId: Long?,
	businessNumber: String?,
	businessRegistrationImageUrl: String?,
	idCardImageUrl: String?,
	emergencyContact: String?,
	verificationStatus: VerificationStatus = VerificationStatus.UNREQUESTED
) : Domain<BusinessInformation, Long>(shopId) {

	var shopId: Long = shopId
		private set

	var ownerId: Long? = ownerId
		private set

	var businessNumber: String? = businessNumber
		private set

	var businessRegistrationImageUrl: String? = businessRegistrationImageUrl
		private set

	var idCardImageUrl: String? = idCardImageUrl
		private set

	var emergencyContact: String? = emergencyContact
		private set

	var verificationStatus: VerificationStatus = verificationStatus
		private set
}
