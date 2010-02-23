/*******************************************************************************
 *  Copyright (c) 2009, 2010  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.internal.jpa2.context.orm;

import java.util.List;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.AttributeMapping;
import org.eclipse.jpt.core.context.JoinColumn;
import org.eclipse.jpt.core.context.orm.OrmJoinColumn;
import org.eclipse.jpt.core.context.orm.OrmJoinColumnEnabledRelationshipReference;
import org.eclipse.jpt.core.context.orm.OrmJoinColumnJoiningStrategy;
import org.eclipse.jpt.core.context.orm.OrmJoiningStrategy;
import org.eclipse.jpt.core.context.orm.OrmOneToManyMapping;
import org.eclipse.jpt.core.context.orm.OrmRelationshipReference;
import org.eclipse.jpt.core.internal.context.orm.AbstractOrmOneToManyRelationshipReference;
import org.eclipse.jpt.core.internal.context.orm.GenericOrmJoinColumnJoiningStrategy;
import org.eclipse.jpt.core.resource.orm.XmlOneToMany;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

public class GenericOrmOneToManyRelationshipReference2_0
	extends AbstractOrmOneToManyRelationshipReference
{	
	protected OrmJoinColumnJoiningStrategy joinColumnJoiningStrategy;

	public GenericOrmOneToManyRelationshipReference2_0(
			OrmOneToManyMapping parent, XmlOneToMany resource) {
		
		super(parent, resource);
	}
	
	@Override
	protected void initializeJoiningStrategies() {
		this.joinColumnJoiningStrategy = buildJoinColumnJoiningStrategy();		
		super.initializeJoiningStrategies();
	}
	
	
	protected OrmJoinColumnJoiningStrategy buildJoinColumnJoiningStrategy() {
		return new GenericOrmJoinColumnJoiningStrategy(this, getResourceMapping());
	}
	
	@Override
	public void initializeOn(OrmRelationshipReference newRelationshipReference) {
		super.initializeOn(newRelationshipReference);
		newRelationshipReference.initializeFromJoinColumnEnabledRelationshipReference(this);
	}
	
	@Override
	public void initializeFromJoinColumnEnabledRelationshipReference(
			OrmJoinColumnEnabledRelationshipReference oldRelationshipReference) {
		super.initializeFromJoinColumnEnabledRelationshipReference(oldRelationshipReference);
		int index = 0;
		for (JoinColumn joinColumn : 
				CollectionTools.iterable(
					oldRelationshipReference.getJoinColumnJoiningStrategy().specifiedJoinColumns())) {
			OrmJoinColumn newJoinColumn = getJoinColumnJoiningStrategy().addSpecifiedJoinColumn(index++);
			newJoinColumn.initializeFrom(joinColumn);
		}
	}
	
	@Override
	public XmlOneToMany getResourceMapping() {
		return super.getResourceMapping();
	}
	
	@Override
	protected OrmJoiningStrategy calculatePredominantJoiningStrategy() {
		if (this.mappedByJoiningStrategy.getMappedByAttribute() != null) {
			return this.mappedByJoiningStrategy;
		}
		else if (this.joinColumnJoiningStrategy.hasSpecifiedJoinColumns()) {
			return this.joinColumnJoiningStrategy;
		}
		else {
			return this.joinTableJoiningStrategy;
		}
	}
	
	
	// **************** mapped by **********************************************
	
	@Override
	protected void setMappedByJoiningStrategy_() {
		super.setMappedByJoiningStrategy_();
		this.joinColumnJoiningStrategy.removeStrategy();
	}
	
	@Override
	public boolean mayBeMappedBy(AttributeMapping mappedByMapping) {
		return super.mayBeMappedBy(mappedByMapping) ||
			mappedByMapping.getKey() == MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY;
	}
	
	
	// **************** join table *********************************************
	
	@Override
	protected void setJoinTableJoiningStrategy_() {
		super.setJoinTableJoiningStrategy_();
		this.joinColumnJoiningStrategy.removeStrategy();
	}
	
	@Override
	public boolean mayHaveDefaultJoinTable() {
		return super.mayHaveDefaultJoinTable()
			&& ! this.joinColumnJoiningStrategy.hasSpecifiedJoinColumns();
	}
	
	
	// **************** join columns *******************************************
	
	public OrmJoinColumnJoiningStrategy getJoinColumnJoiningStrategy() {
		return this.joinColumnJoiningStrategy;
	}
	
	public boolean usesJoinColumnJoiningStrategy() {
		return getPredominantJoiningStrategy() == this.joinColumnJoiningStrategy;
	}
	
	public void setJoinColumnJoiningStrategy() {
		this.joinColumnJoiningStrategy.addStrategy();
		this.mappedByJoiningStrategy.removeStrategy();
		this.joinTableJoiningStrategy.removeStrategy();
		setPredominantJoiningStrategy();
	}
	
	public void unsetJoinColumnJoiningStrategy() {
		this.joinColumnJoiningStrategy.removeStrategy();
		setPredominantJoiningStrategy();
	}
	
	public boolean mayHaveDefaultJoinColumn() {
		return false;
	}
	
	
	// **************** resource => context ************************************
	
	@Override
	protected void updateJoiningStrategies() {
		this.joinColumnJoiningStrategy.update();
		super.updateJoiningStrategies();
	}
	
	
	// **************** Validation *********************************************
	
	@Override
	public void validate(List<IMessage> messages, IReporter reporter) {
		super.validate(messages, reporter);
		this.joinColumnJoiningStrategy.validate(messages, reporter);
	}
}
