package com.cakk.external.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.external.vo.PresignedUrl;

@Service
public class S3Service {

	private final AmazonS3 amazonS3;
	private final String bucket;
	private final String expiredIn;
	private final String objectKey;

	public S3Service(
		@Value("${cloud.aws.s3.bucket}") String bucket,
		@Value("${cloud.aws.s3.expire-in}") String expiredIn,
		@Value("${cloud.aws.s3.objectKey}") String objectKey,
		AmazonS3 amazonS3
	) {
		this.bucket = bucket;
		this.expiredIn = expiredIn;
		this.objectKey = objectKey;
		this.amazonS3 = amazonS3;
	}

	public PresignedUrl getPresignedUrlWithImagePath() {
		try {
			String imagePath = makeObjectKey();
			String imageUrl = getImageUrl(imagePath);
			GeneratePresignedUrlRequest generatePresignedUrlRequest = createGeneratePresignedUrlRequestInstance(
				imagePath);
			String presignedUrl = generatePresignedUrlRequest(generatePresignedUrlRequest);
			return new PresignedUrl(imagePath, imageUrl, presignedUrl);
		} catch (SdkClientException e) {
			throw new CakkException(ReturnCode.EXTERNAL_SERVER_ERROR);
		}
	}

	public void deleteObject(String imagePath) {
		try {
			amazonS3.deleteObject(bucket, imagePath);
		} catch (AmazonServiceException e) {
			throw new CakkException(ReturnCode.EXTERNAL_SERVER_ERROR);
		}
	}

	private GeneratePresignedUrlRequest createGeneratePresignedUrlRequestInstance(String imagePath) {
		Date expiration = new Date();
		long expirationInMs = expiration.getTime();
		expirationInMs += Long.parseLong(expiredIn);
		expiration.setTime(expirationInMs);

		return new GeneratePresignedUrlRequest(bucket, imagePath)
			.withMethod(HttpMethod.PUT)
			.withExpiration(expiration);
	}

	private String generatePresignedUrlRequest(GeneratePresignedUrlRequest generatePresignedUrlRequest)
		throws SdkClientException {
		return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
	}

	private String makeObjectKey() {
		return new StringBuffer().append(objectKey).append("/").append(UUID.randomUUID()).toString();
	}

	private String getImageUrl(String imagePath) {
		return amazonS3.getUrl(bucket, imagePath).toString();
	}
}

