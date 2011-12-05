/*******************************************************************************
* Copyright (c) 2011 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
* 
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.tests.internal.context;

import org.eclipse.jpt.jpa.core.JpaFacet;
import org.eclipse.jpt.jpa.core.platform.JpaPlatformDescription;
import org.eclipse.jpt.jpa.eclipselink.core.platform.EclipseLinkPlatform;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.v2_2.EclipseLink2_2;

public abstract class EclipseLink2_2ContextModelTestCase extends EclipseLinkContextModelTestCase
{
	protected EclipseLink2_2ContextModelTestCase(String name) {
		super(name);
	}

	@Override
	protected String getJpaFacetVersionString() {
		return JpaFacet.VERSION_2_0.getVersionString();
	}

	@Override
	protected JpaPlatformDescription getJpaPlatformDescription() {
		return EclipseLinkPlatform.VERSION_2_2;
	}

	@Override
	protected String getEclipseLinkSchemaVersion() {
		return EclipseLink2_2.SCHEMA_VERSION;
	}
}
