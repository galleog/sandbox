package com.github.galleog.sandbox.jsonfilter;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.lang3.Validate;

/**
 * <a href="http://wiki.fasterxml.com/JacksonHome">Jackson</a> {@link AnnotationIntrospector} to support
 * a custom JSON filter for all serializable classes.
 *
 * @author Oleg Galkin
 */
public class JsonFilterAnnotationIntrospector extends JacksonAnnotationIntrospector {
    /**
     * Default filter dentifier.
     */
    public static final String DEFAULT_FILTER_ID = JsonFilterAnnotationIntrospector.class.getName() + "#FILTER";

    private final String filterId;

    /**
     * Creates a new class instance with the default identifier.
     *
     * @see #DEFAULT_FILTER_ID
     */
    public JsonFilterAnnotationIntrospector() {
        this(DEFAULT_FILTER_ID);
    }

    /**
     * Creates a new class instance with the specified identifier.
     *
     * @param filterId the filter identifier
     */
    public JsonFilterAnnotationIntrospector(String filterId) {
        Validate.notEmpty(filterId);
        this.filterId = filterId;
    }

    @Override
    public Object findFilterId(Annotated ann) {
        Object id = super.findFilterId(ann);
        if (id == null) {
            JavaType javaType = TypeFactory.defaultInstance().constructType(ann.getRawType());
            if (!javaType.isContainerType()) {
                id = filterId;
            }
        }
        return id;
    }
}
