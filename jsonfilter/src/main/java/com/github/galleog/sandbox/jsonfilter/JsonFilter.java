package com.github.galleog.sandbox.jsonfilter;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation applied to a Spring MVC {@code @RequestMapping} or {@code @ExceptionHandler} method to filter out
 * its response body during JSON serialization.
 * <p/>
 * Example:
 * <pre>
 * public class PropSet extends PredefinedPropSet {
 *     public PropSet() {
 *         super(&quot;propA&quot;);
 *     }
 * }
 *
 * &#064;JsonFilter(target = Bean.class, include = {&quot;propB&quot;}, propSets = PropSet.class)
 * public &#064;RequestBody getBean() {
 * 	...
 * }
 * </pre>
 *
 * @author Oleg Galkin
 * @see com.fasterxml.jackson.databind.ObjectMapper#setFilterProvider(com.fasterxml.jackson.databind.ser.FilterProvider)
 * @see com.fasterxml.jackson.databind.ObjectMapper#setAnnotationIntrospector(com.fasterxml.jackson.databind.AnnotationIntrospector)
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface JsonFilter {
    /**
     * Class the filter is used for.
     */
    Class<?> target();

    /**
     * Explicitly defined properties that should be kept when serializing the specified class to JSON.
     */
    String[] include() default {};

    /**
     * {@link PredefinedPropSet Property sets} that define the properties included in JSON.
     * Each property set class must have a default constructor.
     */
    Class<? extends PredefinedPropSet>[] propSets() default {};
}
