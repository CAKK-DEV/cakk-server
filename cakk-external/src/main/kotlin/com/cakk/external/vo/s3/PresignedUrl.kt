package com.cakk.external.vo.s3

data class PresignedUrl(
	val imagePath: String,
	val imageUrl: String,
	val presignedUrl: String
)
