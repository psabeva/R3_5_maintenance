/*******************************************************************************
 * Copyright (c) 2007, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.resource.java.source;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.common.core.internal.resource.java.source.SourceAnnotation;
import org.eclipse.jpt.common.core.internal.utility.jdt.AnnotatedElementAnnotationElementAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.ConversionDeclarationAnnotationElementAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.NumberIntegerExpressionConverter;
import org.eclipse.jpt.common.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.core.utility.jdt.AnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.AnnotationElementAdapter;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationElementAdapter;
import org.eclipse.jpt.jpa.core.resource.java.GeneratorAnnotation;

/**
 * javax.persistence.SequenceGenerator
 * javax.persistence.TableGenerator
 */
abstract class SourceGeneratorAnnotation
	extends SourceAnnotation
	implements GeneratorAnnotation
{
	final DeclarationAnnotationElementAdapter<String> nameDeclarationAdapter;
	final AnnotationElementAdapter<String> nameAdapter;
	String name;
	TextRange nameTextRange;

	final DeclarationAnnotationElementAdapter<Integer> initialValueDeclarationAdapter;
	final AnnotationElementAdapter<Integer> initialValueAdapter;
	Integer initialValue;

	final DeclarationAnnotationElementAdapter<Integer> allocationSizeDeclarationAdapter;
	final AnnotationElementAdapter<Integer> allocationSizeAdapter;
	Integer allocationSize;


	SourceGeneratorAnnotation(JavaResourceNode parent, AnnotatedElement element, DeclarationAnnotationAdapter daa) {
		super(parent, element, daa);
		this.nameDeclarationAdapter = this.getNameAdapter();
		this.nameAdapter = this.buildAdapter(this.nameDeclarationAdapter);
		this.initialValueDeclarationAdapter = this.getInitialValueAdapter();
		this.initialValueAdapter = this.buildIntegerAdapter(this.initialValueDeclarationAdapter);
		this.allocationSizeDeclarationAdapter = this.getAllocationSizeAdapter();
		this.allocationSizeAdapter = this.buildIntegerAdapter(this.allocationSizeDeclarationAdapter);
	}

	protected AnnotationElementAdapter<String> buildAdapter(DeclarationAnnotationElementAdapter<String> daea) {
		return new AnnotatedElementAnnotationElementAdapter<String>(this.annotatedElement, daea);
	}

	protected AnnotationElementAdapter<Integer> buildIntegerAdapter(DeclarationAnnotationElementAdapter<Integer> daea) {
		return new AnnotatedElementAnnotationElementAdapter<Integer>(this.annotatedElement, daea);
	}

	public void initialize(CompilationUnit astRoot) {
		this.name = this.buildName(astRoot);
		this.nameTextRange = this.buildNameTextRange(astRoot);
		this.initialValue = this.buildInitialValue(astRoot);
		this.allocationSize = this.buildAllocationSize(astRoot);
	}

	public void synchronizeWith(CompilationUnit astRoot) {
		this.syncName(this.buildName(astRoot));
		this.nameTextRange = this.buildNameTextRange(astRoot);
		this.syncInitialValue(this.buildInitialValue(astRoot));
		this.syncAllocationSize(this.buildAllocationSize(astRoot));
	}

	@Override
	public boolean isUnset() {
		return super.isUnset() &&
				(this.name == null) &&
				(this.initialValue == null) &&
				(this.allocationSize == null);
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.name);
	}


	// ********** GeneratorAnnotation implementation **********

	// ***** name
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		if (this.attributeValueHasChanged(this.name, name)) {
			this.name = name;
			this.nameAdapter.setValue(name);
		}
	}

	private void syncName(String astName) {
		String old = this.name;
		this.name = astName;
		this.firePropertyChanged(NAME_PROPERTY, old, astName);
	}

	private String buildName(CompilationUnit astRoot) {
		return this.nameAdapter.getValue(astRoot);
	}

	public TextRange getNameTextRange(CompilationUnit astRoot) {
		return this.nameTextRange;
	}

	private TextRange buildNameTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(this.nameDeclarationAdapter, astRoot);
	}

	abstract DeclarationAnnotationElementAdapter<String> getNameAdapter();

	// ***** initial value
	public Integer getInitialValue() {
		return this.initialValue;
	}

	public void setInitialValue(Integer initialValue) {
		if (this.attributeValueHasChanged(this.initialValue, initialValue)) {
			this.initialValue = initialValue;
			this.initialValueAdapter.setValue(initialValue);
		}
	}

	private void syncInitialValue(Integer astIinitialValue) {
		Integer old = this.initialValue;
		this.initialValue = astIinitialValue;
		this.firePropertyChanged(INITIAL_VALUE_PROPERTY, old, astIinitialValue);
	}

	private Integer buildInitialValue(CompilationUnit astRoot) {
		return this.initialValueAdapter.getValue(astRoot);
	}

	public TextRange getInitialValueTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(this.initialValueDeclarationAdapter, astRoot);
	}

	abstract DeclarationAnnotationElementAdapter<Integer> getInitialValueAdapter();

	// ***** allocation size
	public Integer getAllocationSize() {
		return this.allocationSize;
	}

	public void setAllocationSize(Integer allocationSize) {
		if (this.attributeValueHasChanged(this.allocationSize, allocationSize)) {
			this.allocationSize = allocationSize;
			this.allocationSizeAdapter.setValue(allocationSize);
		}
	}

	private void syncAllocationSize(Integer astAllocationSize) {
		Integer old = this.allocationSize;
		this.allocationSize = astAllocationSize;
		this.firePropertyChanged(ALLOCATION_SIZE_PROPERTY, old, astAllocationSize);
	}

	private Integer buildAllocationSize(CompilationUnit astRoot) {
		return this.allocationSizeAdapter.getValue(astRoot);
	}

	public TextRange getAllocationSizeTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(this.allocationSizeDeclarationAdapter, astRoot);
	}

	abstract DeclarationAnnotationElementAdapter<Integer> getAllocationSizeAdapter();


	// ********** static methods **********

	static DeclarationAnnotationElementAdapter<String> buildAdapter(DeclarationAnnotationAdapter annotationAdapter, String elementName) {
		return ConversionDeclarationAnnotationElementAdapter.forStrings(annotationAdapter, elementName);
	}

	static DeclarationAnnotationElementAdapter<Integer> buildIntegerAdapter(DeclarationAnnotationAdapter annotationAdapter, String elementName) {
		return new ConversionDeclarationAnnotationElementAdapter<Integer>(annotationAdapter, elementName, NumberIntegerExpressionConverter.instance());
	}
}
