package com.github.galleog.sandbox.constants;

import org.apache.commons.lang3.Validate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Persistable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Implementation of {@link FieldResolver} based on JPA.
 *
 * @author Oleg Galkin
 */
public class JpaFieldResolver implements FieldResolver {
    public static final String CACHE_NAME = "constants";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_NAME, key = "#field.declaringClass.name + '#' + #field.name")
    public Persistable<?> resolve(Field field) {
        int mod = field.getModifiers();
        Validate.isTrue(Modifier.isPublic(mod) && Modifier.isStatic(mod) && Modifier.isFinal(mod));
        try {
            Persistable<?> value = (Persistable<?>) field.get(null);
            return (Persistable<?>) em.find(field.getType(), value.getId());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not access field " + field, e);
        }
    }
}
