/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.internal.resource.java.source;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.common.core.internal.resource.java.source.SourceAnnotation;
import org.eclipse.jpt.common.core.internal.utility.jdt.AnnotatedElementAnnotationElementAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.EnumDeclarationAnnotationElementAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAnnotatedElement;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.core.utility.jdt.AnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.AnnotationElementAdapter;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationElementAdapter;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLink;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkJoinFetchAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.JoinFetchType;

/**
 * <code>org.eclipse.persistence.annotations.JoinFetch</code>
 */
public final class SourceEclipseLinkJoinFetchAnnotation
	extends SourceAnnotation
	implements EclipseLinkJoinFetchAnnotation
{
	private static final DeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER = new SimpleDeclarationAnnotationAdapter(ANNOTATION_NAME);

	private static final DeclarationAnnotationElementAdapter<String> VALUE_ADAPTER = buildValueAdapter();
	private final AnnotationElementAdapter<String> valueAdapter;
	private JoinFetchType value;
	private TextRange valueTextRange;


	public SourceEclipseLinkJoinFetchAnnotation(JavaResourceAnnotatedElement parent, AnnotatedElement element) {
		super(parent, element, DECLARATION_ANNOTATION_ADAPTER);
		this.valueAdapter = new AnnotatedElementAnnotationElementAdapter<String>(element, VALUE_ADAPTER);
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	public void initialize(CompilationUnit astRoot) {
		this.value = this.buildValue(astRoot);
		this.valueTextRange = this.buildValueTextRange(astRoot);
	}

	public void synchronizeWith(CompilationUnit astRoot) {
		this.syncValue(this.buildValue(astRoot));
		this.valueTextRange = this.buildValueTextRange(astRoot);
	}

	@Override
	public boolean isUnset() {
		return super.isUnset() &&
				(this.value == null);
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.value);
	}


	// ********** JoinFetchAnnotation implementation **********

	// ***** value
	public JoinFetchType getValue() {
		return this.value;
	}

	public void setValue(JoinFetchType value) {
		if (this.attributeValueHasChanged(this.value, value)) {
			this.value = value;
			this.valueAdapter.setValue(JoinFetchType.toJavaAnnotationValue(value));
		}
	}

	private void syncValue(JoinFetchType astValue) {
		JoinFetchType old = this.value;
		this.value = astValue;
		this.firePropertyChanged(VALUE_PROPERTY, old, astValue);
	}

	private JoinFetchType buildValue(CompilationUnit astRoot) {
		return JoinFetchType.fromJavaAnnotationValue(this.valueAdapter.getValue(astRoot));
	}

	public TextRange getValueTextRange() {
		return this.valueTextRange;
	}

	private TextRange buildValueTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(VALUE_ADAPTER, astRoot);
	}


	// ********** static methods **********

	private static DeclarationAnnotationElementAdapter<String> buildValueAdapter() {
		return new EnumDeclarationAnnotationElementAdapter(DECLARATION_ANNOTATION_ADAPTER, EclipseLink.JOIN_FETCH__VALUE);
	}

}
