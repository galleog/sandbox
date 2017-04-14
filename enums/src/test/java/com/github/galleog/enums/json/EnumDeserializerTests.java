package com.github.galleog.enums.json;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.galleog.enums.hibernate.AbstractHibernateEnumType;
import com.github.galleog.enums.hibernate.HibernateIntegerEnumType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;

/**
 * Tests for {@link EnumDeserializer}.
 *
 * @author Oleg Galkin
 */
@Test
public class EnumDeserializerTests {
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Test for deserialization of non-null values.
     */
    public void testDeserialize() throws Exception {
        TestBean bean = objectMapper.readValue("{\"operation\" : {\"key\" : \"Minus\"}, \"integer\" : {\"key\" : 1}}",
                TestBean.class);
        assertThat(bean.getOperation(), sameInstance(OperationEnum.MINUS));
        assertThat(bean.getInteger(), sameInstance(IntegerEnum.ONE));
    }

    /**
     * Test for deserialization of null values.
     */
    public void testDeserializeNulls() throws Exception {
        TestBean bean = objectMapper.readValue("{\"operation\" : null}", TestBean.class);
        assertThat(bean.getOperation(), nullValue());
        assertThat(bean.getOperation(), nullValue());
    }

    /**
     * Test for a deserialization exception when the key name is invalid.
     */
    public void testDeserializeAbsentKey() throws Exception {
        try {
            objectMapper.readValue("{\"integer\" : {\"name\" : \"one\"}}", TestBean.class);
            fail("Exception must be thrown");
        } catch (JsonMappingException e) {
            assertThat(e.getCause(), instanceOf(IllegalStateException.class));
        }
    }

    /**
     * Test for a deserialization exception when a key value is invalid.
     */
    public void testDeserializeInvalidKey() throws Exception {
        try {
            objectMapper.readValue("{\"integer\" : {\"key\" : 3}}", TestBean.class);
            fail("Exception must be thrown");
        } catch (JsonMappingException e) {
            assertThat(e.getCause(), instanceOf(IllegalArgumentException.class));
        }
    }

    /**
     * Test for deserialization of an inherited enumerations.
     */
    public void testDeserializeSubEnum() throws Exception {
        SubEnumBean bean = objectMapper.readValue("{\"integer\" : {\"key\" : 3}}", SubEnumBean.class);
        assertThat(bean.getInteger(), sameInstance(ExtendedIntegerEnum.THREE));
    }

    @Getter
    @Setter
    public static final class TestBean {
        private OperationEnum operation;
        private IntegerEnum integer;
    }

    @Getter
    @Setter
    public static final class SubEnumBean {
        @Type(
                type = HibernateIntegerEnumType.CLASS_NAME,
                parameters = @Parameter(
                        name = AbstractHibernateEnumType.PARAMETER_NAME,
                        value = "com.github.galleog.enums.json.ExtendedIntegerEnum"
                )
        )
        private IntegerEnum integer;
    }
}