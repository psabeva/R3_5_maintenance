/*******************************************************************************
 * Copyright (c) 2008, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.context.java;

import org.eclipse.jpt.core.JpaFactory;
import org.eclipse.jpt.core.context.java.JavaAttributeMapping;
import org.eclipse.jpt.core.context.java.JavaAttributeMappingProvider;
import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.internal.context.java.AbstractJavaAttributeMappingProvider;
import org.eclipse.jpt.core.internal.context.java.JavaOneToManyMappingProvider;

public class JavaEclipseLinkOneToManyMappingProvider
	extends AbstractJavaAttributeMappingProvider
{
	// singleton
	private static final JavaEclipseLinkOneToManyMappingProvider INSTANCE = 
			new JavaEclipseLinkOneToManyMappingProvider();
	
	
	/**
	 * Return the singleton.
	 */
	public static JavaAttributeMappingProvider instance() {
		return INSTANCE;
	}
	
	
	/**
	 * Enforce singleton usage
	 */
	private JavaEclipseLinkOneToManyMappingProvider() {
		super();
	}
	
	
	public String getKey() {
		return JavaOneToManyMappingProvider.instance().getKey();
	}
	
	public String getAnnotationName() {
		return JavaOneToManyMappingProvider.instance().getAnnotationName();
	}

	public JavaAttributeMapping buildMapping(JavaPersistentAttribute parent, JpaFactory factory) {
		return JavaOneToManyMappingProvider.instance().buildMapping(parent, factory);
	}
	
	@Override
	public boolean testDefault(JavaPersistentAttribute persistentAttribute) {
		String targetEntity = persistentAttribute.getMultiReferenceEntityTypeName();
		return (targetEntity != null) 
				&& (persistentAttribute.getPersistenceUnit().getEntity(targetEntity) != null);
	}
}
