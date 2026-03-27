package io.github.martinsamuelkoleff.spring_writer.utils;

public final class SlugUtils {
	
	private SlugUtils() {}

	public static String toSlug(String text) {
        return text
            .toLowerCase()
            .trim()
            .replaceAll("[áàäâã]", "a")
            .replaceAll("[éèëê]", "e")
            .replaceAll("[íìïî]", "i")
            .replaceAll("[óòöôõ]", "o")
            .replaceAll("[úùüû]", "u")
            .replaceAll("[ñ]", "n")
            .replaceAll("[^a-z0-9\\s-]", "")
            .replaceAll("\\s+", "-")
            .replaceAll("-+", "-");
    }
}
