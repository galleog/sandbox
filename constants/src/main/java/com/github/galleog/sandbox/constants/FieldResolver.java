package com.github.galleog.sandbox.constants;

import org.springframework.data.domain.Persistable;

import java.lang.reflect.Field;

/**
 * Interface to resolve values of fields.
 *
 * @author Oleg Galkin
 */
public interface FieldResolver {
    /**
     * Gets the field value.
     *
     * @param field the field to get the value for
     */
    public Persistable<?> resolve(Field field);
}
