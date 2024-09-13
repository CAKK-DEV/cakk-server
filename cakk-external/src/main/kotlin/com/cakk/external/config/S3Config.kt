package com.cakk.external.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder

@Configuration
class S3Config(
    @Value("\${cloud.aws.credentials.access-key}") private val accessKey: String,
    @Value("\${cloud.aws.credentials.secret-key}") private val secretKey: String,
    @Value("\${cloud.aws.region.static}") private val region: String
) {
    @Bean
    @Primary
    fun awsCredentialsProvider(): BasicAWSCredentials {
        return BasicAWSCredentials(accessKey, secretKey)
    }

    @Bean
    fun amazonS3(): AmazonS3 {
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(AWSStaticCredentialsProvider(awsCredentialsProvider()))
            .build()
    }
}

