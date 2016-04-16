package com.github.galleog.sandbox.jsonfilter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.Set;

/**
 * <a href="http://wiki.fasterxml.com/JacksonHome">Jackson</a> filter to exclude all the properties
 * that are not explicitly listed.
 *
 * @author Oleg Galkin
 */
public class ExceptPropertyFilter implements PropertyFilter {
    private final Map<Class<?>, Set<String>> filters = Maps.newHashMap();

    /**
     * Creates a new filter instance by the specified {@link JsonFilter} annotations.
     *
     * @param filters the annotations to define the filter
     */
    public ExceptPropertyFilter(JsonFilter... filters) {
        for (JsonFilter filter : filters) {
            ImmutableSet.Builder<String> builder = ImmutableSet.builder();
            builder.add(filter.include());

            for (Class<? extends PredefinedPropSet> cls : filter.propSets()) {
                PredefinedPropSet set;
                try {
                    set = cls.newInstance();
                } catch (Exception e) {
                    throw new IllegalArgumentException(cls.getName() + " must have a default constructor", e);
                }
                builder.add(set.getProperties());
            }
            this.filters.put(filter.target(), builder.build());
        }
    }

    /**
     * Adds a new filter for the specified class and its properties.
     *
     * @param cls        the Java Bean class
     * @param properties the array of the class properties that should be included in JSON
     */
    public void addFilter(Class<?> cls, String... properties) {
        Validate.notNull(cls);
        this.filters.put(cls, ImmutableSet.copyOf(properties));
    }

    /**
     * Adds a new filter for the specified class and its properties.
     *
     * @param cls        the Java Bean class
     * @param properties the {@link Iterable} of the class properties that should be included in JSON
     */
    public void addFilter(Class<?> cls, Iterable<String> properties) {
        Validate.notNull(cls);
        Validate.notNull(properties);
        this.filters.put(cls, ImmutableSet.copyOf(properties));
    }

    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider prov, PropertyWriter writer)
            throws Exception {
        if (include(pojo, writer)) {
            writer.serializeAsField(pojo, jgen, prov);
        } else if (!jgen.canOmitFields()) {
            writer.serializeAsOmittedField(pojo, jgen, prov);
        }
    }

    @Override
    public void serializeAsElement(Object elementValue, JsonGenerator jgen, SerializerProvider prov,
                                   PropertyWriter writer) throws Exception {
        writer.serializeAsElement(elementValue, jgen, prov);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void depositSchemaProperty(PropertyWriter writer, ObjectNode propertiesNode, SerializerProvider provider)
            throws JsonMappingException {
        writer.depositSchemaProperty(propertiesNode, provider);
    }

    @Override
    public void depositSchemaProperty(PropertyWriter writer, JsonObjectFormatVisitor objectVisitor,
                                      SerializerProvider provider) throws JsonMappingException {
        writer.depositSchemaProperty(objectVisitor, provider);
    }

    protected boolean include(Object object, PropertyWriter writer) {
        Validate.notNull(object);
        Set<String> properties = getProperties(object);
        return properties == null || properties.contains(writer.getName());
    }

    private Set<String> getProperties(Object object) {
        for (Class<?> cls = object.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
            Set<String> fields = filters.get(cls);
            if (fields != null) {
                return fields;
            }
        }
        return null;
    }
}
