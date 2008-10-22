/*******************************************************************************
 *  Copyright (c) 2007 Oracle. 
 *  All rights reserved.  This program and the accompanying materials 
 *  are made available under the terms of the Eclipse Public License v1.0 
 *  which accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.core.tests.internal.context.orm;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class JptEclipseLinkCoreOrmContextModelTests extends TestCase
{
	public static Test suite() {
		return suite(true);
	}
	
	public static Test suite(boolean all) {
		TestSuite suite = new TestSuite(JptEclipseLinkCoreOrmContextModelTests.class.getName());
		suite.addTestSuite(EclipseLinkOrmEmbeddableTests.class);
		suite.addTestSuite(EclipseLinkOrmEntityTests.class);
		suite.addTestSuite(EclipseLinkOrmMappedSuperclassTests.class);
		suite.addTestSuite(EclipseLinkOrmIdMappingTests.class);
		suite.addTestSuite(EclipseLinkOrmBasicMappingTests.class);
		suite.addTestSuite(EclipseLinkOrmVersionMappingTests.class);
		suite.addTestSuite(EclipseLinkOrmManyToOneMappingTests.class);
		suite.addTestSuite(EclipseLinkOrmOneToManyMappingTests.class);
		suite.addTestSuite(EclipseLinkOrmOneToOneMappingTests.class);
		suite.addTestSuite(EclipseLinkOrmManyToManyMappingTests.class);
		return suite;
	}
	
	
	private JptEclipseLinkCoreOrmContextModelTests() {
		throw new UnsupportedOperationException();
	}
}
