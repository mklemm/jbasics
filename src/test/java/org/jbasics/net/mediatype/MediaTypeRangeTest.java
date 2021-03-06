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
package org.jbasics.net.mediatype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.jbasics.testing.Java14LoggingTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * JUnit text for the {@link MediaTypeRange}.
 * 
 * @author Stephan Schloepke
 */
@RunWith(Parameterized.class)
public class MediaTypeRangeTest extends Java14LoggingTestCase {

	/**
	 * JUnit test parameters.
	 * 
	 * @return The collection of test parameters.
	 */
	@SuppressWarnings("boxing")
	@Parameters
	public static Collection<?> parameters() {
		return Arrays.asList(new Object[][] { // Test Cases
		        { "text/html; charset=iso-8859-1", "text", "html", "charset", "iso-8859-1", false }, // Test case
		                { "text/*; charset=iso-8859-1", "text", "*", "charset", "iso-8859-1", false }, // Test case
		                { "*/*; charset=iso-8859-1", "*", "*", "charset", "iso-8859-1", false }, // Test case
		                { "text/xml", "text", "xml", null, null, false }, // Test case
		                { "application/xhtml+xml", "application", "xhtml+xml", null, null, false }, // Test case
		                { "text /html; charset=iso-8859-1", "text", "html", "charset", "iso-8859-1", true }, // Test case
		                { "text/ html; charset=iso-8859-1", "text", "html", "charset", "iso-8859-1", true }, // Test case
		                { "application/rating+xml; version=1 3.56", "application", "rating+xml", "version", "1 3.56", true }, // Test case
		                { "application/rating+xml; version=1/3.56", "application", "rating+xml", "version", "1/3.56", false }, // Test case
		        });
	}

	private final String stringType;
	private final String expectedType;
	private final String expectedSubType;
	private final String expectedParamKey;
	private final String expectedParamValue;
	private final boolean exceptionExpected;

	/**
	 * Creating the test with the given test parameters.
	 * 
	 * @param stringType The string to test.
	 * @param type The expected type.
	 * @param subType The expected sub type.
	 * @param parameter The expected parameter name.
	 * @param value The expected parameter value.
	 * @param exceptionExpected True if an {@link IllegalArgumentException} is expected.
	 */
	public MediaTypeRangeTest(final String stringType, final String type, final String subType, final String parameter, final String value,
	        final boolean exceptionExpected) {
		this.stringType = stringType;
		this.expectedType = type;
		this.expectedSubType = subType;
		this.expectedParamKey = parameter;
		this.expectedParamValue = value;
		this.exceptionExpected = exceptionExpected;
	}

	/**
	 * Run the test with the given parameters of the constructor.
	 */
	@Test
	public void testMediaTypeRangeParsing() {
		try {
			this.logger.entering(getClass().getName(), "testMediaTypeRangeParsing");
			this.logger.info("Testing: " + this.stringType);
			MediaTypeRange mediaTypeRange = MediaTypeRange.valueOf(this.stringType);
			assertFalse(this.exceptionExpected);
			assertEquals(this.expectedType, mediaTypeRange.getType());
			assertEquals(this.expectedSubType, mediaTypeRange.getSubType());
			if (this.expectedParamKey != null) {
				assertEquals(this.expectedParamValue, mediaTypeRange.getParameter(this.expectedParamKey));
			}
		} catch (IllegalArgumentException e) {
			assertTrue(this.exceptionExpected);
		} finally {
			this.logger.exiting(getClass().getName(), "testMediaTypeRangeParsing");
		}
	}
}
