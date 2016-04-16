package com.github.galleog.sandbox.jsonfilter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Map;

/**
 * Spring MVC controller for {@link JsonFilterIntegrationTests}.
 *
 * @author Oleg Galkin
 */
@Controller
public class TestController {
    @JsonFilter(target = TestBean.class, include = {"stringProp"}, propSets = TestBeanPropSet.class)
    @RequestMapping(value = "/testFilter", method = RequestMethod.GET)
    @ResponseBody
    public TestBean getTestBean() {
        return new TestBean(1, 1, 0L, "string", null);
    }

    @JsonFilters({
            @JsonFilter(target = TestBean.class, propSets = {TestBeanPropSet.class, BeanPropSet.class}),
            @JsonFilter(target = TestChildBean.class, include = {"id", "test"})
    })
    @RequestMapping(value = "/testFilterChild", method = RequestMethod.GET)
    @ResponseBody
    public TestBean getTestBeanWithChild() {
        return new TestBean(1, 1, 0L, "string", new TestChildBean(1, "test", "test2"));
    }

    @RequestMapping(value = "/testWithoutFilters", method = RequestMethod.GET)
    @ResponseBody
    public TestBean getTestBeanWithotFilters() {
        return new TestBean(1, 1, 0L, "string", new TestChildBean(1, "test", "test2"));
    }

    @JsonFilter(target = TestBean.class, include = {"id", "intProp"})
    @RequestMapping(value = "/testFilterMap", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getTestBeanMap() {
        return ImmutableMap.<String, Object>of("bean", new TestBean(1, 1, 0L, "string", null));
    }

    @JsonFilter(target = TestChildBean.class, include = "id")
    @RequestMapping(value = "/testFilterCollection", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Object> getTestBeanCollection() {
        return ImmutableList.<Object>of(new TestBean(1, 1, 0L, "string", new TestChildBean(1, "test", "test2")));
    }

    @JsonFilters({
            @JsonFilter(target = TestBean.class),
            @JsonFilter(target = TestChildBean.class, propSets = TestChildBeanPropSet.class)
    })
    @RequestMapping(value = "/testFilterArray", method = RequestMethod.GET)
    @ResponseBody
    public Object[] getTestBeanArray() {
        return new Object[]{new TestBean(1, 1, 0L, "string", null), new TestChildBean(1, "test", "test2")};
    }

    public static final class TestBeanPropSet extends PredefinedPropSet {
        public TestBeanPropSet() {
            super("id", "longProp");
        }
    }

    public static final class BeanPropSet extends PredefinedPropSet {
        public BeanPropSet() {
            super("bean");
        }
    }

    public static final class TestChildBeanPropSet extends PredefinedPropSet {
        public TestChildBeanPropSet() {
            super("id");
        }
    }

    public static final class TestBean {
        @Getter
        private final int id;
        @Getter
        private final int intProp;
        @Getter
        private final long longProp;
        private final String stringProp;
        @Getter
        private final TestChildBean bean;

        public TestBean(int id, int intProp, long longProp, String stringProp, TestChildBean bean) {
            this.id = id;
            this.intProp = intProp;
            this.longProp = longProp;
            this.stringProp = stringProp;
            this.bean = bean;
        }

        @JsonIgnore
        public String getStringProp() {
            return stringProp;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static final class TestChildBean {
        private final int id;
        private final String test;
        private final String test2;
    }
}
