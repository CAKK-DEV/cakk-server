package com.cakk.common.utils;

import java.util.Iterator;
import java.util.Set;

public class SetUtils {

	private SetUtils() {
		throw new IllegalStateException("util class");
	}

	public static <T> void keepOnlyNElements(final Set<T> set, final int max) {
		if (set == null || set.size() <= max) {
			return;
		}

		final Iterator<T> iterator = set.iterator();
		int count = 0;

		while (iterator.hasNext()) {
			iterator.next();
			count++;
			if (count > max) {
				iterator.remove();
			}
		}
	}
}
