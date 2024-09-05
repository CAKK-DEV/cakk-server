package com.cakk.common.utils;

public class RandomUtils {

	private RandomUtils() {
		throw new IllegalStateException("util class");
	}

	public static String generateRandomStringOnlyNumber(int length) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < length; i++) {
			sb.append((int) (Math.random() * 10));
		}

		return sb.toString();
	}
}
