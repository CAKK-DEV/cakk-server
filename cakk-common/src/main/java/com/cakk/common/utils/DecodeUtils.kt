package com.cakk.common.utils;

import java.util.Base64;

public class DecodeUtils {

	private DecodeUtils() {
		throw new IllegalStateException("util class");
	}

	public static byte[] decodeBase64(String string) {
		return Base64.getUrlDecoder().decode(string);
	}
}
