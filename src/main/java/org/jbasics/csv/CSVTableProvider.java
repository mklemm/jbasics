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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.jbasics.configuration.properties.BooleanValueTypeFactory;
import org.jbasics.configuration.properties.SystemProperty;
import org.jbasics.utilities.DataUtilities;

@Provider
public class CSVTableProvider implements MessageBodyReader<CSVTable> {
	private final Logger logger = Logger.getLogger(CSVTableProvider.class.getName());

	public static final String USE_SEMICOLON_AS_STANDARD_PROPERTY = "org.jbasics.csv.CSVTableProvider.invertAlternateSeparator";
	public static final String GERMAN_GUESS_PROPERTY = "org.jbasics.csv.CSVTableProvider.germanUseSemicolonSeparator";
	public static final String AUTO_GUESS_PROPERTY = "org.jbasics.csv.CSVTableProvider.separatorAutoGuessing";

	public static final SystemProperty<Boolean> USE_GERMAN_SEPARATOR_DETECTION = SystemProperty.booleanProperty(CSVTableProvider.GERMAN_GUESS_PROPERTY, Boolean.FALSE);
	public static final SystemProperty<Boolean> USE_SEPARATOR_AUTO_GUESS = SystemProperty.booleanProperty(CSVTableProvider.AUTO_GUESS_PROPERTY, Boolean.FALSE);
	public static final SystemProperty<Boolean> INVERT_ALTERNATE_SEPARATOR = SystemProperty.booleanProperty(CSVTableProvider.USE_SEMICOLON_AS_STANDARD_PROPERTY, Boolean.FALSE);

	@Override
	public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
		return CSVTable.class.isAssignableFrom(type) && mediaType.isCompatible(MediaType.valueOf(CSVTable.RFC4180_MEDIA_TYPE.toString()));
	}

	@Override
	public CSVTable readFrom(final Class<CSVTable> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream) throws IOException, WebApplicationException {
		final Charset charset = Charset.forName(DataUtilities.coalesce(mediaType.getParameters().get("charset"), "ISO-8859-15")); //$NON-NLS-1$ //$NON-NLS-2$
		final Reader r = new BufferedReader(new InputStreamReader(entityStream, charset), 16384);
		final boolean headerPresent = CSVTable.HEADER_PRESENT.right().equalsIgnoreCase(mediaType.getParameters().get(CSVTable.HEADER_PRESENT.first()));
		boolean useAlternateSeparator = CSVTableProvider.INVERT_ALTERNATE_SEPARATOR.value().booleanValue();
		final String temp = mediaType.getParameters().get("use-alternate-separator"); //$NON-NLS-1$
		if (temp != null) {
			if (BooleanValueTypeFactory.SHARED_INSTANCE.create(temp).booleanValue()) {
				useAlternateSeparator = !useAlternateSeparator;
			}
		} else if (CSVTableProvider.USE_GERMAN_SEPARATOR_DETECTION.value().booleanValue() && DataUtilities.coalesce(httpHeaders.getFirst("Content-Language"), "en").equals("de")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			useAlternateSeparator = true;
		} else if (CSVTableProvider.USE_SEPARATOR_AUTO_GUESS.value().booleanValue() && r.markSupported()) {
			r.mark(8192);
			try {
				final int[] commaCount = getRecordsLength(new CSVRecordReader(r, ','), 2);
				r.reset();
				final int[] semicolonCount = getRecordsLength(new CSVRecordReader(r, ';'), 2);
				// 1. check if both are equal if not use the equal one
				if (commaCount[0] == commaCount[1]) {
					if (semicolonCount[0] == semicolonCount[1]) {
						if (commaCount[0] < semicolonCount[0]) {
							useAlternateSeparator = true;
						} else {
							useAlternateSeparator = false;
						}
					} else {
						useAlternateSeparator = false;
					}
				} else if (semicolonCount[0] == semicolonCount[1]) {
					useAlternateSeparator = true;
				} else if (headerPresent) {
					if (commaCount[0] < semicolonCount[0]) {
						useAlternateSeparator = true;
					} else {
						useAlternateSeparator = false;
					}
				}
			} catch (final Exception e) {
				this.logger.log(Level.WARNING, "Exception thrown in guessing the separator upon reading the first lines", e); //$NON-NLS-1$
			} finally {
				r.reset();
			}
		}
		final CSVParser p = new CSVParser(headerPresent, useAlternateSeparator ? ';' : ',', true);
		return p.parse(r);
	}

	private int[] getRecordsLength(final CSVRecordReader reader, final int lines) throws IOException {
		assert lines > 0 && reader != null;
		final int[] result = new int[lines];
		for (int i = 0; i < lines; i++) {
			final CSVRecord temp = reader.readNext();
			result[i] = temp == null ? -1 : temp.size();
		}
		return result;
	}

}
