/*******************************************************************************
 * Copyright (c) 2007, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.context.persistence;

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.jpt.jpa.core.context.JpaContextNode;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceXmlContextNodeFactory;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceXmlDefinition;
import org.eclipse.jpt.jpa.core.internal.context.AbstractJpaContextNode;

/**
 * Use this abstract class for context nodes that are part of an
 * <code>orm.xml</code> file.
 * This will not work for a pure {@link org.eclipse.jpt.jpa.core.context.MappingFile}
 * implementation.
 */
public abstract class AbstractPersistenceXmlContextNode
	extends AbstractJpaContextNode
{
	// ********** constructor **********

	protected AbstractPersistenceXmlContextNode(JpaContextNode parent) {
		super(parent);
	}


	// ********** convenience methods **********

	protected PersistenceXmlDefinition getPersistenceXmlDefinition() {
		return (PersistenceXmlDefinition) this.getJpaPlatform().getResourceDefinition(this.getResourceType());
	}

	protected EFactory getResourceNodeFactory() {
		return this.getPersistenceXmlDefinition().getResourceNodeFactory();
	}

	protected PersistenceXmlContextNodeFactory getContextNodeFactory() {
		return this.getPersistenceXmlDefinition().getContextNodeFactory();
	}
}
