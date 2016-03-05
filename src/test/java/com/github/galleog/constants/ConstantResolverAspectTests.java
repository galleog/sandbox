package com.github.galleog.constants;

import org.aspectj.lang.Aspects;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Persistable;
import org.springframework.util.ReflectionUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ConstantResolverAspect}.
 *
 * @author Oleg Galkin
 */
@Test
public class ConstantResolverAspectTests {
    private static final String ALGERIA = "ALGERIA";
    private static final String BAHRAIN = "BAHRAIN";
    private static final String CANADA = "CANADA";
    private static final String DENMARK = "DENMARK";
    private static final String ECUADOR = "ECUADOR";
    private static final String FRANCE = "FRANCE";
    private static final String GERMANY = "GERMANY";

    @Mock
    private FieldResolver resolver;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ConstantResolverAspect aspect = Aspects.aspectOf(ConstantResolverAspect.class);
        aspect.setResolver(resolver);
    }

    /**
     * Verifies that the aspect is applied correctly.
     */
    public void testAspectWeaving() throws Exception {
        when(resolver.resolve(any(Field.class))).thenAnswer(new Answer<Persistable<?>>() {
            @Override
            public Persistable<?> answer(InvocationOnMock invocation) throws Throwable {
                Field field = (Field) invocation.getArguments()[0];
                Country country = (Country) field.get(null);
                country.setName(field.getName());
                return country;
            }
        });

        assertThat(ResolveConstantType.ALGERIA.getName(), is(ALGERIA));
        assertThat(((Country) ReflectionUtils.findField(ResolveConstantType.class, ALGERIA).get(null)).getName(),
                is(ALGERIA));
        assertThat(ResolveConstantType.BAHRAIN.getName(), is(BAHRAIN));
        assertThat(((Country) ReflectionUtils.findField(ResolveConstantType.class, BAHRAIN).get(null)).getName(),
                is(BAHRAIN));
        assertThat(SuperclassOfResolveConstantType.CANADA.getName(), is(CANADA));
        assertThat(((Country) ReflectionUtils.findField(SuperclassOfResolveConstantType.class, CANADA).get(null))
                .getName(), is(CANADA));
        assertThat(SuperclassOfResolveConstantType.DENMARK.getName(), is(DENMARK));
        assertThat(((Country) ReflectionUtils.findField(SuperclassOfResolveConstantType.class, DENMARK).get(null))
                .getName(), is(DENMARK));
        assertThat(NotResolveConstantType.ECUADOR.getName(), is(ECUADOR));
        assertThat(((Country) ReflectionUtils.findField(NotResolveConstantType.class, ECUADOR).get(null)).getName(),
                is(ECUADOR));
        assertThat(NotResolveConstantType.FRANCE.getName(), is(FRANCE));
        assertThat(((Country) ReflectionUtils.findField(NotResolveConstantType.class, FRANCE).get(null)).getName(),
                is(FRANCE));
        assertThat(NotResolveConstantType.GERMANY.getName(), nullValue());
        assertThat(((Country) ReflectionUtils.findField(NotResolveConstantType.class, GERMANY).get(null)).getName(),
                nullValue());
    }

    @ResolveConstant
    public static class ResolveConstantType {
        public static final Country ALGERIA = new Country("DZ");
        public static final Country BAHRAIN = new Country("BH");
    }

    public static class SuperclassOfResolveConstantType extends ResolveConstantType {
        public static final Country CANADA = new Country("CA");
        public static final Country DENMARK = new Country("DK");
    }

    public static class NotResolveConstantType {
        @ResolveConstant
        public final static Country ECUADOR = new Country("EC");
        @ResolveConstant
        public final static Country FRANCE = new Country("FR");
        public final static Country GERMANY = new Country("DE");
    }
}