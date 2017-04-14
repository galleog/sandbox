package com.github.galleog.enums.hibernate;

import com.github.galleog.enums.Enum;

/**
 * Enumeration type for integers whose keys are strings.
 *
 * @author Oleg Galkin
 */
public final class StringNumberEnum extends Enum<String> {
    public static final StringNumberEnum ONE = new StringNumberEnum("one");
    public static final StringNumberEnum TWO = new StringNumberEnum("two");

    private StringNumberEnum(String key) {
        super(key);
    }
}
