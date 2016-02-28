package com.github.galleog.constants;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;

/**
 * Test entity.
 *
 * @author Oleg Galkin
 */
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class State extends AbstractPersistable<Integer> {
    private String name;

    /**
     * Creates an entity by its identifier.
     *
     * @param id entity identifier
     */
    public State(int id) {
        setId(id);
    }
}
