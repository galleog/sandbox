package com.github.galleog.enums;

/**
 * Enumeration type for verifying constant names.
 *
 * @author Oleg Galkin
 */
public final class MessageSourceTestEnum extends MessageSourceBasedNamedEnum<String> {
    public static final MessageSourceTestEnum TEST = new MessageSourceTestEnum("test");
    public static final MessageSourceTestEnum SAMPLE = new MessageSourceTestEnum("sample");

    private MessageSourceTestEnum(String key) {
        super(key);
    }
}
