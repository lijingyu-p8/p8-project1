package com.example.demo.common;

import org.springframework.lang.Nullable;

public class CommonAssert {

	public static void notNull(@Nullable Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void notEmpty(@Nullable Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
		String str = object.toString();
		if (str.length() == 0) {
			throw new IllegalArgumentException(message);
		}
	}
}