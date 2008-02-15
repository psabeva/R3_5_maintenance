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
import org.eclipse.jpt.core.internal.context.base.IEmbeddedMapping;
import org.eclipse.jpt.core.internal.context.base.IOverride;
import org.eclipse.jpt.core.internal.context.base.IPersistentAttribute;
import org.eclipse.jpt.core.internal.context.java.IJavaPersistentAttribute;
import org.eclipse.jpt.core.internal.context.java.JavaEmbeddedMapping;
import org.eclipse.jpt.core.internal.resource.orm.AttributeMapping;
import org.eclipse.jpt.core.internal.resource.orm.AttributeOverride;
import org.eclipse.jpt.core.internal.resource.orm.Embedded;
import org.eclipse.jpt.core.internal.resource.orm.OrmFactory;
import org.eclipse.jpt.core.internal.resource.orm.TypeMapping;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.iterators.CloneListIterator;
import org.eclipse.jpt.utility.internal.iterators.CompositeListIterator;
import org.eclipse.jpt.utility.internal.iterators.EmptyIterator;
import org.eclipse.jpt.utility.internal.iterators.FilteringIterator;
import org.eclipse.jpt.utility.internal.iterators.TransformationIterator;


public class XmlEmbeddedMapping extends XmlAttributeMapping<Embedded> implements IEmbeddedMapping
{
	protected final List<XmlAttributeOverride> specifiedAttributeOverrides;
	
	protected final List<XmlAttributeOverride> defaultAttributeOverrides;

	private IEmbeddable embeddable;
	
	protected XmlEmbeddedMapping(XmlPersistentAttribute parent) {
		super(parent);
		this.specifiedAttributeOverrides = new ArrayList<XmlAttributeOverride>();
		this.defaultAttributeOverrides = new ArrayList<XmlAttributeOverride>();
	}

	@Override
	protected void initializeOn(XmlAttributeMapping<? extends AttributeMapping> newMapping) {
		newMapping.initializeFromXmlEmbeddedMapping(this);
	}

	@Override
	public void initializeFromXmlEmbeddedIdMapping(XmlEmbeddedIdMapping oldMapping) {
		super.initializeFromXmlEmbeddedIdMapping(oldMapping);
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
		return IMappingKeys.EMBEDDED_ATTRIBUTE_MAPPING_KEY;
	}

	@SuppressWarnings("unchecked")
	public ListIterator<XmlAttributeOverride> attributeOverrides() {
		return new CompositeListIterator<XmlAttributeOverride>(specifiedAttributeOverrides(), defaultAttributeOverrides());
	}

	public int attributeOverridesSize() {
		// TODO Auto-generated method stub
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
		this.fireItemAdded(IEmbeddedMapping.SPECIFIED_ATTRIBUTE_OVERRIDES_LIST, index, attributeOverride);
		return attributeOverride;
	}

	protected void addSpecifiedAttributeOverride(int index, XmlAttributeOverride attributeOverride) {
		addItemToList(index, attributeOverride, this.specifiedAttributeOverrides, IEmbeddedMapping.SPECIFIED_ATTRIBUTE_OVERRIDES_LIST);
	}
	
	public void removeSpecifiedAttributeOverride(IAttributeOverride attributeOverride) {
		removeSpecifiedAttributeOverride(this.specifiedAttributeOverrides.indexOf(attributeOverride));
	}
	
	public void removeSpecifiedAttributeOverride(int index) {
		XmlAttributeOverride removedAttributeOverride = this.specifiedAttributeOverrides.remove(index);
		this.attributeMapping().getAttributeOverrides().remove(index);
		fireItemRemoved(IEmbeddedMapping.SPECIFIED_ATTRIBUTE_OVERRIDES_LIST, index, removedAttributeOverride);
	}
	
	protected void removeSpecifiedAttributeOverride_(XmlAttributeOverride attributeOverride) {
		removeItemFromList(attributeOverride, this.specifiedAttributeOverrides, IEmbeddedMapping.SPECIFIED_ATTRIBUTE_OVERRIDES_LIST);
	}
	
	public void moveSpecifiedAttributeOverride(int targetIndex, int sourceIndex) {
		CollectionTools.move(this.specifiedAttributeOverrides, targetIndex, sourceIndex);
		this.attributeMapping().getAttributeOverrides().move(targetIndex, sourceIndex);
		fireItemMoved(IEmbeddedMapping.SPECIFIED_ATTRIBUTE_OVERRIDES_LIST, targetIndex, sourceIndex);		
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
	public void initialize(Embedded embedded) {
		super.initialize(embedded);
		this.embeddable = embeddableFor(javaPersistentAttribute());
		this.initializeSpecifiedAttributeOverrides(embedded);
	}
	
	protected void initializeSpecifiedAttributeOverrides(Embedded embedded) {
		for (AttributeOverride attributeOverride : embedded.getAttributeOverrides()) {
			this.specifiedAttributeOverrides.add(createAttributeOverride(attributeOverride));
		}
	}
//	
//	protected void initializeDefaultAttributeOverrides(JavaPersistentAttributeResource persistentAttributeResource) {
//		for (Iterator<String> i = allOverridableAttributeNames(); i.hasNext(); ) {
//			String attributeName = i.next();
//			XmlAttributeOverride attributeOverride = attributeOverrideNamed(attributeName);
//			if (attributeOverride == null) {
//				attributeOverride = createAttributeOverride(new NullAttributeOverride(persistentAttributeResource));
//				attributeOverride.setName(attributeName);
//				this.defaultAttributeOverrides.add(attributeOverride);
//			}
//		}
//	}

	protected XmlAttributeOverride createAttributeOverride(AttributeOverride attributeOverride) {
		XmlAttributeOverride xmlAttributeOverride = new XmlAttributeOverride(this, this);
		xmlAttributeOverride.initialize(attributeOverride);
		return xmlAttributeOverride;
	}

	
	@Override
	public void update(Embedded embedded) {
		super.update(embedded);
		this.embeddable = embeddableFor(javaPersistentAttribute());
		this.updateSpecifiedAttributeOverrides(embedded);
	}
	
	protected void updateSpecifiedAttributeOverrides(Embedded embedded) {
		ListIterator<XmlAttributeOverride> attributeOverrides = specifiedAttributeOverrides();
		ListIterator<AttributeOverride> resourceAttributeOverrides = embedded.getAttributeOverrides().listIterator();
		
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
	public Embedded addToResourceModel(TypeMapping typeMapping) {
		Embedded embedded = OrmFactory.eINSTANCE.createEmbeddedImpl();
		persistentAttribute().initialize(embedded);
		typeMapping.getAttributes().getEmbeddeds().add(embedded);
		return embedded;
	}
	
	@Override
	public void removeFromResourceModel(TypeMapping typeMapping) {
		typeMapping.getAttributes().getEmbeddeds().remove(this.attributeMapping());
		if (typeMapping.getAttributes().isAllFeaturesUnset()) {
			typeMapping.setAttributes(null);
		}
	}
	
	//******* static methods *********
	public static IEmbeddable embeddableFor(IJavaPersistentAttribute javaPersistentAttribute) {
		if (javaPersistentAttribute == null) {
			return null;
		}
		return JavaEmbeddedMapping.embeddableFor(javaPersistentAttribute);
	}

}
