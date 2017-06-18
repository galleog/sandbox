package com.github.galleog.enums.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasNoJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Tests for enum serialization to JSON.
 *
 * @author Oleg Galkin
 */
@Test
public class EnumSerializerTests {
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Test for serialization of non-null values.
     */
    public void testSerialize() throws Exception {
        TestBean bean = new TestBean(OperationEnum.PLUS, IntegerEnum.TWO);
        String json = objectMapper.writeValueAsString(bean);
        assertThat(json, hasJsonPath("$.operation", equalTo("Plus")));
        assertThat(json, hasJsonPath("$.integer", equalTo(2)));
    }

    /**
     * Test for serialization of null values.
     */
    public void testSerializeNulls() throws Exception {
        TestBean bean = new TestBean();
        String json = objectMapper.writeValueAsString(bean);
        assertThat(json, hasNoJsonPath("$.operation"));
        assertThat(json, hasNoJsonPath("$.integer"));
    }
}
