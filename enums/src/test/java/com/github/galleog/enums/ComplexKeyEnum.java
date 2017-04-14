package com.github.galleog.enums;

/**
 * Enumeration type with complex keys.
 *
 * @author Oleg Galkin
 */
public final class ComplexKeyEnum extends MessageSourceBasedNamedEnum<String> {
    public static final ComplexKeyEnum DIGIT = new ComplexKeyEnum("1");
    public static final ComplexKeyEnum WHITE_SPACE = new ComplexKeyEnum(" ");
    public static final ComplexKeyEnum EQUAL_SIGN = new ComplexKeyEnum("=");
    public static final ComplexKeyEnum COLON = new ComplexKeyEnum(":");
    public static final ComplexKeyEnum UNICODE = new ComplexKeyEnum("кириллица");

    private ComplexKeyEnum(String key) {
        super(key);
    }
}
