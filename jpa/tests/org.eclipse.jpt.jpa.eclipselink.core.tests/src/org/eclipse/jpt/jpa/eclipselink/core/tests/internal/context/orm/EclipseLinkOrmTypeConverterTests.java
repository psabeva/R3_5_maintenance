/*******************************************************************************
 * Copyright (c) 2008, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.tests.internal.context.orm;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.common.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.jpa.core.MappingKeys;
import org.eclipse.jpt.jpa.core.context.BasicMapping;
import org.eclipse.jpt.jpa.core.context.java.JavaBasicMapping;
import org.eclipse.jpt.jpa.core.context.orm.OrmBasicMapping;
import org.eclipse.jpt.jpa.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.jpa.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.jpa.core.context.orm.OrmReadOnlyPersistentAttribute;
import org.eclipse.jpt.jpa.core.resource.java.JPA;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkConvert;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkTypeConverter;
import org.eclipse.jpt.jpa.eclipselink.core.internal.context.orm.OrmEclipseLinkTypeConverter;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLink;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlBasic;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlTypeConverter;

@SuppressWarnings("nls")
public class EclipseLinkOrmTypeConverterTests
	extends EclipseLinkOrmContextModelTestCase
{
	
	private ICompilationUnit createTestEntityWithBasicMapping() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.BASIC, EclipseLink.CONVERT, EclipseLink.TYPE_CONVERTER);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Basic").append(CR);
				sb.append("    @Convert(name=\"foo\")").append(CR);
				sb.append("    @TypeConverter");
			}
		});
	}

	
	public EclipseLinkOrmTypeConverterTests(String name) {
		super(name);
	}
	
	
	public void testUpdateDataType() throws Exception {
		createTestEntityWithBasicMapping();
		
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedAttribute(MappingKeys.BASIC_ATTRIBUTE_MAPPING_KEY, "id");
		OrmBasicMapping ormBasicMapping = (OrmBasicMapping) ormPersistentAttribute.getMapping(); 
		ormBasicMapping.setConverter(EclipseLinkConvert.class);
		((EclipseLinkConvert) ormBasicMapping.getConverter()).setConverter(EclipseLinkTypeConverter.class);
		EclipseLinkTypeConverter ormConverter = (EclipseLinkTypeConverter) ((EclipseLinkConvert) ormBasicMapping.getConverter()).getConverter();
		XmlTypeConverter converterResource = ((XmlBasic) getXmlEntityMappings().getEntities().get(0).getAttributes().getBasics().get(0)).getTypeConverter();
		JavaBasicMapping javaBasicMapping = (JavaBasicMapping) ormPersistentType.getJavaPersistentType().getAttributeNamed("id").getMapping();
		
		assertEquals(null, ormConverter.getDataType());
		assertEquals(null, converterResource.getDataType());
				
		//set converter class name in the resource model, verify context model updated
		converterResource.setDataType("myConvert");
		assertEquals("myConvert", ormConverter.getDataType());
		assertEquals("myConvert", converterResource.getDataType());

		//set converter class name to null in the resource model
		converterResource.setDataType(null);
		assertEquals(null, ormConverter.getDataType());
		assertEquals(null, converterResource.getDataType());
				
		//remove the specified persistent attribute, test virtual mapping	
		ormPersistentAttribute.convertToVirtual();
		OrmReadOnlyPersistentAttribute ormPersistentAttribute2 = ormPersistentType.getAttributeNamed("id");
		BasicMapping virtualBasicMapping = (BasicMapping) ormPersistentAttribute2.getMapping();
		ormConverter = (EclipseLinkTypeConverter) ((EclipseLinkConvert) virtualBasicMapping.getConverter()).getConverter();
		EclipseLinkTypeConverter javaConverter = ((EclipseLinkTypeConverter) ((EclipseLinkConvert) javaBasicMapping.getConverter()).getConverter());
		javaConverter.setDataType("bar");
		
		assertNull(ormConverter);
		assertEquals("bar", javaConverter.getDataType());
		
		//set metadata-complete, test virtual mapping	
		ormPersistentType.getMapping().setSpecifiedMetadataComplete(Boolean.TRUE);
		ormPersistentAttribute2 = ormPersistentType.getAttributeNamed("id");
		virtualBasicMapping = (BasicMapping) ormPersistentAttribute2.getMapping();
		
		assertNull(virtualBasicMapping.getConverter().getType());
		assertEquals("bar", javaConverter.getDataType());
	}
	
	public void testModifyDataType() throws Exception {
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedAttribute(MappingKeys.BASIC_ATTRIBUTE_MAPPING_KEY, "basicMapping");
		OrmBasicMapping ormBasicMapping = ((OrmBasicMapping) ormPersistentAttribute.getMapping()); 
		ormBasicMapping.setConverter(EclipseLinkConvert.class);
		((EclipseLinkConvert) ormBasicMapping.getConverter()).setConverter(EclipseLinkTypeConverter.class);
		EclipseLinkTypeConverter ormConverter = (EclipseLinkTypeConverter) ((EclipseLinkConvert) ormBasicMapping.getConverter()).getConverter();
		XmlTypeConverter converterResource = ((XmlBasic) getXmlEntityMappings().getEntities().get(0).getAttributes().getBasics().get(0)).getTypeConverter();
	
		assertEquals(null, ormConverter.getDataType());
		assertEquals(null, converterResource.getDataType());
				
		//set converter class in the context model, verify resource model updated
		ormConverter.setDataType("foo");
		assertEquals("foo", ormConverter.getDataType());
		assertEquals("foo", converterResource.getDataType());
	
		ormConverter.setDataType(null);
		assertEquals(null, ormConverter.getDataType());
		assertEquals(null, converterResource.getDataType());
	}
	
	public void testUpdateObjectType() throws Exception {
		createTestEntityWithBasicMapping();
		
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedAttribute(MappingKeys.BASIC_ATTRIBUTE_MAPPING_KEY, "id");
		OrmBasicMapping ormBasicMapping = (OrmBasicMapping) ormPersistentAttribute.getMapping(); 
		ormBasicMapping.setConverter(EclipseLinkConvert.class);
		((EclipseLinkConvert) ormBasicMapping.getConverter()).setConverter(EclipseLinkTypeConverter.class);
		EclipseLinkTypeConverter ormConverter = (EclipseLinkTypeConverter) ((EclipseLinkConvert) ormBasicMapping.getConverter()).getConverter();
		XmlTypeConverter converterResource = ((XmlBasic) getXmlEntityMappings().getEntities().get(0).getAttributes().getBasics().get(0)).getTypeConverter();
		JavaBasicMapping javaBasicMapping = (JavaBasicMapping) ormPersistentType.getJavaPersistentType().getAttributeNamed("id").getMapping();
		
		assertEquals(null, ormConverter.getObjectType());
		assertEquals(null, converterResource.getObjectType());
				
		//set converter class name in the resource model, verify context model updated
		converterResource.setObjectType("myConvert");
		assertEquals("myConvert", ormConverter.getObjectType());
		assertEquals("myConvert", converterResource.getObjectType());

		//set converter class name to null in the resource model
		converterResource.setObjectType(null);
		assertEquals(null, ormConverter.getObjectType());
		assertEquals(null, converterResource.getObjectType());
		
		//remove the specified persistent attribute, test virtual mapping	
		ormPersistentAttribute.convertToVirtual();
		OrmReadOnlyPersistentAttribute ormPersistentAttribute2 = ormPersistentType.getAttributeNamed("id");
		BasicMapping virtualBasicMapping = (BasicMapping) ormPersistentAttribute2.getMapping();
		ormConverter = (EclipseLinkTypeConverter) ((EclipseLinkConvert) virtualBasicMapping.getConverter()).getConverter();
		EclipseLinkTypeConverter javaConverter = ((EclipseLinkTypeConverter) ((EclipseLinkConvert) javaBasicMapping.getConverter()).getConverter());
		javaConverter.setObjectType("bar");
		
		assertNull(ormConverter);
		assertEquals("bar", javaConverter.getObjectType());
		
		//set metadata-complete, test virtual mapping	
		ormPersistentType.getMapping().setSpecifiedMetadataComplete(Boolean.TRUE);
		ormPersistentAttribute2 = ormPersistentType.getAttributeNamed("id");
		virtualBasicMapping = (BasicMapping) ormPersistentAttribute2.getMapping();
		
		assertNull(virtualBasicMapping.getConverter().getType());
		assertEquals("bar", javaConverter.getObjectType());
	}
	
	public void testModifyObjectType() throws Exception {
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedAttribute(MappingKeys.BASIC_ATTRIBUTE_MAPPING_KEY, "basicMapping");
		OrmBasicMapping ormBasicMapping = ((OrmBasicMapping) ormPersistentAttribute.getMapping()); 
		ormBasicMapping.setConverter(EclipseLinkConvert.class);
		((EclipseLinkConvert) ormBasicMapping.getConverter()).setConverter(EclipseLinkTypeConverter.class);
		EclipseLinkTypeConverter ormConverter = (EclipseLinkTypeConverter) ((EclipseLinkConvert) ormBasicMapping.getConverter()).getConverter();
		XmlTypeConverter converterResource = ((XmlBasic) getXmlEntityMappings().getEntities().get(0).getAttributes().getBasics().get(0)).getTypeConverter();
	
		assertEquals(null, ormConverter.getObjectType());
		assertEquals(null, converterResource.getObjectType());
				
		//set converter class in the context model, verify resource model updated
		ormConverter.setObjectType("foo");
		assertEquals("foo", ormConverter.getObjectType());
		assertEquals("foo", converterResource.getObjectType());
	
		ormConverter.setObjectType(null);
		assertEquals(null, ormConverter.getObjectType());
		assertEquals(null, converterResource.getObjectType());
	}
	
	public void testUpdateName() throws Exception {
		createTestEntityWithBasicMapping();
		
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedAttribute(MappingKeys.BASIC_ATTRIBUTE_MAPPING_KEY, "id");
		OrmBasicMapping ormBasicMapping = ((OrmBasicMapping) ormPersistentAttribute.getMapping()); 
		ormBasicMapping.setConverter(EclipseLinkConvert.class);
		((EclipseLinkConvert) ormBasicMapping.getConverter()).setConverter(EclipseLinkTypeConverter.class);
		OrmEclipseLinkTypeConverter ormConverter = (OrmEclipseLinkTypeConverter) ((EclipseLinkConvert) ormBasicMapping.getConverter()).getConverter();
		XmlTypeConverter converterResource = ((XmlBasic) getXmlEntityMappings().getEntities().get(0).getAttributes().getBasics().get(0)).getTypeConverter();
		JavaBasicMapping javaBasicMapping = (JavaBasicMapping) ormPersistentType.getJavaPersistentType().getAttributeNamed("id").getMapping();
		
		assertEquals(null, ormConverter.getName());
		assertEquals(null, converterResource.getName());
				
		//set converter class name in the resource model, verify context model updated
		converterResource.setName("myConvert");
		assertEquals("myConvert", ormConverter.getName());
		assertEquals("myConvert", converterResource.getName());

		//set converter class name to null in the resource model
		converterResource.setName(null);
		assertEquals(null, ormConverter.getName());
		assertEquals(null, converterResource.getName());
		
		//remove the specified persistent attribute, test virtual mapping	
		ormPersistentAttribute.convertToVirtual();
		OrmReadOnlyPersistentAttribute ormPersistentAttribute2 = ormPersistentType.getAttributeNamed("id");
		BasicMapping virtualBasicMapping = (BasicMapping) ormPersistentAttribute2.getMapping();
		EclipseLinkTypeConverter virtualConverter = (EclipseLinkTypeConverter) ((EclipseLinkConvert) virtualBasicMapping.getConverter()).getConverter();
		EclipseLinkTypeConverter javaConverter = ((EclipseLinkTypeConverter) ((EclipseLinkConvert) javaBasicMapping.getConverter()).getConverter());
		javaConverter.setName("bar");
		
		assertNull(virtualConverter);
		assertEquals("bar", javaConverter.getName());
		
		//set metadata-complete, test virtual mapping
		ormPersistentType.getMapping().setSpecifiedMetadataComplete(Boolean.TRUE);
		ormPersistentAttribute2 = ormPersistentType.getVirtualAttributes().iterator().next();
		virtualBasicMapping = (BasicMapping) ormPersistentAttribute2.getMapping();
		
		assertNull(virtualBasicMapping.getConverter().getType());
		assertEquals("bar", javaConverter.getName());
	}
	
	public void testModifyName() throws Exception {
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, "model.Foo");
		OrmPersistentAttribute ormPersistentAttribute = ormPersistentType.addSpecifiedAttribute(MappingKeys.BASIC_ATTRIBUTE_MAPPING_KEY, "basicMapping");
		OrmBasicMapping ormBasicMapping = ((OrmBasicMapping) ormPersistentAttribute.getMapping()); 
		ormBasicMapping.setConverter(EclipseLinkConvert.class);
		((EclipseLinkConvert) ormBasicMapping.getConverter()).setConverter(EclipseLinkTypeConverter.class);
		OrmEclipseLinkTypeConverter ormConverter = (OrmEclipseLinkTypeConverter) ((EclipseLinkConvert) ormBasicMapping.getConverter()).getConverter();
		XmlTypeConverter converterResource = ((XmlBasic) getXmlEntityMappings().getEntities().get(0).getAttributes().getBasics().get(0)).getTypeConverter();
	
		assertEquals(null, ormConverter.getName());
		assertEquals(null, converterResource.getName());
				
		//set converter class in the context model, verify resource model updated
		ormConverter.setName("foo");
		assertEquals("foo", ormConverter.getName());
		assertEquals("foo", converterResource.getName());
	
		ormConverter.setName(null);
		assertEquals(null, ormConverter.getName());
		assertEquals(null, converterResource.getName());
	}
}
