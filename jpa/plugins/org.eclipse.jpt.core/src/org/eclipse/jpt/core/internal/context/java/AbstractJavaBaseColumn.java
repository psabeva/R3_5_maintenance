/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import java.util.Iterator;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.context.BaseColumn;
import org.eclipse.jpt.core.context.java.JavaBaseColumn;
import org.eclipse.jpt.core.context.java.JavaJpaContextNode;
import org.eclipse.jpt.core.internal.context.NamedColumnTextRangeResolver;
import org.eclipse.jpt.core.resource.java.BaseColumnAnnotation;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.utility.Filter;
import org.eclipse.jpt.utility.internal.StringTools;
import org.eclipse.jpt.utility.internal.iterators.FilteringIterator;

public abstract class AbstractJavaBaseColumn<T extends BaseColumnAnnotation> extends AbstractJavaNamedColumn<T>
	implements JavaBaseColumn
{

	protected String specifiedTable;
	
	protected String defaultTable;

	protected Boolean specifiedUnique;
	
	protected Boolean specifiedNullable;
	
	protected Boolean specifiedInsertable;
	
	protected Boolean specifiedUpdatable;

	protected AbstractJavaBaseColumn(JavaJpaContextNode parent, JavaBaseColumn.Owner owner) {
		super(parent, owner);
	}
	
	@Override
	protected void initialize(T column) {
		super.initialize(column);
		this.defaultTable = this.buildDefaultTableName();
		this.specifiedTable = this.getResourceTable();
		this.specifiedUnique = this.getResourceUnique();
		this.specifiedNullable = this.getResourceNullable();
		this.specifiedInsertable = this.getResourceInsertable();
		this.specifiedUpdatable = this.getResourceUpdatable();
	}
	
	@Override
	protected void update(T column) {
		super.update(column);
		this.setDefaultTable(this.buildDefaultTableName());
		this.setSpecifiedTable_(this.getResourceTable());
		this.setSpecifiedUnique_(this.getResourceUnique());
		this.setSpecifiedNullable_(this.getResourceNullable());
		this.setSpecifiedInsertable_(this.getResourceInsertable());
		this.setSpecifiedUpdatable_(this.getResourceUpdatable());
	}
	
	@Override
	public JavaBaseColumn.Owner getOwner() {
		return (JavaBaseColumn.Owner) super.getOwner();
	}

	//************** BaseColumn implementation *******************
	
	//************** table *******************

	@Override
	public String getTable() {
		return (this.getSpecifiedTable() == null) ? getDefaultTable() : this.getSpecifiedTable();
	}

	public String getSpecifiedTable() {
		return this.specifiedTable;
	}

	public void setSpecifiedTable(String newSpecifiedTable) {
		String oldSpecifiedTable = this.specifiedTable;
		this.specifiedTable = newSpecifiedTable;
		getResourceColumn().setTable(newSpecifiedTable);
		firePropertyChanged(BaseColumn.SPECIFIED_TABLE_PROPERTY, oldSpecifiedTable, newSpecifiedTable);
	}
	
	/**
	 * internal setter used only for updating from the resource model.
	 * There were problems with InvalidThreadAccess exceptions in the UI
	 * when you set a value from the UI and the annotation doesn't exist yet.
	 * Adding the annotation causes an update to occur and then the exception.
	 */
	protected void setSpecifiedTable_(String newSpecifiedTable) {
		String oldSpecifiedTable = this.specifiedTable;
		this.specifiedTable = newSpecifiedTable;
		firePropertyChanged(BaseColumn.SPECIFIED_TABLE_PROPERTY, oldSpecifiedTable, newSpecifiedTable);
	}

	public String getDefaultTable() {
		return this.defaultTable;
	}

	protected void setDefaultTable(String newDefaultTable) {
		String oldDefaultTable = this.defaultTable;
		this.defaultTable = newDefaultTable;
		firePropertyChanged(BaseColumn.DEFAULT_TABLE_PROPERTY, oldDefaultTable, newDefaultTable);
	}
	
	protected String getResourceTable() {
		return getResourceColumn().getTable();
	}
	
	public boolean tableNameIsInvalid() {
		return getOwner().tableNameIsInvalid(getTable());
	}

	//************** unique *******************

	public boolean isUnique() {
		return (this.getSpecifiedUnique() == null) ? this.isDefaultUnique() : this.getSpecifiedUnique().booleanValue();
	}
	
	public boolean isDefaultUnique() {
		return BaseColumn.DEFAULT_UNIQUE;
	}
	
	public Boolean getSpecifiedUnique() {
		return this.specifiedUnique;
	}
	
	public void setSpecifiedUnique(Boolean newSpecifiedUnique) {
		Boolean oldSpecifiedUnique = this.specifiedUnique;
		this.specifiedUnique = newSpecifiedUnique;
		this.getResourceColumn().setUnique(newSpecifiedUnique);
		firePropertyChanged(BaseColumn.SPECIFIED_UNIQUE_PROPERTY, oldSpecifiedUnique, newSpecifiedUnique);
	}
	
	/**
	 * internal setter used only for updating from the resource model.
	 * There were problems with InvalidThreadAccess exceptions in the UI
	 * when you set a value from the UI and the annotation doesn't exist yet.
	 * Adding the annotation causes an update to occur and then the exception.
	 */
	protected void setSpecifiedUnique_(Boolean newSpecifiedUnique) {
		Boolean oldSpecifiedUnique = this.specifiedUnique;
		this.specifiedUnique = newSpecifiedUnique;
		firePropertyChanged(BaseColumn.SPECIFIED_UNIQUE_PROPERTY, oldSpecifiedUnique, newSpecifiedUnique);
	}
	
	protected Boolean getResourceUnique() {
		return getResourceColumn().getUnique();
	}
	
	//************** nullable *******************
	
	public boolean isNullable() {
		return (this.getSpecifiedNullable() == null) ? this.isDefaultNullable() : this.getSpecifiedNullable().booleanValue();
	}
	
	public boolean isDefaultNullable() {
		return BaseColumn.DEFAULT_NULLABLE;
	}
	
	public Boolean getSpecifiedNullable() {
		return this.specifiedNullable;
	}
	
	public void setSpecifiedNullable(Boolean newSpecifiedNullable) {
		Boolean oldSpecifiedNullable = this.specifiedNullable;
		this.specifiedNullable = newSpecifiedNullable;
		this.getResourceColumn().setNullable(newSpecifiedNullable);
		firePropertyChanged(BaseColumn.SPECIFIED_NULLABLE_PROPERTY, oldSpecifiedNullable, newSpecifiedNullable);
	}
	
	/**
	 * internal setter used only for updating from the resource model.
	 * There were problems with InvalidThreadAccess exceptions in the UI
	 * when you set a value from the UI and the annotation doesn't exist yet.
	 * Adding the annotation causes an update to occur and then the exception.
	 */
	protected void setSpecifiedNullable_(Boolean newSpecifiedNullable) {
		Boolean oldSpecifiedNullable = this.specifiedNullable;
		this.specifiedNullable = newSpecifiedNullable;
		firePropertyChanged(BaseColumn.SPECIFIED_NULLABLE_PROPERTY, oldSpecifiedNullable, newSpecifiedNullable);
	}
	
	protected Boolean getResourceNullable() {
		return getResourceColumn().getNullable();
	}
	
	//************** insertable *******************
	
	public boolean isInsertable() {
		return (this.getSpecifiedInsertable() == null) ? this.isDefaultInsertable() : this.getSpecifiedInsertable().booleanValue();
	}
	
	public boolean isDefaultInsertable() {
		return BaseColumn.DEFAULT_INSERTABLE;
	}
	
	public Boolean getSpecifiedInsertable() {
		return this.specifiedInsertable;
	}
	
	public void setSpecifiedInsertable(Boolean newSpecifiedInsertable) {
		Boolean oldSpecifiedInsertable = this.specifiedInsertable;
		this.specifiedInsertable = newSpecifiedInsertable;
		this.getResourceColumn().setInsertable(newSpecifiedInsertable);
		firePropertyChanged(BaseColumn.SPECIFIED_INSERTABLE_PROPERTY, oldSpecifiedInsertable, newSpecifiedInsertable);
	}
	
	/**
	 * internal setter used only for updating from the resource model.
	 * There were problems with InvalidThreadAccess exceptions in the UI
	 * when you set a value from the UI and the annotation doesn't exist yet.
	 * Adding the annotation causes an update to occur and then the exception.
	 */
	protected void setSpecifiedInsertable_(Boolean newSpecifiedInsertable) {
		Boolean oldSpecifiedInsertable = this.specifiedInsertable;
		this.specifiedInsertable = newSpecifiedInsertable;
		firePropertyChanged(BaseColumn.SPECIFIED_INSERTABLE_PROPERTY, oldSpecifiedInsertable, newSpecifiedInsertable);
	}
	
	protected Boolean getResourceInsertable() {
		return getResourceColumn().getInsertable();
	}
	
	//************** updatable *******************

	public boolean isUpdatable() {
		return (this.getSpecifiedUpdatable() == null) ? this.isDefaultUpdatable() : this.getSpecifiedUpdatable().booleanValue();
	}
	
	public boolean isDefaultUpdatable() {
		return BaseColumn.DEFAULT_UPDATABLE;
	}
	
	public Boolean getSpecifiedUpdatable() {
		return this.specifiedUpdatable;
	}
	
	public void setSpecifiedUpdatable(Boolean newSpecifiedUpdatable) {
		Boolean oldSpecifiedUpdatable = this.specifiedUpdatable;
		this.specifiedUpdatable = newSpecifiedUpdatable;
		this.getResourceColumn().setUpdatable(newSpecifiedUpdatable);
		firePropertyChanged(BaseColumn.SPECIFIED_UPDATABLE_PROPERTY, oldSpecifiedUpdatable, newSpecifiedUpdatable);
	}

	/**
	 * internal setter used only for updating from the resource model.
	 * There were problems with InvalidThreadAccess exceptions in the UI
	 * when you set a value from the UI and the annotation doesn't exist yet.
	 * Adding the annotation causes an update to occur and then the exception.
	 */
	protected void setSpecifiedUpdatable_(Boolean newSpecifiedUpdatable) {
		Boolean oldSpecifiedUpdatable = this.specifiedUpdatable;
		this.specifiedUpdatable = newSpecifiedUpdatable;
		firePropertyChanged(BaseColumn.SPECIFIED_UPDATABLE_PROPERTY, oldSpecifiedUpdatable, newSpecifiedUpdatable);
	}
	
	protected Boolean getResourceUpdatable() {
		return getResourceColumn().getUpdatable();
	}

	public TextRange getTableTextRange(CompilationUnit astRoot) {
		TextRange textRange = this.getResourceColumn().getTableTextRange(astRoot);
		return (textRange != null) ? textRange : this.getOwner().getValidationTextRange(astRoot);
	}

	public boolean tableTouches(int pos, CompilationUnit astRoot) {
		return getResourceColumn().tableTouches(pos, astRoot);
	}

	public Iterator<String> candidateTableNames() {
		return getOwner().candidateTableNames();
	}

	private Iterator<String> candidateTableNames(Filter<String> filter) {
		return new FilteringIterator<String>(this.candidateTableNames(), filter);
	}

	private Iterator<String> javaCandidateTableNames(Filter<String> filter) {
		return StringTools.convertToJavaStringLiterals(this.candidateTableNames(filter));
	}

	@Override
	public Iterator<String> javaCompletionProposals(int pos, Filter<String> filter, CompilationUnit astRoot) {
		Iterator<String> result = super.javaCompletionProposals(pos, filter, astRoot);
		if (result != null) {
			return result;
		}
		if (this.tableTouches(pos, astRoot)) {
			return this.javaCandidateTableNames(filter);
		}
		return null;
	}

	protected String buildDefaultTableName() {
		return this.getOwner().getDefaultTableName();
	}

	@Override
	protected NamedColumnTextRangeResolver buildTextRangeResolver(CompilationUnit astRoot) {
		return new JavaBaseColumnTextRangeResolver(this, astRoot);
	}
}
