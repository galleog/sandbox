package com.github.galleog.enums;

import java.util.List;

/**
 * Enumeration type for colors.
 *
 * @author Oleg Galkin
 */
public final class ColorEnum extends Enum<String> {
	public static final ColorEnum RED = new ColorEnum("Red");
	public static final ColorEnum GREEN = new ColorEnum("Green");
	public static final ColorEnum BLUE = new ColorEnum("Blue");

	private ColorEnum(String color) {
		super(color);
	}

	public static ColorEnum valueOf(String color) {
		return valueOf(ColorEnum.class, color);
	}

	public static List<ColorEnum> values() {
		return values(ColorEnum.class);
	}
}
