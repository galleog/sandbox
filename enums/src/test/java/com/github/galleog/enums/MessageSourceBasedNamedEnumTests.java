package com.github.galleog.enums;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests for {@link MessageSourceBasedNamedEnum}.
 *
 * @author Oleg Galkin
 */
@Test
public class MessageSourceBasedNamedEnumTests {
    @DataProvider(name = "enums")
    public Object[][] getEnums() {
        return new Object[][]{
                {ComplexKeyEnum.DIGIT},
                {ComplexKeyEnum.WHITE_SPACE},
                {ComplexKeyEnum.EQUAL_SIGN},
                {ComplexKeyEnum.COLON},
                {ComplexKeyEnum.UNICODE},
                // Должны определяться имена у констант из разных классов
                {MessageSourceTestEnum.TEST},
                {MessageSourceTestEnum.SAMPLE}
        };
    }

    /**
     * Test for {@link MessageSourceBasedNamedEnum#getName()}.
     */
    @Test(dataProvider = "enums")
    public void testGetName(MessageSourceBasedNamedEnum value) {
        assertThat(value.getName(), is("name of '" + value.getKey() + "'"));
    }
}