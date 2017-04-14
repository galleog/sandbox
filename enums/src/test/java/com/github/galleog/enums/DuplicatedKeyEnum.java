package com.github.galleog.enums;

/**
 * Enumeration type with a duplicated key.
 *
 * @author Oleg Galkin
 */
public final class DuplicatedKeyEnum extends Enum<String> {
	public static final DuplicatedKeyEnum RED = new DuplicatedKeyEnum("Red");
	public static final DuplicatedKeyEnum GREEN = new DuplicatedKeyEnum("Green");
	public static final DuplicatedKeyEnum GREENISH = new DuplicatedKeyEnum("Green");  // duplicated key

	private DuplicatedKeyEnum(String color) {
		super(color);
	}
}
