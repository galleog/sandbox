package com.github.galleog.enums.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.galleog.enums.Enum;

/**
 * Enumeration type for mathematical operations.
 *
 * @author Oleg Galkin
 */
@JsonDeserialize(using = EnumDeserializer.class)
public abstract class OperationEnum extends Enum<String> {
    public static final OperationEnum PLUS = new OperationEnum("Plus") {
        @Override
        public int eval(int a, int b) {
            return (a + b);
        }
    };

    public static final OperationEnum MINUS = new OperationEnum("Minus") {
        @Override
        public int eval(int a, int b) {
            return (a - b);
        }
    };

    protected OperationEnum(String operation) {
        super(operation);
    }

    @Override
    public Class<? extends Enum<String>> getEnumClass() {
        return OperationEnum.class;
    }

    public abstract int eval(int a, int b);
}
