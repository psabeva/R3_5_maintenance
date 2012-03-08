/*******************************************************************************
 * Copyright (c) 2007, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa2.resource.java.source;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.common.core.internal.utility.jdt.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAnnotatedElement;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.core.utility.jdt.AnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.AnnotationElementAdapter;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationElementAdapter;
import org.eclipse.jpt.jpa.core.internal.resource.java.source.SourceNamedColumnAnnotation;
import org.eclipse.jpt.jpa.core.jpa2.resource.java.JPA2_0;
import org.eclipse.jpt.jpa.core.jpa2.resource.java.OrderColumn2_0Annotation;

/**
 * <code>javax.persistence.OrderColumn</code>
 */
public final class SourceOrderColumn2_0Annotation
	extends SourceNamedColumnAnnotation
	implements OrderColumn2_0Annotation
{
	private static final DeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER = new SimpleDeclarationAnnotationAdapter(ANNOTATION_NAME);

	private DeclarationAnnotationElementAdapter<Boolean> nullableDeclarationAdapter;
	private AnnotationElementAdapter<Boolean> nullableAdapter;
	private Boolean nullable;
	private TextRange nullableTextRange;

	private DeclarationAnnotationElementAdapter<Boolean> insertableDeclarationAdapter;
	private AnnotationElementAdapter<Boolean> insertableAdapter;
	private Boolean insertable;
	private TextRange insertableTextRange;

	private DeclarationAnnotationElementAdapter<Boolean> updatableDeclarationAdapter;
	private AnnotationElementAdapter<Boolean> updatableAdapter;
	private Boolean updatable;
	private TextRange updatableTextRange;


	public SourceOrderColumn2_0Annotation(JavaResourceAnnotatedElement parent, AnnotatedElement element) {
		super(parent, element, DECLARATION_ANNOTATION_ADAPTER);
		this.nullableDeclarationAdapter = this.buildNullableDeclarationAdapter();
		this.nullableAdapter = this.buildNullableAdapter();
		this.insertableDeclarationAdapter = this.buildInsertableDeclarationAdapter();
		this.insertableAdapter = this.buildInsertableAdapter();
		this.updatableDeclarationAdapter = this.buildUpdatableDeclarationAdapter();
		this.updatableAdapter = this.buildUpdatableAdapter();
	}
	
	@Override
	public void initialize(CompilationUnit astRoot) {
		super.initialize(astRoot);

		this.nullable = this.buildNullable(astRoot);
		this.nullableTextRange = this.buildNullableTextRange(astRoot);

		this.insertable = this.buildInsertable(astRoot);
		this.insertableTextRange = this.buildInsertableTextRange(astRoot);

		this.updatable = this.buildUpdatable(astRoot);
		this.updatableTextRange = this.buildUpdatableTextRange(astRoot);
	}
	
	@Override
	public void synchronizeWith(CompilationUnit astRoot) {
		super.synchronizeWith(astRoot);

		this.syncNullable(this.buildNullable(astRoot));
		this.nullableTextRange = this.buildNullableTextRange(astRoot);

		this.syncInsertable(this.buildInsertable(astRoot));
		this.insertableTextRange = this.buildInsertableTextRange(astRoot);

		this.syncUpdatable(this.buildUpdatable(astRoot));
		this.updatableTextRange = this.buildUpdatableTextRange(astRoot);
	}
	
	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}
	
	@Override
	protected String getNameElementName() {
		return JPA2_0.ORDER_COLUMN__NAME;
	}
	
	@Override
	protected String getColumnDefinitionElementName() {
		return JPA2_0.ORDER_COLUMN__COLUMN_DEFINITION;
	}


	//************* OrderColumn2_0Annotation implementation *************

	// ***** nullable
	public Boolean getNullable() {
		return this.nullable;
	}

	public void setNullable(Boolean nullable) {
		if (this.attributeValueHasChanged(this.nullable, nullable)) {
			this.nullable = nullable;
			this.nullableAdapter.setValue(nullable);
		}
	}

	private void syncNullable(Boolean astNullable) {
		Boolean old = this.nullable;
		this.nullable = astNullable;
		this.firePropertyChanged(NULLABLE_PROPERTY, old, astNullable);
	}

	private Boolean buildNullable(CompilationUnit astRoot) {
		return this.nullableAdapter.getValue(astRoot);
	}
	
	public TextRange getNullableTextRange() {
		return this.nullableTextRange;
	}
	
	private TextRange buildNullableTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(this.nullableDeclarationAdapter, astRoot);
	}
	
	private DeclarationAnnotationElementAdapter<Boolean> buildNullableDeclarationAdapter() {
		return this.buildBooleanElementAdapter(this.getNullableElementName());
	}

	private AnnotationElementAdapter<Boolean> buildNullableAdapter() {
		return this.buildBooleanElementAdapter(this.nullableDeclarationAdapter);
	}

	String getNullableElementName() {
		return JPA2_0.ORDER_COLUMN__NULLABLE;
	}

	// ***** insertable
	public Boolean getInsertable() {
		return this.insertable;
	}

	public void setInsertable(Boolean insertable) {
		if (this.attributeValueHasChanged(this.insertable, insertable)) {
			this.insertable = insertable;
			this.insertableAdapter.setValue(insertable);
		}
	}

	private void syncInsertable(Boolean astInsertable) {
		Boolean old = this.insertable;
		this.insertable = astInsertable;
		this.firePropertyChanged(INSERTABLE_PROPERTY, old, astInsertable);
	}

	private Boolean buildInsertable(CompilationUnit astRoot) {
		return this.insertableAdapter.getValue(astRoot);
	}
	
	public TextRange getInsertableTextRange() {
		return this.insertableTextRange;
	}
	
	private TextRange buildInsertableTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(this.insertableDeclarationAdapter, astRoot);
	}
	
	private DeclarationAnnotationElementAdapter<Boolean> buildInsertableDeclarationAdapter() {
		return this.buildBooleanElementAdapter(this.getInsertableElementName());
	}

	private AnnotationElementAdapter<Boolean> buildInsertableAdapter() {
		return this.buildBooleanElementAdapter(this.insertableDeclarationAdapter);
	}

	String getInsertableElementName() {
		return JPA2_0.ORDER_COLUMN__INSERTABLE;
	}

	// ***** updatable
	public Boolean getUpdatable() {
		return this.updatable;
	}

	public void setUpdatable(Boolean updatable) {
		if (this.attributeValueHasChanged(this.updatable, updatable)) {
			this.updatable = updatable;
			this.updatableAdapter.setValue(updatable);
		}
	}

	private void syncUpdatable(Boolean astUpdatable) {
		Boolean old = this.updatable;
		this.updatable = astUpdatable;
		this.firePropertyChanged(UPDATABLE_PROPERTY, old, astUpdatable);
	}

	private Boolean buildUpdatable(CompilationUnit astRoot) {
		return this.updatableAdapter.getValue(astRoot);
	}

	public TextRange getUpdatableTextRange() {
		return this.updatableTextRange;
	}
	
	private TextRange buildUpdatableTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(this.updatableDeclarationAdapter, astRoot);
	}
	
	private DeclarationAnnotationElementAdapter<Boolean> buildUpdatableDeclarationAdapter() {
		return this.buildBooleanElementAdapter(this.getUpdatableElementName());
	}

	private AnnotationElementAdapter<Boolean> buildUpdatableAdapter() {
		return this.buildBooleanElementAdapter(this.updatableDeclarationAdapter);
	}

	String getUpdatableElementName() {
		return JPA2_0.ORDER_COLUMN__UPDATABLE;
	}


	// ********** misc **********

	@Override
	public boolean isUnset() {
		return super.isUnset() &&
				(this.nullable == null) &&
				(this.insertable == null) &&
				(this.updatable == null);
	}
}
