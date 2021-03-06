/*
 * Copyright (c) 2009 Stephan Schloepke and innoQ Deutschland GmbH
 *
 * Stephan Schloepke: http://www.schloepke.de/
 * innoQ Deutschland GmbH: http://www.innoq.com/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jbasics.arrays;

import java.lang.reflect.Array;
import java.net.URI;
import java.net.URL;
import java.util.Date;

import org.jbasics.annotation.ImmutableState;
import org.jbasics.annotation.ThreadSafe;
import org.jbasics.checker.ContractCheck;
import org.jbasics.checker.ContractViolationException;

/**
 * Often used constant arrays.
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
@ThreadSafe
@ImmutableState
public final class ArrayConstants {
	/**
	 * A zero length boolean array constant.
	 * 
	 * @since 1.0
	 */
	public static final boolean[] ZERO_LENGTH_BOOLEAN_ARRAY = new boolean[0];
	/**
	 * A zero length {@link Boolean} array constant.
	 * 
	 * @since 1.0
	 */
	public static final Boolean[] ZERO_LENGTH_BOOLEAN_TYPE_ARRAY = new Boolean[0];
	/**
	 * A zero length byte array constant.
	 * 
	 * @since 1.0
	 */
	public static final byte[] ZERO_LENGTH_BYTE_ARRAY = new byte[0];
	/**
	 * A zero length {@link Byte} array constant.
	 * 
	 * @since 1.0
	 */
	public static final Byte[] ZERO_LENGTH_BYTE_TYPE_ARRAY = new Byte[0];
	/**
	 * A zero length short array constant.
	 * 
	 * @since 1.0
	 */
	public static final short[] ZERO_LENGTH_SHORT_ARRAY = new short[0];
	/**
	 * A zero length {@link Short} array constant.
	 * 
	 * @since 1.0
	 */
	public static final Short[] ZERO_LENGTH_SHORT_TYPE_ARRAY = new Short[0];
	/**
	 * A zero length int array constant.
	 * 
	 * @since 1.0
	 */
	public static final int[] ZERO_LENGTH_INT_ARRAY = new int[0];
	/**
	 * A zero length {@link Integer} array constant.
	 * 
	 * @since 1.0
	 */
	public static final Integer[] ZERO_LENGTH_INTEGER_TYPE_ARRAY = new Integer[0];
	/**
	 * A zero length long array constant.
	 * 
	 * @since 1.0
	 */
	public static final long[] ZERO_LENGTH_LONG_ARRAY = new long[0];
	/**
	 * A zero length {@link Long} array constant.
	 * 
	 * @since 1.0
	 */
	public static final Long[] ZERO_LENGTH_LONG_TYPE_ARRAY = new Long[0];
	/**
	 * A zero length float array constant.
	 * 
	 * @since 1.0
	 */
	public static final float[] ZERO_LENGTH_FLOAT_ARRAY = new float[0];
	/**
	 * A zero length {@link Float} array constant.
	 * 
	 * @since 1.0
	 */
	public static final Float[] ZERO_LENGTH_FLOAT_TYPE_ARRAY = new Float[0];
	/**
	 * A zero length double array constant.
	 * 
	 * @since 1.0
	 */
	public static final double[] ZERO_LENGTH_DOUBLE_ARRAY = new double[0];
	/**
	 * A zero length {@link Double} array constant.
	 * 
	 * @since 1.0
	 */
	public static final Double[] ZERO_LENGTH_DOUBLE_TYPE_ARRAY = new Double[0];
	/**
	 * A zero length {@link Number} array constant.
	 * 
	 * @since 1.0
	 */
	public static final Number[] ZERO_LENGTH_NUMBER_ARRAY = new Number[0];
	/**
	 * A zero length {@link String} array constant.
	 * 
	 * @since 1.0
	 */
	public static final String[] ZERO_LENGTH_STRING_ARRAY = new String[0];
	/**
	 * A zero length {@link Date} array constant.
	 * 
	 * @since 1.0
	 */
	public static final Date[] ZERO_LENGTH_DATE_ARRAY = new Date[0];
	/**
	 * A zero length {@link URI} array constant.
	 * 
	 * @since 1.0
	 */
	public static final URI[] ZERO_LENGTH_URI_ARRAY = new URI[0];
	/**
	 * A zero length {@link URL} array constant.
	 * 
	 * @since 1.0
	 */
	public static final URL[] ZERO_LENGTH_URL_ARRAY = new URL[0];
	/**
	 * A zero length {@link Object} array constant.
	 * 
	 * @since 1.0
	 */
	public static final Object[] ZERO_LENGTH_OBJECT_ARRAY = new Object[0];

	/**
	 * Creates a zero length array usable as a constant for the given type.
	 * 
	 * @param type
	 *            The type of the array to create (MUST not be null)
	 * @return A a newly created zero length array of the given type.
	 * @throws ContractViolationException
	 *             if type is null.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] createZeroLengthArrayForType(final Class<T> type) {
		return (T[]) Array.newInstance(ContractCheck.mustNotBeNull(type, "type"), 0); //$NON-NLS-1$
	}

	/**
	 * Private constructor to hinder instantiation of this constants collection.
	 */
	protected ArrayConstants() {
		// To hinder anyone to instantiate this constant class.
	}
}
