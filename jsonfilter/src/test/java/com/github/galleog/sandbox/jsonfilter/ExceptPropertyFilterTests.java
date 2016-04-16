package com.github.galleog.sandbox.jsonfilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for {@link ExceptPropertyFilter}.
 *
 * @author Oleg Galkin
 */
@Test
public class ExceptPropertyFilterTests {
    private static final String FILTER_ID = "filter";

    @DataProvider(name = "testData")
    public Object[][] getTestData() {
        try {
            return new Object[][]{
                    {
                            createFilter(ImmutableMap.<Class<?>, String[]>of(A.class, new String[]{"propA"})),
                            new B("a", 1),
                            "{\"propA\":\"a\"}"
                    },
                    {
                            createFilter(ImmutableMap.of(
                                    B.class, new String[]{"propB"},
                                    C.class, new String[]{"propC"}
                            )),
                            new B("a", 1),
                            "{\"propB\":{\"propC\":1}}"
                    },
                    {
                            createFilter(ImmutableMap.of(
                                    A.class, new String[]{"propA"},
                                    B.class, new String[]{"propB"},
                                    C.class, new String[]{"propC"}
                            )),
                            new B("b", 6),
                            "{\"propB\":{\"propC\":6}}"
                    },
                    {
                            createFilter(ImmutableMap.<Class<?>, String[]>of(A.class, new String[]{"propA"})),
                            new A("c"),
                            "{\"propA\":\"c\"}"
                    },
                    {
                            createFilter(ImmutableMap.<Class<?>, String[]>of(B.class, new String[]{"propB"})),
                            new B("b", 6),
                            "{\"propB\":{\"propC\":6}}"
                    }
            };
        } catch (Throwable t) {
            return new Object[][]{{}};
        }
    }

    /**
     * Test to filter properties out.
     */
    @Test(dataProvider = "testData")
    public void testFilter(PropertyFilter filter, Object pojo, String expected) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleFilterProvider filters = new SimpleFilterProvider();
        filters.addFilter(FILTER_ID, filter);
        mapper.setFilterProvider(filters);
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            @Override
            public Object findFilterId(Annotated a) {
                return FILTER_ID;
            }
        });

        assertThat(mapper.writeValueAsString(pojo), jsonEquals(expected));
    }

    private PropertyFilter createFilter(Map<Class<?>, String[]> fields) {
        ExceptPropertyFilter filter = new ExceptPropertyFilter();
        for (Map.Entry<Class<?>, String[]> entry : fields.entrySet()) {
            filter.addFilter(entry.getKey(), entry.getValue());
        }
        return filter;
    }

    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    public static class A {
        @Getter
        private final String propA;
    }

    public static class B extends A {
        @Getter
        private final C propB;

        B(String propA, int propC) {
            super(propA);
            this.propB = new C(propC);
        }
    }

    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    public static class C {
        @Getter
        private final int propC;
    }
}