/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.internal.IMappingKeys;
import org.eclipse.jpt.core.internal.ITextRange;
import org.eclipse.jpt.core.internal.context.base.IAttributeOverride;
import org.eclipse.jpt.core.internal.context.base.IColumnMapping;
import org.eclipse.jpt.core.internal.context.base.IEmbeddable;
import org.eclipse.jpt.core.internal.context.base.IEmbeddedIdMapping;
import org.eclipse.jpt.core.internal.context.base.IOverride;
import org.eclipse.jpt.core.internal.context.base.IPersistentAttribute;
import org.eclipse.jpt.core.internal.context.java.JavaEmbeddedMapping;
import org.eclipse.jpt.core.internal.resource.orm.AttributeMapping;
import org.eclipse.jpt.core.internal.resource.orm.AttributeOverride;
import org.eclipse.jpt.core.internal.resource.orm.EmbeddedId;
import org.eclipse.jpt.core.internal.resource.orm.OrmFactory;
import org.eclipse.jpt.core.internal.resource.orm.TypeMapping;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.iterators.CloneListIterator;
import org.eclipse.jpt.utility.internal.iterators.EmptyIterator;
import org.eclipse.jpt.utility.internal.iterators.EmptyListIterator;
import org.eclipse.jpt.utility.internal.iterators.FilteringIterator;
import org.eclipse.jpt.utility.internal.iterators.TransformationIterator;


public class XmlEmbeddedIdMapping extends XmlAttributeMapping<EmbeddedId> implements IEmbeddedIdMapping
{
	protected final List<XmlAttributeOverride> specifiedAttributeOverrides;
	
	protected final List<XmlAttributeOverride> defaultAttributeOverrides;

	private IEmbeddable embeddable;
	
	protected XmlEmbeddedIdMapping(XmlPersistentAttribute parent) {
		super(parent);
		this.specifiedAttributeOverrides = new ArrayList<XmlAttributeOverride>();
		this.defaultAttributeOverrides = new ArrayList<XmlAttributeOverride>();
	}

	@Override
	protected void initializeOn(XmlAttributeMapping<? extends AttributeMapping> newMapping) {
		newMapping.initializeFromXmlEmbeddedIdMapping(this);
	}

	@Override
	public void initializeFromXmlEmbeddedMapping(XmlEmbeddedMapping oldMapping) {
		super.initializeFromXmlEmbeddedMapping(oldMapping);
		int index = 0;
		for (IAttributeOverride attributeOverride : CollectionTools.iterable(oldMapping.specifiedAttributeOverrides())) {
			XmlAttributeOverride newAttributeOverride = addSpecifiedAttributeOverride(index++);
			newAttributeOverride.setName(attributeOverride.getName());
			newAttributeOverride.getColumn().initializeFrom(attributeOverride.getColumn());
		}
	}
	
	@Override
	public int xmlSequence() {
		return 7;
	}

	public String getKey() {
		return IMappingKeys.EMBEDDED_ID_ATTRIBUTE_MAPPING_KEY;
	}

	@SuppressWarnings("unchecked")
	public ListIterator<XmlAttributeOverride> attributeOverrides() {
		//TODO attribute overrides
		return EmptyListIterator.instance();
	}

