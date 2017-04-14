package com.github.galleog.enums;

import java.io.Serializable;

/**
 * Realization of {@link NamedEnum} whose value name is specified explicitly.
 *
 * @author Oleg Galkin
 */
public abstract class DefaultNamedEnum<T extends Serializable & Comparable<? super T>> extends NamedEnum<T> {
    private final String name;

    /**
     * Creates a new enumeration value by its unique ksy and name.
     *
     * @param key  the value key
     * @param name the value name
     */
    protected DefaultNamedEnum(T key, String name) {
        super(key);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
