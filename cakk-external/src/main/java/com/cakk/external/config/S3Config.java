package com.cakk.external.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class S3Config {
	private final String accessKey;
	private final String secretKey;
	private final String region;

	public S3Config(
		@Value("${cloud.aws.credentials.access-key}")
		String accessKey,
		@Value("${cloud.aws.credentials.secret-key}")
		String secretKey,
		@Value("${cloud.aws.region.static}")
		String region) {
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.region = region;
	}

	@Bean
	@Primary
	public BasicAWSCredentials awsCredentialsProvider(){
		return new BasicAWSCredentials(accessKey, secretKey);
	}

	@Bean
	public AmazonS3 amazonS3() {
		return AmazonS3ClientBuilder.standard()
			.withRegion(region)
			.withCredentials(new AWSStaticCredentialsProvider(awsCredentialsProvider()))
			.build();
	}
}

