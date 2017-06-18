package com.github.galleog.enums.json;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        TestBean bean = objectMapper.readValue("{\"operation\" : \"Minus\", \"integer\" : 1}",
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
        assertThat(bean.getInteger(), nullValue());
    }

    /**
     * Test for a deserialization exception when the key name is invalid.
     */
    @Test(expectedExceptions = JsonMappingException.class)
    public void testDeserializeAbsentKey() throws Exception {
        objectMapper.readValue("{\"integer\" : {\"one\"}}", TestBean.class);
    }

    /**
     * Test for a deserialization exception when a key value is invalid.
     */
    public void testDeserializeInvalidKey() throws Exception {
        try {
            objectMapper.readValue("{\"integer\" : 3}", TestBean.class);
            fail("Exception must be thrown");
        } catch (JsonMappingException e) {
            assertThat(e.getCause(), instanceOf(IllegalArgumentException.class));
        }
    }
}