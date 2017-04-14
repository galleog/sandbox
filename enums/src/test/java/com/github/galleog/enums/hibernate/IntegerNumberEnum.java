package com.github.galleog.enums.hibernate;

import com.github.galleog.enums.Enum;

/**
 * Enumeration type for integers whose keys are integers.
 *
 * @author Oleg Galkin
 */
public final class IntegerNumberEnum extends Enum<Integer> {
    public static final IntegerNumberEnum ONE = new IntegerNumberEnum(1);
    public static final IntegerNumberEnum TWO = new IntegerNumberEnum(2);

    private IntegerNumberEnum(int key) {
        super(key);
    }
}