	public int attributeOverridesSize() {
		// TODO attributes overrides size
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	public ListIterator<XmlAttributeOverride> defaultAttributeOverrides() {
		return new CloneListIterator<XmlAttributeOverride>(this.defaultAttributeOverrides);
	}
	
	public int defaultAttributeOverridesSize() {
		return this.defaultAttributeOverrides.size();
	}
	
	@SuppressWarnings("unchecked")
	public ListIterator<XmlAttributeOverride> specifiedAttributeOverrides() {
		return new CloneListIterator<XmlAttributeOverride>(this.specifiedAttributeOverrides);
	}

	public int specifiedAttributeOverridesSize() {
		return this.specifiedAttributeOverrides.size();
	}

	public XmlAttributeOverride addSpecifiedAttributeOverride(int index) {
		XmlAttributeOverride attributeOverride = new XmlAttributeOverride(this, this);
		this.specifiedAttributeOverrides.add(index, attributeOverride);
		AttributeOverride attributeOverrideResource = OrmFactory.eINSTANCE.createAttributeOverrideImpl();
		this.attributeMapping().getAttributeOverrides().add(index, attributeOverrideResource);
		attributeOverride.initialize(attributeOverrideResource);
		this.fireItemAdded(IEmbeddedIdMapping.SPECIFIED_ATTRIBUTE_OVERRIDES_LIST, index, attributeOverride);
		return attributeOverride;
	}

	protected void addSpecifiedAttributeOverride(int index, XmlAttributeOverride attributeOverride) {
		addItemToList(index, attributeOverride, this.specifiedAttributeOverrides, IEmbeddedIdMapping.SPECIFIED_ATTRIBUTE_OVERRIDES_LIST);
	}
	
	public void removeSpecifiedAttributeOverride(IAttributeOverride attributeOverride) {
		removeSpecifiedAttributeOverride(this.specifiedAttributeOverrides.indexOf(attributeOverride));
	}
	
	public void removeSpecifiedAttributeOverride(int index) {
		XmlAttributeOverride removedAttributeOverride = this.specifiedAttributeOverrides.remove(index);
		this.attributeMapping().getAttributeOverrides().remove(index);
		fireItemRemoved(IEmbeddedIdMapping.SPECIFIED_ATTRIBUTE_OVERRIDES_LIST, index, removedAttributeOverride);
	}
	
	protected void removeSpecifiedAttributeOverride_(XmlAttributeOverride attributeOverride) {
		removeItemFromList(attributeOverride, this.specifiedAttributeOverrides, IEmbeddedIdMapping.SPECIFIED_ATTRIBUTE_OVERRIDES_LIST);
	}
	
	public void moveSpecifiedAttributeOverride(int targetIndex, int sourceIndex) {
		CollectionTools.move(this.specifiedAttributeOverrides, targetIndex, sourceIndex);
		this.attributeMapping().getAttributeOverrides().move(targetIndex, sourceIndex);
		fireItemMoved(IEmbeddedIdMapping.SPECIFIED_ATTRIBUTE_OVERRIDES_LIST, targetIndex, sourceIndex);		
	}

	public boolean isVirtual(IOverride override) {
		return this.defaultAttributeOverrides.contains(override);
	}

	public IColumnMapping columnMapping(String attributeName) {
		return JavaEmbeddedMapping.columnMapping(attributeName, embeddable());
	}

	public ITextRange validationTextRange(CompilationUnit astRoot) {
		// TODO Auto-generated method stub
		return null;
	}

//
//	public EList<IAttributeOverride> getAttributeOverrides() {
//		EList<IAttributeOverride> list = new EObjectEList<IAttributeOverride>(IAttributeOverride.class, this, OrmPackage.XML_EMBEDDED__ATTRIBUTE_OVERRIDES);
//		list.addAll(getSpecifiedAttributeOverrides());
//		list.addAll(getDefaultAttributeOverrides());
//		return list;
//	}
//
//	public EList<IAttributeOverride> getSpecifiedAttributeOverrides() {
//		if (specifiedAttributeOverrides == null) {
//			specifiedAttributeOverrides = new EObjectContainmentEList<IAttributeOverride>(IAttributeOverride.class, this, OrmPackage.XML_EMBEDDED__SPECIFIED_ATTRIBUTE_OVERRIDES);
//		}
//		return specifiedAttributeOverrides;
//	}
//
//	public EList<IAttributeOverride> getDefaultAttributeOverrides() {
//		if (defaultAttributeOverrides == null) {
//			defaultAttributeOverrides = new EObjectContainmentEList<IAttributeOverride>(IAttributeOverride.class, this, OrmPackage.XML_EMBEDDED__DEFAULT_ATTRIBUTE_OVERRIDES);
//		}
//		return defaultAttributeOverrides;
//	}
//
//	public IEmbeddable embeddable() {
//		return this.embeddable;
//	}
//
//	public IAttributeOverride attributeOverrideNamed(String name) {
//		return (IAttributeOverride) overrideNamed(name, getAttributeOverrides());
//	}
//
//	public boolean containsAttributeOverride(String name) {
//		return containsOverride(name, getAttributeOverrides());
//	}
//
//	public boolean containsSpecifiedAttributeOverride(String name) {
//		return containsOverride(name, getSpecifiedAttributeOverrides());
//	}
//
//	private IOverride overrideNamed(String name, List<? extends IOverride> overrides) {
//		for (IOverride override : overrides) {
//			String overrideName = override.getName();
//			if (overrideName == null && name == null) {
//				return override;
//			}
//			if (overrideName != null && overrideName.equals(name)) {
//				return override;
//			}
//		}
//		return null;
//	}
//
//	private boolean containsOverride(String name, List<? extends IOverride> overrides) {
//		return overrideNamed(name, overrides) != null;
//	}
//
//	public Iterator<String> allOverridableAttributeNames() {
//		return new TransformationIterator<IPersistentAttribute, String>(this.allOverridableAttributes()) {
//			@Override
//			protected String transform(IPersistentAttribute attribute) {
//				return attribute.getName();
//			}
//		};
//	}
//
//	public Iterator<IPersistentAttribute> allOverridableAttributes() {
//		if (this.embeddable() == null) {
//			return EmptyIterator.instance();
//		}
//		return new FilteringIterator<IPersistentAttribute>(this.embeddable().getPersistentType().attributes()) {
//			@Override
//			protected boolean accept(Object o) {
//				return ((IPersistentAttribute) o).isOverridableAttribute();
//			}
//		};
//	}
//
//	public IAttributeOverride createAttributeOverride(int index) {
//		return OrmFactory.eINSTANCE.createXmlAttributeOverride(new AttributeOverrideOwner(this));
//	}
//

	
	public IEmbeddable embeddable() {
		return this.embeddable;
	}
	
	public Iterator<String> allOverridableAttributeNames() {
		return new TransformationIterator<IPersistentAttribute, String>(this.allOverridableAttributes()) {
			@Override
			protected String transform(IPersistentAttribute attribute) {
				return attribute.getName();
			}
		};
	}

	public Iterator<IPersistentAttribute> allOverridableAttributes() {
		if (this.embeddable() == null) {
			return EmptyIterator.instance();
		}
		return new FilteringIterator<IPersistentAttribute, IPersistentAttribute>(this.embeddable().persistentType().attributes()) {
			@Override
			protected boolean accept(IPersistentAttribute o) {
				return o.isOverridableAttribute();
			}
		};
	}
	
	@Override
	public void initialize(EmbeddedId embeddedId) {
		super.initialize(embeddedId);
		this.initializeSpecifiedAttributeOverrides(embeddedId);
	}
	
	protected void initializeSpecifiedAttributeOverrides(EmbeddedId embeddedId) {
		for (AttributeOverride attributeOverride : embeddedId.getAttributeOverrides()) {
			this.specifiedAttributeOverrides.add(createAttributeOverride(attributeOverride));
		}
	}

	protected XmlAttributeOverride createAttributeOverride(AttributeOverride attributeOverride) {
		XmlAttributeOverride xmlAttributeOverride = new XmlAttributeOverride(this, this);
		xmlAttributeOverride.initialize(attributeOverride);
		return xmlAttributeOverride;
	}

	
	@Override
	public void update(EmbeddedId embeddedId) {
		super.update(embeddedId);
		this.updateSpecifiedAttributeOverrides(embeddedId);
	}
	
	protected void updateSpecifiedAttributeOverrides(EmbeddedId embeddedId) {
		ListIterator<XmlAttributeOverride> attributeOverrides = specifiedAttributeOverrides();
		ListIterator<AttributeOverride> resourceAttributeOverrides = embeddedId.getAttributeOverrides().listIterator();
		
		while (attributeOverrides.hasNext()) {
			XmlAttributeOverride attributeOverride = attributeOverrides.next();
			if (resourceAttributeOverrides.hasNext()) {
				attributeOverride.update(resourceAttributeOverrides.next());
			}
			else {
				removeSpecifiedAttributeOverride_(attributeOverride);
			}
		}
		
		while (resourceAttributeOverrides.hasNext()) {
			addSpecifiedAttributeOverride(specifiedAttributeOverridesSize(), createAttributeOverride(resourceAttributeOverrides.next()));
		}
	}

	@Override
	public EmbeddedId addToResourceModel(TypeMapping typeMapping) {
		EmbeddedId embeddedId = OrmFactory.eINSTANCE.createEmbeddedIdImpl();
		persistentAttribute().initialize(embeddedId);
		typeMapping.getAttributes().getEmbeddedIds().add(embeddedId);
		return embeddedId;
	}
	
	@Override
	public void removeFromResourceModel(TypeMapping typeMapping) {
		typeMapping.getAttributes().getEmbeddedIds().remove(this.attributeMapping());
		if (typeMapping.getAttributes().isAllFeaturesUnset()) {
			typeMapping.setAttributes(null);
		}
	}
}
