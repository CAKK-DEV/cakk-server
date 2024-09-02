package com.cakk.external.vo;

public record PresignedUrl(
	String imagePath,
	String imageUrl,
	String presignedUrl
) {
}
