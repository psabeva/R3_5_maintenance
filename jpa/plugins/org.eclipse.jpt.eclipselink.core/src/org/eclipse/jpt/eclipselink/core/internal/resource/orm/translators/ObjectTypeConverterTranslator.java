/*******************************************************************************
 *  Copyright (c) 2008 Oracle. All rights reserved. This
 *  program and the accompanying materials are made available under the terms of
 *  the Eclipse Public License v1.0 which accompanies this distribution, and is
 *  available at http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: Oracle. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.resource.orm.translators;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmFactory;
import org.eclipse.wst.common.internal.emf.resource.Translator;

public class ObjectTypeConverterTranslator extends Translator
	implements EclipseLinkOrmXmlMapper
{	
	private Translator[] children;	
	
	
	public ObjectTypeConverterTranslator(String domNameAndPath, EStructuralFeature aFeature) {
		super(domNameAndPath, aFeature);
	}
	
	@Override
	public EObject createEMFObject(@SuppressWarnings("unused") String nodeName, @SuppressWarnings("unused") String readAheadName) {
		return EclipseLinkOrmFactory.eINSTANCE.createXmlObjectTypeConverterImpl();
	}
	
	@Override
	public Translator[] getChildren(@SuppressWarnings("unused") Object target, @SuppressWarnings("unused") int versionID) {
		if (this.children == null) {
			this.children = createChildren();
		}
		return this.children;
	}
		
	protected Translator[] createChildren() {
		return new Translator[] {
			createConversionValueTranslator(),
			createDefaultObjectValueTranslator(),
			createNameTranslator(),
			createDataTypeTranslator(),
			createObjectTypeTranslator(),
		};
	}
	
	protected Translator createConversionValueTranslator() {
		return new ConversionValueTranslator();
	}
	
	protected Translator createDefaultObjectValueTranslator() {
		return new Translator(OBJECT_TYPE_CONVERTER__DEFAULT_OBJECT_VALUE, ECLIPSELINK_ORM_PKG.getXmlObjectTypeConverter_DefaultObjectValue());
	}
	
	protected Translator createNameTranslator() {
		return new Translator(OBJECT_TYPE_CONVERTER__NAME, ECLIPSELINK_ORM_PKG.getXmlObjectTypeConverter_Name(), DOM_ATTRIBUTE);
	}
	
	protected Translator createDataTypeTranslator() {
		return new Translator(OBJECT_TYPE_CONVERTER__DATA_TYPE, ECLIPSELINK_ORM_PKG.getXmlObjectTypeConverter_DataType(), DOM_ATTRIBUTE);
	}
	
	protected Translator createObjectTypeTranslator() {
		return new Translator(OBJECT_TYPE_CONVERTER__OBJECT_TYPE, ECLIPSELINK_ORM_PKG.getXmlObjectTypeConverter_ObjectType(), DOM_ATTRIBUTE);
	}
}
