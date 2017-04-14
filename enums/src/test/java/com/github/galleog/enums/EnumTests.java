package com.github.galleog.enums;

import org.apache.commons.lang3.SerializationUtils;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Enum}.
 *
 * @author Oleg Galkin
 */
public class EnumTests {
	/**
	 * Test for {@link Enum#compareTo(Enum)}.
	 */
	@Test
	public void testCompareTo() {
		assertThat(ColorEnum.BLUE.compareTo(ColorEnum.BLUE), is(0));
		assertThat(ColorEnum.RED.compareTo(ColorEnum.BLUE), greaterThan(0));
		assertThat(ColorEnum.BLUE.compareTo(ColorEnum.RED), lessThan(0));

		try {
			ColorEnum.RED.compareTo(null);
			fail("NullPointerException must be thrown");
		} catch (NullPointerException ignore) {
		}
		try {
			ColorEnum.RED.compareTo(GreekLetterEnum.ALPHA);
			fail("ClassCastException must be thrown");
		} catch (ClassCastException ignore) {
		}
	}

	/**
	 * Test for {@link Enum#equals(Object)}.
	 */
	@Test
	public void testEquals() {
		assertThat(ColorEnum.RED, is(ColorEnum.RED));
		assertThat(ColorEnum.valueOf("Red"), is(ColorEnum.RED));
		assertThat(ColorEnum.RED, not(ColorEnum.BLUE));
		assertThat(GreekLetterEnum.ALPHA, is(ExtendedGreekLetterEnum.ALPHA));
		assertThat(ExtendedGreekLetterEnum.valueOf("Alpha"), is(GreekLetterEnum.ALPHA));
		assertThat(ExtendedGreekLetterEnum.GAMMA, is(ExtendedGreekLetterEnum.GAMMA));
		assertThat(OperationEnum.PLUS, is(OperationEnum.PLUS));
		assertThat(OperationEnum.valueOf("Minus"), is(OperationEnum.MINUS));
		assertThat(OperationEnum.PLUS, not(OperationEnum.MINUS));
		assertThat(ColorEnum.RED, Matchers.<Enum<String>>not(GreekLetterEnum.ALPHA));
	}

	/**
	 * Test for {@link Enum#hashCode}.
	 */
	@Test
	public void testHashCode() {
		assertThat(ColorEnum.RED.hashCode(), is(ColorEnum.RED.hashCode()));
		assertThat(ColorEnum.valueOf("Red").hashCode(), is(ColorEnum.RED.hashCode()));
		assertThat(GreekLetterEnum.ALPHA.hashCode(), is(ExtendedGreekLetterEnum.ALPHA.hashCode()));
		assertThat(ExtendedGreekLetterEnum.valueOf("Alpha").hashCode(), is(GreekLetterEnum.ALPHA.hashCode()));
	}

	/**
	 * Test for {@link Enum#valueOf(Class, Object)}.
	 */
	@Test
	public void testValueOf() {
		assertThat(ColorEnum.valueOf("Red"), sameInstance(ColorEnum.RED));
		assertThat(ColorEnum.valueOf("Blue"), sameInstance(ColorEnum.BLUE));
		assertThat(ColorEnum.valueOf("Green"), sameInstance(ColorEnum.GREEN));
		try {
			ColorEnum.valueOf("Pink");
			fail("IllegalArgumentException must be thrown");
		} catch (IllegalArgumentException ignore) {
		}

		assertThat(OperationEnum.valueOf("Plus"), sameInstance(OperationEnum.PLUS));
		assertThat(OperationEnum.valueOf("Minus"), sameInstance(OperationEnum.MINUS));
		try {
			OperationEnum.valueOf("Multiply");
			fail("IllegalArgumentException must be thrown");
		} catch (IllegalArgumentException ignore) {
		}

		assertThat(GreekLetterEnum.valueOf("Alpha"), sameInstance(GreekLetterEnum.ALPHA));
		assertThat(GreekLetterEnum.valueOf("Beta"), sameInstance(GreekLetterEnum.BETA));
		try {
			GreekLetterEnum.valueOf("Gamma");
			fail("IllegalArgumentException must be thrown");
		} catch (IllegalArgumentException ignore) {
		}

		assertThat(ExtendedGreekLetterEnum.valueOf("Alpha"), sameInstance(GreekLetterEnum.ALPHA));
		assertThat(ExtendedGreekLetterEnum.valueOf("Beta"), sameInstance(GreekLetterEnum.BETA));
		assertThat(ExtendedGreekLetterEnum.valueOf("Gamma"), sameInstance(ExtendedGreekLetterEnum.GAMMA));
		try {
			ExtendedGreekLetterEnum.valueOf("Delta");
			fail("IllegalArgumentException must be thrown");
		} catch (IllegalArgumentException ignore) {
		}
	}

