/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.context;


/**
 * 
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface Generator extends JpaContextNode
{
	// **************** name ***************************************************
	
	String NAME_PROPERTY = "nameProperty";
	
	String getName();
	
	void setName(String value);
	
	
	// **************** initial value ******************************************
	
	Integer getInitialValue();
	
	String DEFAULT_INITIAL_VALUE_PROPERTY = "defaultInitialValueProperty";
	
	Integer getDefaultInitialValue();
	
	String SPECIFIED_INITIAL_VALUE_PROPERTY = "specifiedInitialValueProperty";
	
	Integer getSpecifiedInitialValue();
	
	void setSpecifiedInitialValue(Integer value);
	
	
	// **************** allocation size ****************************************
	
	Integer getAllocationSize();
	
	String DEFAULT_ALLOCATION_SIZE_PROPERTY = "defaultAllocationSizeProperty";
	
	Integer DEFAULT_ALLOCATION_SIZE = Integer.valueOf(50);
	
	Integer getDefaultAllocationSize();
		
	String SPECIFIED_ALLOCATION_SIZE_PROPERTY = "specifiedAllocationSizeProperty";
	
	Integer getSpecifiedAllocationSize();
	
	void setSpecifiedAllocationSize(Integer value);
	
	
	// **************** validation *********************************************
	
	/**
	 * Return true if this generator overrides the definition of the specified
	 * generator
	 * (for example, a generator defined in orm.xml overrides one defined in java)
	 */
	boolean overrides(Generator generator);
}
