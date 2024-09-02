package com.cakk.external.service

import java.util.*

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import com.amazonaws.AmazonServiceException
import com.amazonaws.HttpMethod
import com.amazonaws.SdkClientException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.external.vo.PresignedUrl

@Service
class S3Service(
	private val amazonS3: AmazonS3,
	@Value("\${cloud.aws.s3.bucket}") private val bucket: String,
	@Value("\${cloud.aws.s3.expire-in}") private val expiredIn: String,
	@Value("\${cloud.aws.s3.object-key}") private val objectKey: String
) {

	fun getPresignedUrlWithImagePath(): PresignedUrl {
		try {
			val imagePath = makeObjectKey()
			val imageUrl = getImageUrl(imagePath)
			val generatePresignedUrlRequest = createGeneratePresignedUrlRequestInstance(imagePath)
			val presignedUrl = generatePresignedUrlRequest(generatePresignedUrlRequest)

			return PresignedUrl(imagePath, imageUrl, presignedUrl)
		} catch (e: SdkClientException) {
			throw CakkException(ReturnCode.EXTERNAL_SERVER_ERROR)
		}
	}

	fun deleteObject(imagePath: String) {
		try {
			amazonS3.deleteObject(bucket, imagePath)
		} catch (e: AmazonServiceException) {
			throw CakkException(ReturnCode.EXTERNAL_SERVER_ERROR)
		}
	}

	private fun createGeneratePresignedUrlRequestInstance(imagePath: String): GeneratePresignedUrlRequest {
		val expiration = Date()
		var expirationInMs = expiration.time
		expirationInMs += expiredIn.toLong()
		expiration.time = expirationInMs

		return GeneratePresignedUrlRequest(bucket, imagePath)
			.withMethod(HttpMethod.PUT)
			.withExpiration(expiration)
	}

	private fun generatePresignedUrlRequest(generatePresignedUrlRequest: GeneratePresignedUrlRequest): String {
		return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString()
	}

	private fun makeObjectKey(): String {
		return StringBuffer().append(objectKey).append("/").append(UUID.randomUUID()).append(".jpeg").toString()
	}

	private fun getImageUrl(imagePath: String): String {
		return amazonS3.getUrl(bucket, imagePath).toString()
	}
}

