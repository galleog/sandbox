package com.github.galleog.enums.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.github.galleog.enums.Enum;
import org.apache.commons.lang3.Validate;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import java.io.IOException;

import static com.github.galleog.enums.hibernate.AbstractHibernateEnumType.PARAMETER_NAME;

/**
 * <a href="https://github.com/FasterXML/jackson">Jackson JSON</a> deserializer to convert
 * the key of an enumeration to the enumeration itself.
 *
 * @author Oleg Galkin
 */
public class EnumDeserializer<E extends Enum<?>> extends StdDeserializer<E> implements ContextualDeserializer {
    private static final String KEY_FIELD_NAME = "key";

    private final JavaType keyType;

    /**
     * Default constructor.
     * <p/>
     * It is needed just to create an instance of the deserializer declared in the
     * {@link com.fasterxml.jackson.databind.annotation.JsonDeserialize} annotation. The really used deserializer
     * will be created by {@link #createContextual(DeserializationContext, BeanProperty)}.
     */
    public EnumDeserializer() {
        this(null, null);
    }

    /**
     * Creates an instance of the deserializer.
     *
     * @param enumClass the enumeration class
     * @param keyType   the type of the ksy the enumeration uses
     */
    public EnumDeserializer(Class<?> enumClass, JavaType keyType) {
        super(enumClass);
        this.keyType = keyType;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
        if (property != null) {
            // определяем класс перечислимого типа
            Class<?> cls = null;
            Type annotation = property.getAnnotation(Type.class);
            if (annotation != null) {
                Parameter[] parameters = annotation.parameters();
                for (Parameter parameter : parameters) {
                    if (PARAMETER_NAME.equals(parameter.name())) {
                        try {
                            cls = Class.forName(parameter.value());
                        } catch (ClassNotFoundException e) {
                            throw new IllegalStateException("Class name " + parameter.value() + " is invalid", e);
                        }
                        break;
                    }
                }
            }

            if (cls == null) {
                cls = property.getType().getRawClass();
            }
            Validate.validState(Enum.class.isAssignableFrom(cls), "Class %s must be subclass of Enum", cls.getName());

            JavaType[] types = ctxt.getTypeFactory().findTypeParameters(cls, Enum.class);
            if (types == null || types.length != 1) {
                throw new IllegalStateException("Can not find type parameter for Enum of type " + cls.getName());
            }
            return new EnumDeserializer<E>(cls, types[0]);

        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.getCurrentToken() != JsonToken.START_OBJECT) {
            throw ctxt.wrongTokenException(p, JsonToken.START_OBJECT, "Start object expected");
        }

        E result = null;
        while (p.nextToken() != JsonToken.END_OBJECT) {
            if (p.getCurrentToken() == JsonToken.FIELD_NAME && KEY_FIELD_NAME.equals(p.getCurrentName())) {
                p.nextToken();

                Object key = ctxt.readValue(p, keyType);
                if (key != null) {
                    result = Enum.valueOf((Class<? extends E>) handledType(), key);
                }
            }
        }

        Validate.validState(result != null, "Key is not specified for enum value of class %s",
                handledType().getName());
        return result;
    }
}
