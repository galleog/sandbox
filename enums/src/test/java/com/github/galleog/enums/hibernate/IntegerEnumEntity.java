package com.github.galleog.enums.hibernate;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Entity for tests of {@link HibernateIntegerEnumType}.
 *
 * @author Oleg Galkin
 */
@Entity
public class IntegerEnumEntity implements Serializable {
    @Id
    private int id;

    @Getter
    @Setter
    @Type(
            type = HibernateIntegerEnumType.CLASS_NAME,
            parameters = @Parameter(
                    name = AbstractHibernateEnumType.PARAMETER_NAME,
                    value = "com.github.galleog.enums.hibernate.IntegerNumberEnum"
            )
    )
    private IntegerNumberEnum enumValue;
}
