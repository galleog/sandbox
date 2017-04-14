package com.github.galleog.enums.hibernate;

import org.hibernate.type.IntegerType;
import org.hibernate.type.SingleColumnType;

/**
 * Hibernate type used for enumerations whose keys are integers.
 *
 * @author Oleg Galkin
 */
public class HibernateIntegerEnumType extends AbstractHibernateEnumType {
    /**
     * Type class name.
     */
    public static final String CLASS_NAME = "com.github.galleog.enums.hibernate.HibernateIntegerEnumType";

    @Override
    protected SingleColumnType<?> getKeyType() {
        return IntegerType.INSTANCE;
    }
}
