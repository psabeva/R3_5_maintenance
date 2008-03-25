/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.persistencexml.details;

import org.eclipse.jpt.core.context.persistence.PersistenceXml;
import org.eclipse.jpt.ui.internal.platform.generic.PersistenceXmlItemLabelProvider;
import org.eclipse.jpt.ui.jface.DelegatingContentAndLabelProvider;
import org.eclipse.jpt.ui.jface.ItemLabelProvider;
import org.eclipse.jpt.ui.jface.ItemLabelProviderFactory;

/**
 * EclipseLinkNavigatorItemLabelProviderFactory
 */
public class EclipseLinkNavigatorItemLabelProviderFactory
					implements ItemLabelProviderFactory
{
	public ItemLabelProvider buildItemLabelProvider(
						Object item, 
						DelegatingContentAndLabelProvider contentAndLabelProvider) {
		
		if (item instanceof PersistenceXml) {
			return new PersistenceXmlItemLabelProvider((PersistenceXml) item, contentAndLabelProvider);
		}
		return null;
	}
}
