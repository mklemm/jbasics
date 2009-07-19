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

/**
 * Often used constant arrays.
 * 
 * @author Stephan Schloepke
 * @since 1.0
 */
public final class ArrayConstants {
	/**
	 * A zero length byte array constant.
	 * 
	 * @since 1.0
	 */
	public static final byte[] ZERO_LENGTH_BYTE_ARRAY = new byte[0];
	/**
	 * A zero length short array constant.
	 * 
	 * @since 1.0
	 */
	public static final short[] ZERO_LENGTH_SHORT_ARRAY = new short[0];
	/**
	 * A zero length integer array constant.
	 * 
	 * @since 1.0
	 */
	public static final int[] ZERO_LENGTH_INT_ARRAY = new int[0];
	/**
	 * A zero length long array constant.
	 * 
	 * @since 1.0
	 */
	public static final long[] ZERO_LENGTH_LONG_ARRAY = new long[0];

	private ArrayConstants() {
		// To avoid accidental instantiation.
	}

}