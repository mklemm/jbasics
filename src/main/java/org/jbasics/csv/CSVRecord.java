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
package org.jbasics.csv;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.jbasics.arrays.ArrayConstants;
import org.jbasics.arrays.unstable.ArrayIterator;
import org.jbasics.pattern.container.Indexed;

public class CSVRecord implements Iterable<String>, Indexed<String> {
	private final String[] fields;

	public CSVRecord(final List<String> fields) {
		if (fields == null || fields.isEmpty()) {
			this.fields = ArrayConstants.ZERO_LENGTH_STRING_ARRAY;
		} else {
			this.fields = fields.toArray(new String[fields.size()]);
		}
	}

	public CSVRecord(final String... fields) {
		if (fields == null || fields.length == 0) {
			this.fields = ArrayConstants.ZERO_LENGTH_STRING_ARRAY;
		} else {
			this.fields = new String[fields.length];
			System.arraycopy(fields, 0, this.fields, 0, fields.length);
		}
	}

	public Iterator<String> iterator() {
		return new ArrayIterator<String>(this.fields);
	}

	public int size() {
		return this.fields.length;
	}

	public String getField(final int index) {
		return this.fields[index];
	}

	public Appendable append(final Appendable appendable, final char separator) throws IOException {
		boolean delim = false;
		for (final String field : this.fields) {
			if (delim) {
				appendable.append(separator);
			} else {
				delim = true;
			}
			appendValue(field, appendable, separator);
		}
		return appendable;
	}

	@Override
	public String toString() {
		return Arrays.toString(this.fields);
	}

	private Appendable appendValue(final String value, final Appendable appendable, final char separator) throws IOException {
		if (value != null) {
			final StringBuilder t = new StringBuilder();
			boolean quote = false;
			for (int i = 0; i < value.length(); i++) {
				final char c = value.charAt(i);
				t.append(c);
				if (c == separator) {
					quote = true;
				} else {
					switch (c) {
						case '"':
							t.append('"');
						case ',':
						case '\n':
						case '\r':
							quote = true;
							break;
					}
				}
			}
			if (quote) {
				appendable.append('"').append(t).append('"');
			} else {
				appendable.append(t);
			}
		}
		return appendable;
	}

	@Override
	public String getElementAtIndex(final int index) {
		return getField(index);
	}

}
