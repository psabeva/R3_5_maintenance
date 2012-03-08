/*******************************************************************************
 * Copyright (c) 2007, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.resource.java;

import org.eclipse.jpt.common.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.jpa.core.resource.java.PrimaryKeyJoinColumnAnnotation;

/**
 * <code>javax.persistence.PrimaryKeyJoinColumn</code>
 */
public final class NullPrimaryKeyJoinColumnAnnotation
	extends NullNamedColumnAnnotation<PrimaryKeyJoinColumnAnnotation>
	implements PrimaryKeyJoinColumnAnnotation
{	
	public NullPrimaryKeyJoinColumnAnnotation(JavaResourceNode parent) {
		super(parent);
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	// ***** referenced column name
	public String getReferencedColumnName() {
		return null;
	}

	public void setReferencedColumnName(String referencedColumnName) {
		throw new UnsupportedOperationException();
	}

	public TextRange getReferencedColumnNameTextRange() {
		return null;
	}
	
	public boolean referencedColumnNameTouches(int pos) {
		return false;
	}
}
