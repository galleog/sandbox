package com.github.galleog.sandbox.jsonfilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link JsonFilter} annotations in Spring MVC.
 *
 * @author Oleg Galkin
 */
@Test
@WebAppConfiguration
@ContextConfiguration(classes = JsonFilterIntegrationTests.ContextConfig.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        ServletTestExecutionListener.class
})
public class JsonFilterIntegrationTests extends AbstractTestNGSpringContextTests {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @BeforeMethod
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * Verifies a filter applied by {@link JsonFilter}.
     */
    public void testJsonFilter() throws Exception {
        mockMvc.perform(get("/testFilter"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.intProp").doesNotExist())
                .andExpect(jsonPath("$.longProp").exists())
                .andExpect(jsonPath("$.stringProp").doesNotExist())
                .andExpect(jsonPath("$.bean").doesNotExist());
    }

    /**
     * Verifies a filter for several class at once.
     */
    public void testJsonFilterChild() throws Exception {
        mockMvc.perform(get("/testFilterChild"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.intProp").doesNotExist())
                .andExpect(jsonPath("$.longProp").exists())
                .andExpect(jsonPath("$.stringProp").doesNotExist())
                .andExpect(jsonPath("$.bean").exists())
                .andExpect(jsonPath("$.bean.id").exists())
                .andExpect(jsonPath("$.bean.test").exists())
                .andExpect(jsonPath("$.bean.test2").doesNotExist());
    }

    /**
     * Verifies serialization without filters.
     */
    public void testWithoutJsonFilter() throws Exception {
        mockMvc.perform(get("/testWithoutFilters"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.intProp").exists())
                .andExpect(jsonPath("$.longProp").exists())
                .andExpect(jsonPath("$.stringProp").doesNotExist())
                .andExpect(jsonPath("$.bean").exists())
                .andExpect(jsonPath("$.bean.id").exists())
                .andExpect(jsonPath("$.bean.test").exists())
                .andExpect(jsonPath("$.bean.test2").exists());
    }

    /**
     * Verifies serialization of a {@link java.util.Map}.
     */
    public void testJsonFilterMap() throws Exception {
        mockMvc.perform(get("/testFilterMap"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.bean.id").exists())
                .andExpect(jsonPath("$.bean.intProp").exists())
                .andExpect(jsonPath("$.bean.longProp").doesNotExist())
                .andExpect(jsonPath("$.bean.stringProp").doesNotExist())
                .andExpect(jsonPath("$.bean.bean").doesNotExist());
    }

    /**
     * Verifies serialization of a {@link java.util.Collection}.
     */
    public void testJsonFilterCollection() throws Exception {
        mockMvc.perform(get("/testFilterCollection"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].intProp").exists())
                .andExpect(jsonPath("$[0].longProp").exists())
                .andExpect(jsonPath("$[0].stringProp").doesNotExist())
                .andExpect(jsonPath("$[0].bean").exists())
                .andExpect(jsonPath("$[0].bean.id").exists())
                .andExpect(jsonPath("$[0].bean.test").doesNotExist())
                .andExpect(jsonPath("$[0].bean.test2").doesNotExist());
    }

    /**
     * Verifies serialization of an array.
     */
    public void testJsonFilterArray() throws Exception {
        mockMvc.perform(get("/testFilterArray"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].id").doesNotExist())
                .andExpect(jsonPath("$[0].intProp").doesNotExist())
                .andExpect(jsonPath("$[0].longProp").doesNotExist())
                .andExpect(jsonPath("$[0].stringProp").doesNotExist())
                .andExpect(jsonPath("$[0].bean").doesNotExist())
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].test").doesNotExist())
                .andExpect(jsonPath("$[1].test2").doesNotExist());
    }

    @EnableWebMvc
    @Configuration
    @ComponentScan(basePackageClasses = JsonFilterIntegrationTests.class)
    public static class ContextConfig extends WebMvcConfigurerAdapter {
        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                    .annotationIntrospector(new JsonFilterAnnotationIntrospector())
                    .filters(new SimpleFilterProvider().setFailOnUnknownId(false))
                    .build();
            converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
        }
    }
}
