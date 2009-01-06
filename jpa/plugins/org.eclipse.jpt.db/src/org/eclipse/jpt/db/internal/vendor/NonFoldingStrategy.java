/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.db.internal.vendor;

import org.eclipse.jpt.utility.internal.ClassTools;

/**
 * Do not fold "normal" identifiers.
 * Respect the case of "normal" identifiers.
 */
class NonFoldingStrategy
	implements FoldingStrategy
{

	// singleton
	private static final FoldingStrategy INSTANCE = new NonFoldingStrategy();

	/**
	 * Return the singleton.
	 */
	static FoldingStrategy instance() {
		return INSTANCE;
	}

	/**
	 * Ensure single instance.
	 */
	private NonFoldingStrategy() {
		super();
	}

	/**
	 * Since identifiers are not folded to upper- or lower-case, the name is
	 * already "folded".
	 */
	public String fold(String name) {
		return name;
	}

	/**
	 * Since identifiers are not folded to upper- or lower-case, the name is
	 * already "folded".
	 * (Non-folding databases do not require delimiters around mixed-case
	 * "normal" identifiers.)
	 */
	public boolean nameIsFolded(String name) {
		return true;
	}

	public boolean normalIdentifiersAreCaseSensitive() {
		return true;
	}

	@Override
	public String toString() {
		return ClassTools.toStringClassNameForObject(this);
	}

}
