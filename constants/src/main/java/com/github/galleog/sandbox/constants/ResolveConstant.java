package com.github.galleog.sandbox.constants;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation applied to {@code public static final} fields to resolve their values.
 *
 * @author Oleg Galkin
 */
@Documented
@Inherited
@Target({TYPE, FIELD})
@Retention(RUNTIME)
public @interface ResolveConstant {
}
