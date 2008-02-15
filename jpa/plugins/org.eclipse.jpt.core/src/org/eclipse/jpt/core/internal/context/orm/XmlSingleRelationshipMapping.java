/*******************************************************************************
 * Copyright (c) 2006, 2007 Oracle. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Oracle. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.ITextRange;
import org.eclipse.jpt.core.internal.context.base.FetchType;
import org.eclipse.jpt.core.internal.context.base.IAbstractJoinColumn;
import org.eclipse.jpt.core.internal.context.base.IEntity;
import org.eclipse.jpt.core.internal.context.base.IJoinColumn;
import org.eclipse.jpt.core.internal.context.base.INullable;
import org.eclipse.jpt.core.internal.context.base.IRelationshipMapping;
import org.eclipse.jpt.core.internal.context.base.ISingleRelationshipMapping;
import org.eclipse.jpt.core.internal.context.base.ITypeMapping;
import org.eclipse.jpt.core.internal.resource.orm.JoinColumn;
import org.eclipse.jpt.core.internal.resource.orm.OrmFactory;
import org.eclipse.jpt.core.internal.resource.orm.SingleRelationshipMapping;
import org.eclipse.jpt.db.internal.Table;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.iterators.CloneListIterator;
import org.eclipse.jpt.utility.internal.iterators.EmptyListIterator;


public abstract class XmlSingleRelationshipMapping<T extends SingleRelationshipMapping>
	extends XmlRelationshipMapping<T> implements ISingleRelationshipMapping
{
	
	protected final List<XmlJoinColumn> specifiedJoinColumns;

	protected final List<XmlJoinColumn> defaultJoinColumns;

	protected Boolean specifiedOptional;

	protected XmlSingleRelationshipMapping(XmlPersistentAttribute parent) {
		super(parent);
		this.specifiedJoinColumns = new ArrayList<XmlJoinColumn>();
		this.defaultJoinColumns = new ArrayList<XmlJoinColumn>();

		//this.getDefaultJoinColumns().add(this.createJoinColumn(new JoinColumnOwner(this)));
	}
	
	@Override
	public void initializeFromXmlSingleRelationshipMapping(XmlSingleRelationshipMapping<? extends SingleRelationshipMapping> oldMapping) {
		super.initializeFromXmlSingleRelationshipMapping(oldMapping);
		int index = 0;
		for (IJoinColumn joinColumn : CollectionTools.iterable(oldMapping.specifiedJoinColumns())) {
			XmlJoinColumn newJoinColumn = addSpecifiedJoinColumn(index++);
			newJoinColumn.initializeFrom(joinColumn);
		}
	}
	
	public FetchType getDefaultFetch() {
		return ISingleRelationshipMapping.DEFAULT_FETCH_TYPE;
	}

	//***************** ISingleRelationshipMapping implementation *****************
	@SuppressWarnings("unchecked")
	public ListIterator<XmlJoinColumn> joinColumns() {
		return this.specifiedJoinColumns.isEmpty() ? this.defaultJoinColumns() : this.specifiedJoinColumns();
	}

	public int joinColumnsSize() {
		return this.specifiedJoinColumns.isEmpty() ? this.defaultJoinColumnsSize() : this.specifiedJoinColumnsSize();
	}
	
	public IJoinColumn getDefaultJoinColumn() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public ListIterator<XmlJoinColumn> defaultJoinColumns() {
		return new CloneListIterator<XmlJoinColumn>(this.defaultJoinColumns);
	}

	public int defaultJoinColumnsSize() {
		return this.defaultJoinColumns.size();
	}
	
	@SuppressWarnings("unchecked")
	public ListIterator<XmlJoinColumn> specifiedJoinColumns() {
		return new CloneListIterator<XmlJoinColumn>(this.specifiedJoinColumns);
	}

	public int specifiedJoinColumnsSize() {
		return this.specifiedJoinColumns.size();
	}

	public boolean containsSpecifiedJoinColumns() {
		return !this.specifiedJoinColumns.isEmpty();
	}

	public XmlJoinColumn addSpecifiedJoinColumn(int index) {
		XmlJoinColumn joinColumn = new XmlJoinColumn(this, new JoinColumnOwner());
		this.specifiedJoinColumns.add(index, joinColumn);
		this.attributeMapping().getJoinColumns().add(index, OrmFactory.eINSTANCE.createJoinColumnImpl());
		this.fireItemAdded(ISingleRelationshipMapping.SPECIFIED_JOIN_COLUMNS_LIST, index, joinColumn);
		return joinColumn;
	}

	protected void addSpecifiedJoinColumn(int index, XmlJoinColumn joinColumn) {
		addItemToList(index, joinColumn, this.specifiedJoinColumns, ISingleRelationshipMapping.SPECIFIED_JOIN_COLUMNS_LIST);
	}

	public void removeSpecifiedJoinColumn(IJoinColumn joinColumn) {
		this.removeSpecifiedJoinColumn(this.specifiedJoinColumns.indexOf(joinColumn));
	}
	
	public void removeSpecifiedJoinColumn(int index) {
		XmlJoinColumn removedJoinColumn = this.specifiedJoinColumns.remove(index);
		this.attributeMapping().getJoinColumns().remove(index);
		fireItemRemoved(ISingleRelationshipMapping.SPECIFIED_JOIN_COLUMNS_LIST, index, removedJoinColumn);
	}

	protected void removeSpecifiedJoinColumn_(XmlJoinColumn joinColumn) {
		removeItemFromList(joinColumn, this.specifiedJoinColumns, ISingleRelationshipMapping.SPECIFIED_JOIN_COLUMNS_LIST);
	}
	
	public void moveSpecifiedJoinColumn(int targetIndex, int sourceIndex) {
		CollectionTools.move(this.specifiedJoinColumns, targetIndex, sourceIndex);
		this.attributeMapping().getJoinColumns().move(targetIndex, sourceIndex);
		fireItemMoved(ISingleRelationshipMapping.SPECIFIED_JOIN_COLUMNS_LIST, targetIndex, sourceIndex);		
	}

	public Boolean getOptional() {
		return getSpecifiedOptional() == null ? getDefaultOptional() : getSpecifiedOptional();
	}
	
	public Boolean getDefaultOptional() {
		return INullable.DEFAULT_OPTIONAL;
	}

	public Boolean getSpecifiedOptional() {
		return this.specifiedOptional;
	}
	
	public void setSpecifiedOptional(Boolean newSpecifiedOptional) {
		Boolean oldSpecifiedOptional = this.specifiedOptional;
		this.specifiedOptional = newSpecifiedOptional;
		attributeMapping().setOptional(newSpecifiedOptional);
		firePropertyChanged(INullable.SPECIFIED_OPTIONAL_PROPERTY, oldSpecifiedOptional, newSpecifiedOptional);
	}
	
	protected void setSpecifiedOptional_(Boolean newSpecifiedOptional) {
		Boolean oldSpecifiedOptional = this.specifiedOptional;
		this.specifiedOptional = newSpecifiedOptional;
		firePropertyChanged(INullable.SPECIFIED_OPTIONAL_PROPERTY, oldSpecifiedOptional, newSpecifiedOptional);
	}
//
//	public boolean containsSpecifiedJoinColumns() {
//		return !this.getSpecifiedJoinColumns().isEmpty();
//	}
	
	@Override
	public void initialize(T singleRelationshipMapping) {
		super.initialize(singleRelationshipMapping);
		this.specifiedOptional = singleRelationshipMapping.getOptional();
		//TODO defaultOptional
		this.initializeSpecifiedJoinColumns(singleRelationshipMapping);
	}
	
	protected void initializeSpecifiedJoinColumns(T singleRelationshipMapping) {
		if (singleRelationshipMapping == null) {
			return;
		}
		for (JoinColumn joinColumn : singleRelationshipMapping.getJoinColumns()) {
			this.specifiedJoinColumns.add(createJoinColumn(joinColumn));
		}
	}
	
	protected XmlJoinColumn createJoinColumn(JoinColumn joinColumn) {
		XmlJoinColumn xmlJoinColumn = new XmlJoinColumn(this, new JoinColumnOwner());
		xmlJoinColumn.initialize(joinColumn);
		return xmlJoinColumn;
	}	

	@Override
	public void update(T singleRelationshipMapping) {
		super.update(singleRelationshipMapping);
		this.setSpecifiedOptional_(singleRelationshipMapping.getOptional());
		this.updateSpecifiedJoinColumns(singleRelationshipMapping);
	}
	
	protected void updateSpecifiedJoinColumns(T singleRelationshipMapping) {
		ListIterator<XmlJoinColumn> joinColumns = specifiedJoinColumns();
		ListIterator<JoinColumn> resourceJoinColumns = EmptyListIterator.instance();
		if (singleRelationshipMapping != null) {
			resourceJoinColumns = singleRelationshipMapping.getJoinColumns().listIterator();
		}
		
		while (joinColumns.hasNext()) {
			XmlJoinColumn joinColumn = joinColumns.next();
			if (resourceJoinColumns.hasNext()) {
				joinColumn.update(resourceJoinColumns.next());
			}
			else {
				removeSpecifiedJoinColumn_(joinColumn);
			}
		}
		
		while (resourceJoinColumns.hasNext()) {
			addSpecifiedJoinColumn(specifiedJoinColumnsSize(), createJoinColumn(resourceJoinColumns.next()));
		}
	}

	
	public class JoinColumnOwner implements IJoinColumn.Owner
	{

		public JoinColumnOwner() {
			super();
		}

		/**
		 * by default, the join column is in the type mapping's primary table
		 */
		public String defaultTableName() {
			return XmlSingleRelationshipMapping.this.typeMapping().tableName();
		}

		public IEntity targetEntity() {
			return XmlSingleRelationshipMapping.this.getResolvedTargetEntity();
		}

		public String attributeName() {
			return XmlSingleRelationshipMapping.this.getName();
		}

		public IRelationshipMapping relationshipMapping() {
			return XmlSingleRelationshipMapping.this;
		}

		public boolean tableNameIsInvalid(String tableName) {
			return XmlSingleRelationshipMapping.this.typeMapping().tableNameIsInvalid(tableName);
		}

		/**
		 * the join column can be on a secondary table
		 */
		public boolean tableIsAllowed() {
			return true;
		}

		public ITypeMapping typeMapping() {
			return XmlSingleRelationshipMapping.this.typeMapping();
		}

		public Table dbTable(String tableName) {
			return typeMapping().dbTable(tableName);
		}

		public Table dbReferencedColumnTable() {
			IEntity targetEntity = targetEntity();
			return (targetEntity == null) ? null : targetEntity.primaryDbTable();
		}
		
		public boolean isVirtual(IAbstractJoinColumn joinColumn) {
			return XmlSingleRelationshipMapping.this.defaultJoinColumns.contains(joinColumn);
		}
		
		public String defaultColumnName() {
			// TODO Auto-generated method stub
			return null;
		}
		
		public ITextRange validationTextRange(CompilationUnit astRoot) {
			// TODO Auto-generated method stub
			return null;
		}

		public int joinColumnsSize() {
			return XmlSingleRelationshipMapping.this.joinColumnsSize();
		}
	}
}
