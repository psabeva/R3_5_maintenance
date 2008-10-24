/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import java.util.ListIterator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.jpt.core.context.java.JavaAttributeOverride;
import org.eclipse.jpt.core.context.java.JavaEmbeddedIdMapping;
import org.eclipse.jpt.core.context.orm.OrmTypeMapping;
import org.eclipse.jpt.core.resource.orm.OrmPackage;
import org.eclipse.jpt.core.resource.orm.XmlAttributeOverride;
import org.eclipse.jpt.core.resource.orm.XmlColumn;
import org.eclipse.jpt.core.resource.orm.XmlEmbeddedId;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.utility.internal.CollectionTools;

/**
 * VirtualEmbeddedId is an implementation of EmbeddedId used when there is 
 * no tag in the orm.xml and an underlying javaEmbeddedIdMapping exists.
 */
public class VirtualXmlEmbeddedId extends VirtualXmlAttributeMapping<JavaEmbeddedIdMapping> implements XmlEmbeddedId
{
		
	public VirtualXmlEmbeddedId(OrmTypeMapping ormTypeMapping, JavaEmbeddedIdMapping javaEmbeddedIdMapping) {
		super(ormTypeMapping, javaEmbeddedIdMapping);
	}
	
	public String getName() {
		return this.javaAttributeMapping.getPersistentAttribute().getName();
	}

	public void setName(@SuppressWarnings("unused")String newName) {
		throw new UnsupportedOperationException("cannot set values on a virtual mapping"); //$NON-NLS-1$
	}

	public EList<XmlAttributeOverride> getAttributeOverrides() {
		EList<XmlAttributeOverride> attributeOverrides = new EObjectContainmentEList<XmlAttributeOverride>(XmlAttributeOverride.class, this, OrmPackage.XML_EMBEDDED_ID__ATTRIBUTE_OVERRIDES);
		ListIterator<JavaAttributeOverride> javaAttributeOverrides;
		if (!this.isOrmMetadataComplete()) {
			javaAttributeOverrides = this.javaAttributeMapping.attributeOverrides();
		}
		else {
			javaAttributeOverrides = this.javaAttributeMapping.virtualAttributeOverrides();
		}
		for (JavaAttributeOverride javaAttributeOverride : CollectionTools.iterable(javaAttributeOverrides)) {
			XmlColumn xmlColumn = new VirtualXmlColumn(this.ormTypeMapping, javaAttributeOverride.getColumn());
			XmlAttributeOverride xmlAttributeOverride = new VirtualXmlAttributeOverride(javaAttributeOverride.getName(), xmlColumn);
			attributeOverrides.add(xmlAttributeOverride);
		}
		return attributeOverrides;
	}

	public TextRange getNameTextRange() {
		return null;
	}
}
