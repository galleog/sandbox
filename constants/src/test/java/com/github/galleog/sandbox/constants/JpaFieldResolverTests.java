package com.github.galleog.sandbox.constants;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.util.ReflectionUtils;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests for {@link JpaFieldResolver}.
 *
 * @author Oleg Galkin
 */
@Test
@ContextConfiguration(classes = JpaFieldResolverTests.ContextConfig.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
})
@DbUnitConfiguration(databaseConnection = "databaseConnection")
public class JpaFieldResolverTests extends AbstractTestNGSpringContextTests {
    @Autowired
    private FieldResolver fieldResolver;

    /**
     * Test for {@link JpaFieldResolver#resolve(java.lang.reflect.Field)}.
     */
    @DatabaseSetup("dataset.xml")
    public void testResolve() {
        Country algeria = (Country) fieldResolver.resolve(ReflectionUtils.findField(Country.class, "ALGERIA"));
        assertThat(algeria.getId(), is("DZ"));
        assertThat(algeria.getName(), is("Algeria"));

        Country bahrain = (Country) fieldResolver.resolve(ReflectionUtils.findField(Country.class, "BAHRAIN"));
        assertThat(bahrain.getId(), is("BH"));
        assertThat(bahrain.getName(), is("Bahrain"));
    }

    @Configuration
    @ImportResource("classpath:datasource.xml")
    public static class ContextConfig {
        @Bean
        public FieldResolver fieldResolver() {
            return new JpaFieldResolver();
        }
    }
}