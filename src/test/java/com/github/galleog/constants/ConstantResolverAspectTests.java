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
    private static final String STATE_1 = "STATE_1";
    private static final String STATE_2 = "STATE_2";
    private static final String STATE_3 = "STATE_3";
    private static final String STATE_4 = "STATE_4";
    private static final String STATE_5 = "STATE_5";
    private static final String STATE_6 = "STATE_6";
    private static final String STATE_7 = "STATE_7";

    @Mock
    private FieldResolver resolver;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ConstantResolverAspect aspect = Aspects.aspectOf(ConstantResolverAspect.class);
        aspect.setResolver(resolver);
    }

    /**
     * Тест применения аспекта.
     */
    public void testAspectWeaving() throws Exception {
        when(resolver.resolve(any(Field.class))).thenAnswer(new Answer<Persistable<?>>() {
            @Override
            public Persistable<?> answer(InvocationOnMock invocation) throws Throwable {
                Field field = (Field) invocation.getArguments()[0];
                State state = (State) field.get(null);
                state.setName(field.getName());
                return state;
            }
        });

        assertThat(ResolveConstantType.STATE_1.getName(), is(STATE_1));
        assertThat(((State) ReflectionUtils.findField(ResolveConstantType.class, STATE_1).get(null)).getName(),
                is(STATE_1));
        assertThat(ResolveConstantType.STATE_2.getName(), is(STATE_2));
        assertThat(((State) ReflectionUtils.findField(ResolveConstantType.class, STATE_2).get(null)).getName(),
                is(STATE_2));
        assertThat(SuperclassOfResolveConstantType.STATE_3.getName(), is(STATE_3));
        assertThat(((State) ReflectionUtils.findField(SuperclassOfResolveConstantType.class, STATE_3).get(null))
                .getName(), is(STATE_3));
        assertThat(SuperclassOfResolveConstantType.STATE_4.getName(), is(STATE_4));
        assertThat(((State) ReflectionUtils.findField(SuperclassOfResolveConstantType.class, STATE_4).get(null))
                .getName(), is(STATE_4));
        assertThat(NotResolveConstantType.STATE_5.getName(), is(STATE_5));
        assertThat(((State) ReflectionUtils.findField(NotResolveConstantType.class, STATE_5).get(null)).getName(),
                is(STATE_5));
        assertThat(NotResolveConstantType.STATE_6.getName(), is(STATE_6));
        assertThat(((State) ReflectionUtils.findField(NotResolveConstantType.class, STATE_6).get(null)).getName(),
                is(STATE_6));
        assertThat(NotResolveConstantType.STATE_7.getName(), nullValue());
        assertThat(((State) ReflectionUtils.findField(NotResolveConstantType.class, STATE_7).get(null)).getName(),
                nullValue());
    }

    @ResolveConstant
    public static class ResolveConstantType {
        public static final State STATE_1 = new State(1);
        public static final State STATE_2 = new State(2);
    }

    public static class SuperclassOfResolveConstantType extends ResolveConstantType {
        public static final State STATE_3 = new State(3);
        public static final State STATE_4 = new State(4);
    }

    public static class NotResolveConstantType {
        @ResolveConstant
        public final static State STATE_5 = new State(5);
        @ResolveConstant
        public final static State STATE_6 = new State(6);
        public final static State STATE_7 = new State(7);
    }
}