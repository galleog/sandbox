package com.github.galleog.enums;

import java.io.Serializable;

/**
 * Base class for enumerations that have descriptions of their values.
 *
 * @author Oleg Galkin
 */
public abstract class NamedEnum<T extends Serializable & Comparable<? super T>> extends Enum<T> {
    /**
     * Creates a new enumeration value by its unique ksy.
     *
     * @param key the value key
     */
    protected NamedEnum(T key) {
        super(key);
    }

    /**
     * Gets the description of the enumeration value.
     */
    public abstract String getName();
}
