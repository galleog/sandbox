package com.github.galleog.enums.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Extension of {@link IntegerEnum}.
 *
 * @author Oleg Galkin
 */
@JsonDeserialize(using = EnumDeserializer.class)
public final class ExtendedIntegerEnum extends IntegerEnum {
    public final static IntegerEnum THREE = new ExtendedIntegerEnum(3);

    private ExtendedIntegerEnum(int key) {
        super(key);
    }
}
