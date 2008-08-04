/*******************************************************************************
 * Copyright (c) 2006, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.context.Column;
import org.eclipse.jpt.core.context.java.JavaColumn;
import org.eclipse.jpt.core.context.java.JavaJpaContextNode;
import org.eclipse.jpt.core.resource.java.ColumnAnnotation;
import org.eclipse.jpt.core.utility.TextRange;

public class GenericJavaColumn extends AbstractJavaBaseColumn<ColumnAnnotation> implements JavaColumn
{

	protected Integer specifiedLength;

	protected Integer specifiedPrecision;

	protected Integer specifiedScale;
	
	public GenericJavaColumn(JavaJpaContextNode parent, JavaColumn.Owner owner) {
		super(parent, owner);
	}
	
	@Override
	public void initialize(ColumnAnnotation column) {
		super.initialize(column);
		this.specifiedLength = this.specifiedLength(column);
		this.specifiedPrecision = this.specifiedPrecision(column);
		this.specifiedScale = this.specifiedScale(column);
	}
	
	@Override
	public JavaColumn.Owner getOwner() {
		return (JavaColumn.Owner) super.getOwner();
	}

	@Override
	protected ColumnAnnotation getColumnResource() {
		return this.getOwner().getColumnResource();
	}
	
	public Integer getLength() {
		return (this.getSpecifiedLength() == null) ? getDefaultLength() : this.getSpecifiedLength();
	}

	public Integer getDefaultLength() {
		return Column.DEFAULT_LENGTH;
	}
	
	public Integer getSpecifiedLength() {
		return this.specifiedLength;
	}

	public void setSpecifiedLength(Integer newSpecifiedLength) {
		Integer oldSpecifiedLength = this.specifiedLength;
		this.specifiedLength = newSpecifiedLength;
		getColumnResource().setLength(newSpecifiedLength);
		firePropertyChanged(SPECIFIED_LENGTH_PROPERTY, oldSpecifiedLength, newSpecifiedLength);
	}
	
	protected void setSpecifiedLength_(Integer newSpecifiedLength) {
		Integer oldSpecifiedLength = this.specifiedLength;
		this.specifiedLength = newSpecifiedLength;
		firePropertyChanged(SPECIFIED_LENGTH_PROPERTY, oldSpecifiedLength, newSpecifiedLength);
	}

	public Integer getPrecision() {
		return (this.getSpecifiedPrecision() == null) ? getDefaultPrecision() : this.getSpecifiedPrecision();
	}

	public Integer getDefaultPrecision() {
		return Column.DEFAULT_PRECISION;
	}
	
	public Integer getSpecifiedPrecision() {
		return this.specifiedPrecision;
	}

	public void setSpecifiedPrecision(Integer newSpecifiedPrecision) {
		Integer oldSpecifiedPrecision = this.specifiedPrecision;
		this.specifiedPrecision = newSpecifiedPrecision;
		getColumnResource().setPrecision(newSpecifiedPrecision);
		firePropertyChanged(SPECIFIED_PRECISION_PROPERTY, oldSpecifiedPrecision, newSpecifiedPrecision);
	}
	
	protected void setSpecifiedPrecision_(Integer newSpecifiedPrecision) {
		Integer oldSpecifiedPrecision = this.specifiedPrecision;
		this.specifiedPrecision = newSpecifiedPrecision;
		firePropertyChanged(SPECIFIED_PRECISION_PROPERTY, oldSpecifiedPrecision, newSpecifiedPrecision);
	}

	public Integer getScale() {
		return (this.getSpecifiedScale() == null) ? getDefaultScale() : this.getSpecifiedScale();
	}

	public Integer getDefaultScale() {
		return Column.DEFAULT_SCALE;
	}
	
	public Integer getSpecifiedScale() {
		return this.specifiedScale;
	}

	public void setSpecifiedScale(Integer newSpecifiedScale) {
		Integer oldSpecifiedScale = this.specifiedScale;
		this.specifiedScale = newSpecifiedScale;
		getColumnResource().setScale(newSpecifiedScale);
		firePropertyChanged(SPECIFIED_SCALE_PROPERTY, oldSpecifiedScale, newSpecifiedScale);
	}
	
	protected void setSpecifiedScale_(Integer newSpecifiedScale) {
		Integer oldSpecifiedScale = this.specifiedScale;
		this.specifiedScale = newSpecifiedScale;
		firePropertyChanged(SPECIFIED_SCALE_PROPERTY, oldSpecifiedScale, newSpecifiedScale);
	}

	@Override
	public boolean tableIsAllowed() {
		return true;
	}
	
	public TextRange getValidationTextRange(CompilationUnit astRoot) {
		TextRange textRange = getColumnResource().getTextRange(astRoot);
		return (textRange != null) ? textRange : this.getOwner().getValidationTextRange(astRoot);	
	}
	
	@Override
	public void update(ColumnAnnotation column) {
		super.update(column);
		this.setSpecifiedLength_(this.specifiedLength(column));
		this.setSpecifiedPrecision_(this.specifiedPrecision(column));
		this.setSpecifiedScale_(this.specifiedScale(column));
	}
	
	protected Integer specifiedLength(ColumnAnnotation column) {
		return column.getLength();
	}
	
	protected Integer specifiedPrecision(ColumnAnnotation column) {
		return column.getPrecision();
	}
	
	protected Integer specifiedScale(ColumnAnnotation column) {
		return column.getScale();
	}
}
