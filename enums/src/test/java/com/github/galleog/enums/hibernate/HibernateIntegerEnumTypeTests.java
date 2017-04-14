package com.github.galleog.enums.hibernate;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;

/**
 * Tests for {@link HibernateIntegerEnumType}.
 *
 * @author Oleg Galkin
 */
@Test
@ContextConfiguration("datasource.xml")
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class
})
@DbUnitConfiguration(databaseConnection = "databaseConnection")
public class HibernateIntegerEnumTypeTests extends AbstractTestNGSpringContextTests {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private TransactionTemplate transactionTemplate;

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        em = null;
        transactionTemplate = null;
        applicationContext = null;
    }

    /**
     * Test to read and write enumeration values.
     */
    @DatabaseSetup("HibernateIntegerEnumTypeTests-setup-dataset.xml")
    @ExpectedDatabase(
            value = "HibernateIntegerEnumTypeTests-expected-dataset.xml",
            assertionMode = DatabaseAssertionMode.NON_STRICT
    )
    public void testHibernateType() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                IntegerEnumEntity entity = em.find(IntegerEnumEntity.class, 1);
                assertThat(entity.getEnumValue(), sameInstance(IntegerNumberEnum.ONE));

                entity.setEnumValue(IntegerNumberEnum.TWO);
                em.merge(entity);
            }
        });
    }
}