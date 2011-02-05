/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.internal.iterables;

import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.jpt.common.utility.internal.StringTools;
import org.eclipse.jpt.common.utility.internal.iterators.ArrayIterator;

/**
 * An <code>ArrayIterable</code> provides an {@link Iterable}
 * for an array of objects of type <code>E</code>.
 * 
 * @param <E> the type of elements returned by the iterable's iterator
 * 
 * @see ArrayIterator
 * @see ArrayListIterable
 */
public class ArrayIterable<E>
	implements Iterable<E>
{
	final E[] array;
	final int start;
	final int length;

	/**
	 * Construct an iterable for the specified array.
	 */
	public ArrayIterable(E... array) {
		this(array, 0, array.length);
	}

	/**
	 * Construct an iterable for the specified array,
	 * starting at the specified start index and continuing for
	 * the rest of the array.
	 */
	public ArrayIterable(E[] array, int start) {
		this(array, start, array.length - start);
	}

	/**
	 * Construct an iterable for the specified array,
	 * starting at the specified start index and continuing for
	 * the specified length.
	 */
	public ArrayIterable(E[] array, int start, int length) {
		super();
		if ((start < 0) || (start > array.length)) {
			throw new IllegalArgumentException("start: " + start); //$NON-NLS-1$
		}
		if ((length < 0) || (length > array.length - start)) {
			throw new IllegalArgumentException("length: " + length); //$NON-NLS-1$
		}
		this.array = array;
		this.start = start;
		this.length = length;
	}

	public Iterator<E> iterator() {
		return new ArrayIterator<E>(this.array, this.start, this.length);
	}

	@Override
	public String toString() {
		return StringTools.buildToStringFor(this, Arrays.toString(this.array));
	}

}
