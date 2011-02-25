/*******************************************************************************
 *  Copyright (c) 2011  Oracle. All rights reserved.
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0, which accompanies this distribution
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jaxb.eclipselink.core.internal.v2_3;

import org.eclipse.jpt.jaxb.core.JaxbFactory;
import org.eclipse.jpt.jaxb.core.internal.jaxb21.GenericJaxb_2_1_Factory;
import org.eclipse.jpt.jaxb.core.internal.jaxb22.AbstractJaxb_2_2_PlatformDefinition;
import org.eclipse.jpt.jaxb.core.platform.JaxbPlatformDefinition;
import org.eclipse.jpt.jaxb.core.platform.JaxbPlatformDescription;
import org.eclipse.jpt.jaxb.eclipselink.core.MoxyPlatform;


public class Moxy_2_3_PlatformDefinition
		extends  AbstractJaxb_2_2_PlatformDefinition {
	
	// singleton
	private static final JaxbPlatformDefinition INSTANCE = new Moxy_2_3_PlatformDefinition();
	
	/**
	 * Return the singleton.
	 */
	public static JaxbPlatformDefinition instance() {
		return INSTANCE;
	}
	
	
	protected Moxy_2_3_PlatformDefinition() {
		super();
	}
	
	
	public JaxbPlatformDescription getDescription() {
		return MoxyPlatform.VERSION_2_3;
	}

	public JaxbFactory getFactory() {
		return GenericJaxb_2_1_Factory.instance();
	}
}
