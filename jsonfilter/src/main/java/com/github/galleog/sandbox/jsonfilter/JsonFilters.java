package com.github.galleog.sandbox.jsonfilter;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Declares several {@link JsonFilter} annotations for different classes on the same method.
 *
 * @author Oleg Galkin
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface JsonFilters {
    /**
     * Defined filters.
     */
    JsonFilter[] value() default {};
}
