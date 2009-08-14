/*******************************************************************************
* Copyright (c) 2009 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
* 
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.core.internal.jpa2.context.persistence.connection;

import org.eclipse.jpt.core.internal.context.persistence.PersistenceUnitProperties;

/**
 *  JpaConnection2_0
 */
public interface JpaConnection2_0 extends PersistenceUnitProperties
{		
	String getDefaultDriver();
	String getDriver();
	void setDriver(String newDriver);
		static final String DRIVER_PROPERTY = "driver"; //$NON-NLS-1$
		// Property key
		static final String PERSISTENCE_JDBC_DRIVER = "javax.persistence.jdbc.driver"; //$NON-NLS-1$
		static final String DEFAULT_JDBC_DRIVER = ""; //$NON-NLS-1$
		
	String getDefaultUrl();
	String getUrl();
	void setUrl(String newUrl);
		static final String URL_PROPERTY = "url"; //$NON-NLS-1$
		// Property key
		static final String PERSISTENCE_JDBC_URL = "javax.persistence.jdbc.url"; //$NON-NLS-1$
		static final String DEFAULT_JDBC_URL = ""; //$NON-NLS-1$
		
	String getDefaultUser();
	String getUser();
	void setUser(String newUser);
		static final String USER_PROPERTY = "user"; //$NON-NLS-1$
		// Property key
		static final String PERSISTENCE_JDBC_USER = "javax.persistence.jdbc.user"; //$NON-NLS-1$
		static final String DEFAULT_JDBC_USER = ""; //$NON-NLS-1$
		
	String getDefaultPassword();
	String getPassword();
	void setPassword(String newPassword);
		static final String PASSWORD_PROPERTY = "password"; //$NON-NLS-1$
		// Property key
		static final String PERSISTENCE_JDBC_PASSWORD = "javax.persistence.jdbc.password"; //$NON-NLS-1$
		static final String DEFAULT_JDBC_PASSWORD = ""; //$NON-NLS-1$
	
}
