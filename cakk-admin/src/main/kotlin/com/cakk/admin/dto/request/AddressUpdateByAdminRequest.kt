package com.cakk.admin.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class AddressUpdateByAdminRequest(
	@field:NotBlank @field:Size(max = 50)
	val shopAddress: String?,
	@field:NotNull @field:Min(-90) @field:Max(90)
	val latitude: Double?,
	@field:NotNull @field:Min(-180) @field:Max(180)
	val longitude: Double?
)
