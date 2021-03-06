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
package org.jbasics.types.builders;

import java.util.Collections;
import java.util.Map;

import org.jbasics.checker.ContractCheck;
import org.jbasics.pattern.builder.Builder;
import org.jbasics.pattern.factory.Factory;
import org.jbasics.types.factories.MapFactory;
import org.jbasics.types.tuples.Pair;

public class MapBuilder<K, V> implements Builder<Map<K, V>> {
	private final Factory<Map<K, V>> mapFactory;
	private boolean mutable = false;
	private final Map<K, V> map;

	public MapBuilder() {
		this.mapFactory = MapFactory.unorderedMapFactory();
		this.map = this.mapFactory.newInstance();
	}

	public MapBuilder(final Factory<Map<K, V>> mapFactory) {
		this.mapFactory = ContractCheck.mustNotBeNull(mapFactory, "mapFactory");
		this.map = this.mapFactory.newInstance();
	}

	public MapBuilder<K, V> put(final K key, final V value) {
		this.map.put(key, value);
		return this;
	}

	public MapBuilder<K, V> put(final Pair<K, V> keyValuePair) {
		this.map.put(keyValuePair.first(), keyValuePair.second());
		return this;
	}

	public MapBuilder<K, V> putAll(final Map<? extends K, ? extends V> map) {
		this.map.putAll(map);
		return this;
	}

	public MapBuilder<K, V> putAll(final Pair<K, V>... keyValuePairs) {
		for (Pair<K, V> keyValuePair : keyValuePairs) {
			this.map.put(keyValuePair.first(), keyValuePair.second());
		}
		return this;
	}

	public MapBuilder<K, V> mutable() {
		this.mutable = true;
		return this;
	}

	public MapBuilder<K, V> immutable() {
		this.mutable = false;
		return this;
	}

	public Map<K, V> build() {
		Map<K, V> result = this.mapFactory.newInstance();
		result.putAll(this.map);
		return this.mutable ? result : Collections.unmodifiableMap(result);
	}

	public void reset() {
		this.map.clear();
	}

}
