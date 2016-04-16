package com.github.galleog.sandbox.jsonfilter;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Base class to define properties that should be kept when serializing a bean to JSON.
 *
 * @author Oleg Galkin
 */
public abstract class PredefinedPropSet {
    private final String[] properties;

    /**
     * Creates a property set instance that includes the specified properties.
     *
     * @param properties the properties that must be preserved in JSON
     */
    public PredefinedPropSet(String... properties) {
        this.properties = properties;
    }

    /**
     * Creates a property set instance with the specified parent set and added properties.
     *
     * @param parentSet  the parent property set
     * @param properties new propeties that must be preserved in JSON along with the parent set
     */
    public PredefinedPropSet(PredefinedPropSet parentSet, String... properties) {
        this.properties = ArrayUtils.addAll(parentSet.getProperties(), properties);
    }

    /**
     * Gets the properties that must be kept in JSON after serialization.
     */
    public String[] getProperties() {
        return properties;
    }
}
