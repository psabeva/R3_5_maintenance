/*******************************************************************************
 * Copyright (c) 2007, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.resource.java.source;

import org.eclipse.jpt.common.core.internal.utility.jdt.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.AnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.jpa.core.resource.java.EnumeratedAnnotation;
import org.eclipse.jpt.jpa.core.resource.java.JPA;

/**
 * <code>javax.persistence.Enumerated</code>
 */
public final class SourceEnumeratedAnnotation
	extends SourceBaseEnumeratedAnnotation
	implements EnumeratedAnnotation
{
	private static final DeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER = new SimpleDeclarationAnnotationAdapter(ANNOTATION_NAME);	

	public SourceEnumeratedAnnotation(JavaResourceAnnotatedElement parent, AnnotatedElement element) {
		super(parent, element, DECLARATION_ANNOTATION_ADAPTER);
	}
	
	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	@Override
	protected String getValueElementName() {
		return JPA.ENUMERATED__VALUE;
	}
}