	/**
	 * Test for {@link Enum#values(Class)}.
	 */
	@Test
	public void testValues() {
		assertThat(ColorEnum.values(), containsInAnyOrder(ColorEnum.RED, ColorEnum.BLUE, ColorEnum.GREEN));
		assertThat(OperationEnum.values(), containsInAnyOrder(OperationEnum.PLUS, OperationEnum.MINUS));
		assertThat(GreekLetterEnum.values(), containsInAnyOrder(GreekLetterEnum.ALPHA, GreekLetterEnum.BETA));
		assertThat(Enum.values(ExtendedGreekLetterEnum.class),
				containsInAnyOrder(GreekLetterEnum.ALPHA, GreekLetterEnum.BETA, ExtendedGreekLetterEnum.GAMMA));
	}

	/**
	 * Test for serialization/deserialization.
	 */
	@Test
	public void testSerialization() {
		assertThat(SerializationUtils.clone(ColorEnum.RED), sameInstance(ColorEnum.RED));
		assertThat(SerializationUtils.clone(ColorEnum.BLUE), sameInstance(ColorEnum.BLUE));
		assertThat(SerializationUtils.clone(ColorEnum.GREEN), sameInstance(ColorEnum.GREEN));
		assertThat(SerializationUtils.clone(OperationEnum.PLUS), sameInstance(OperationEnum.PLUS));
		assertThat(SerializationUtils.clone(OperationEnum.MINUS), sameInstance(OperationEnum.MINUS));
		assertThat(SerializationUtils.clone(GreekLetterEnum.ALPHA), sameInstance(GreekLetterEnum.ALPHA));
		assertThat(SerializationUtils.clone(GreekLetterEnum.BETA), sameInstance(GreekLetterEnum.BETA));
		assertThat(SerializationUtils.clone(ExtendedGreekLetterEnum.ALPHA), sameInstance(GreekLetterEnum.ALPHA));
		assertThat(SerializationUtils.clone(ExtendedGreekLetterEnum.BETA), sameInstance(GreekLetterEnum.BETA));
		assertThat(SerializationUtils.clone(ExtendedGreekLetterEnum.GAMMA),
				sameInstance(ExtendedGreekLetterEnum.GAMMA));
	}

	/**
	 * Test for a duplicated key.
	 */
	@Test
	public void testDuplicatedKey() {
		try {
			DuplicatedKeyEnum.GREEN.getKey();
			fail("ExceptionInInitializerError must be thrown");
		} catch (ExceptionInInitializerError e) {
			assertThat(e.getException(), instanceOf(IllegalStateException.class));
		}
	}

	/**
	 * Test for {@link Enum#getEnumClass()} that returns {@code null}.
	 */
	@Test
	public void testNullClassName() {
		try {
			NullClassEnum.PLUS.getKey();
			fail("ExceptionInInitializerError must be thrown");
		} catch (ExceptionInInitializerError e) {
			assertThat(e.getException(), instanceOf(IllegalStateException.class));
		}
	}

	/**
	 * Test for invalid {@link Enum#getEnumClass()}.
	 */
	@Test
	public void testInvalidClassName() {
		try {
			InvalidClassEnum.PLUS.getKey();
			fail("ExceptionInInitializerError must be thrown");
		} catch (ExceptionInInitializerError e) {
			assertThat(e.getException(), instanceOf(IllegalStateException.class));
		}
	}
}
