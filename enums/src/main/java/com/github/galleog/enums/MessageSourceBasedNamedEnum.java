package com.github.galleog.enums;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.io.Serializable;

/**
 * Base class for enumerations whose value descriptions can be retrieved via
 * {@link org.springframework.context.MessageSource}.
 *
 * @author Oleg Galkin
 */
public abstract class MessageSourceBasedNamedEnum<T extends Serializable & Comparable<? super T>> extends NamedEnum<T> {
    private volatile MessageSourceAccessor messageAccessor;

    /**
     * Creates a new enumeration value by its unique ksy.
     *
     * @param key the value key
     */
    protected MessageSourceBasedNamedEnum(T key) {
        super(key);
    }

    @Override
    public String getName() {
        return getMessageAccessor().getMessage(getKey().toString());
    }

    /**
     * Gets {@code MessageSourceAccessor} that used to retrieve enumeration descriptions.
     * {@link java.util.ResourceBundle} whose basename is the same as the name of the enumeration class
     * is used by default .
     */
    protected MessageSourceAccessor getMessageAccessor() {
        if (messageAccessor == null) {
            ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
            messageSource.setBasename(getEnumClass().getName());
            messageAccessor = new MessageSourceAccessor(messageSource);
        }
        return messageAccessor;
    }
}
