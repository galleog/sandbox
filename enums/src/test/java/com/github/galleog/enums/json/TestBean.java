package com.github.galleog.enums.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Test bean for serialization/deserialization tests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestBean {
    private OperationEnum operation;
    private IntegerEnum integer;
}
