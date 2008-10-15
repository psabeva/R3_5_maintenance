/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.tests.internal.context.java;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.eclipselink.core.context.ChangeTracking;
import org.eclipse.jpt.eclipselink.core.context.ChangeTrackingType;
import org.eclipse.jpt.eclipselink.core.context.Customizer;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkMappedSuperclass;
import org.eclipse.jpt.eclipselink.core.context.ReadOnly;
import org.eclipse.jpt.eclipselink.core.resource.java.ChangeTrackingAnnotation;
import org.eclipse.jpt.eclipselink.core.resource.java.CustomizerAnnotation;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLinkJPA;
import org.eclipse.jpt.eclipselink.core.resource.java.ReadOnlyAnnotation;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;

public class EclipseLinkJavaMappedSuperclassTests extends EclipseLinkJavaContextModelTestCase
{

	private void createReadOnlyAnnotation() throws Exception{
		this.createAnnotationAndMembers(EclipseLinkJPA.PACKAGE, "ReadOnly", "");		
	}
	
	private void createCustomizerAnnotation() throws Exception {
		this.createAnnotationAndMembers(EclipseLinkJPA.PACKAGE, "Customizer", "Class value();");		
	}

	private void createChangeTrackingAnnotation() throws Exception{
		createChangeTrackingTypeEnum();
		this.createAnnotationAndMembers(EclipseLinkJPA.PACKAGE, "ChangeTracking", "ChangeTrackingType value() default ChangeTrackingType.AUTO");		
	}
	
	private void createChangeTrackingTypeEnum() throws Exception {
		this.createEnumAndMembers(ECLIPSELINK_ANNOTATIONS_PACKAGE_NAME, "ChangeTrackingType", "ATTRIBUTE, OBJECT, DEFERRED, AUTO;");	
	}

	private ICompilationUnit createTestMappedSuperclassWithReadOnly() throws Exception {
		createReadOnlyAnnotation();
		
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.MAPPED_SUPERCLASS, EclipseLinkJPA.READ_ONLY);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@MappedSuperclass").append(CR);
				sb.append("@ReadOnly").append(CR);
			}
		});
	}
	
	private ICompilationUnit createTestMappedSuperclassWithConvertAndCustomizerClass() throws Exception {
		createCustomizerAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.MAPPED_SUPERCLASS, EclipseLinkJPA.CUSTOMIZER);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@MappedSuperclass").append(CR);
				sb.append("    @Customizer(Foo.class");
			}
		});
	}
	
	private ICompilationUnit createTestMappedSuperclassWithChangeTracking() throws Exception {
		createChangeTrackingAnnotation();
		
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.MAPPED_SUPERCLASS, EclipseLinkJPA.CHANGE_TRACKING);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@MappedSuperclass").append(CR);
				sb.append("    @ChangeTracking").append(CR);
			}
		});
	}

	public EclipseLinkJavaMappedSuperclassTests(String name) {
		super(name);
	}


	public void testGetReadOnly() throws Exception {
		createTestMappedSuperclassWithReadOnly();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		EclipseLinkMappedSuperclass mappedSuperclass = (EclipseLinkMappedSuperclass) javaPersistentType().getMapping();
		ReadOnly readOnly = mappedSuperclass.getReadOnly();
		assertEquals(Boolean.TRUE, readOnly.getReadOnly());
	}

	public void testGetSpecifiedReadOnly() throws Exception {
		createTestMappedSuperclassWithReadOnly();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		EclipseLinkMappedSuperclass mappedSuperclass = (EclipseLinkMappedSuperclass) javaPersistentType().getMapping();
		ReadOnly readOnly = mappedSuperclass.getReadOnly();
		assertEquals(Boolean.TRUE, readOnly.getSpecifiedReadOnly());
	}

	//TODO test inheriting a default readonly from you superclass
	public void testGetDefaultReadOnly() throws Exception {
		createTestMappedSuperclassWithReadOnly();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		EclipseLinkMappedSuperclass mappedSuperclass = (EclipseLinkMappedSuperclass) javaPersistentType().getMapping();
		ReadOnly readOnly = mappedSuperclass.getReadOnly();
		assertEquals(Boolean.FALSE, readOnly.getDefaultReadOnly());
	}

	public void testSetSpecifiedReadOnly() throws Exception {
		createTestMappedSuperclassWithReadOnly();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		EclipseLinkMappedSuperclass mappedSuperclass = (EclipseLinkMappedSuperclass) javaPersistentType().getMapping();
		ReadOnly readOnly = mappedSuperclass.getReadOnly();
		assertEquals(Boolean.TRUE, readOnly.getReadOnly());
		
		readOnly.setSpecifiedReadOnly(Boolean.FALSE);
		
		JavaResourcePersistentType typeResource = jpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		assertNull(typeResource.getAnnotation(ReadOnlyAnnotation.ANNOTATION_NAME));
		assertEquals(null, readOnly.getSpecifiedReadOnly());//Boolean.FALSE and null really mean the same thing since there are only 2 states in the java resource model

		readOnly.setSpecifiedReadOnly(Boolean.TRUE);
		assertNotNull(typeResource.getAnnotation(ReadOnlyAnnotation.ANNOTATION_NAME));
		assertEquals(Boolean.TRUE, readOnly.getSpecifiedReadOnly());

		readOnly.setSpecifiedReadOnly(null);
		assertNull(typeResource.getAnnotation(ReadOnlyAnnotation.ANNOTATION_NAME));
		assertEquals(null, readOnly.getSpecifiedReadOnly());//Boolean.FALSE and null really mean the same thing since there are only 2 states in the java resource model
	}
	
	public void testSpecifiedReadOnlyUpdatesFromResourceModelChange() throws Exception {
		createTestMappedSuperclassWithReadOnly();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		EclipseLinkMappedSuperclass mappedSuperclass = (EclipseLinkMappedSuperclass) javaPersistentType().getMapping();
		ReadOnly readOnly = mappedSuperclass.getReadOnly();
		assertEquals(Boolean.TRUE, readOnly.getSpecifiedReadOnly());
		
		
		JavaResourcePersistentType typeResource = jpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		typeResource.removeAnnotation(ReadOnlyAnnotation.ANNOTATION_NAME);
		
		assertEquals(null, readOnly.getSpecifiedReadOnly());
		assertEquals(Boolean.FALSE, readOnly.getDefaultReadOnly());
		
		typeResource.addAnnotation(ReadOnlyAnnotation.ANNOTATION_NAME);
		assertEquals(Boolean.TRUE, readOnly.getSpecifiedReadOnly());
	}

	public void testGetCustomizerClass() throws Exception {
		createTestMappedSuperclassWithConvertAndCustomizerClass();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		Customizer customizer = ((EclipseLinkMappedSuperclass) javaPersistentType().getMapping()).getCustomizer();
		
		assertEquals("Foo", customizer.getCustomizerClass());
	}

	public void testSetCustomizerClass() throws Exception {
		createTestMappedSuperclassWithConvertAndCustomizerClass();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		Customizer customizer = ((EclipseLinkMappedSuperclass) javaPersistentType().getMapping()).getCustomizer();
		assertEquals("Foo", customizer.getCustomizerClass());
		
		customizer.setCustomizerClass("Bar");
		assertEquals("Bar", customizer.getCustomizerClass());
			
		JavaResourcePersistentType typeResource = jpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		CustomizerAnnotation customizerAnnotation = (CustomizerAnnotation) typeResource.getAnnotation(CustomizerAnnotation.ANNOTATION_NAME);		
		assertEquals("Bar", customizerAnnotation.getValue());

		
		customizer.setCustomizerClass(null);
		assertEquals(null, customizer.getCustomizerClass());
		customizerAnnotation = (CustomizerAnnotation) typeResource.getAnnotation(CustomizerAnnotation.ANNOTATION_NAME);		
		assertEquals(null, customizerAnnotation);


		customizer.setCustomizerClass("Bar");
		assertEquals("Bar", customizer.getCustomizerClass());
		customizerAnnotation = (CustomizerAnnotation) typeResource.getAnnotation(CustomizerAnnotation.ANNOTATION_NAME);		
		assertEquals("Bar", customizerAnnotation.getValue());
	}
	
	public void testGetCustomizerClassUpdatesFromResourceModelChange() throws Exception {
		createTestMappedSuperclassWithConvertAndCustomizerClass();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		EclipseLinkMappedSuperclass mappedSuperclass = (EclipseLinkMappedSuperclass) javaPersistentType().getMapping();
		Customizer customizer = mappedSuperclass.getCustomizer();

		assertEquals("Foo", customizer.getCustomizerClass());
		
		JavaResourcePersistentType typeResource = jpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		CustomizerAnnotation customizerAnnotation = (CustomizerAnnotation) typeResource.getAnnotation(CustomizerAnnotation.ANNOTATION_NAME);
		customizerAnnotation.setValue("Bar");
		assertEquals("Bar", customizer.getCustomizerClass());
		
		typeResource.removeAnnotation(CustomizerAnnotation.ANNOTATION_NAME);
		assertEquals(null, customizer.getCustomizerClass());
		
		customizerAnnotation = (CustomizerAnnotation) typeResource.addAnnotation(CustomizerAnnotation.ANNOTATION_NAME);		
		assertEquals(null, customizer.getCustomizerClass());
		
		customizerAnnotation.setValue("FooBar");
		assertEquals("FooBar", customizer.getCustomizerClass());	
	}
	
	public void testHasChangeTracking() throws Exception {
		createTestMappedSuperclassWithChangeTracking();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		EclipseLinkMappedSuperclass mappedSuperclass = (EclipseLinkMappedSuperclass) javaPersistentType().getMapping();
		ChangeTracking changeTracking = mappedSuperclass.getChangeTracking();
		assertEquals(true, changeTracking.hasChangeTracking());
		
		JavaResourcePersistentType typeResource = jpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		typeResource.removeAnnotation(ChangeTrackingAnnotation.ANNOTATION_NAME);
		
		assertEquals(false, changeTracking.hasChangeTracking());
		
		typeResource.addAnnotation(ChangeTrackingAnnotation.ANNOTATION_NAME);
		assertEquals(true, changeTracking.hasChangeTracking());
	}
	
	public void testSetChangeTracking() throws Exception {
		createTestMappedSuperclassWithChangeTracking();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		EclipseLinkMappedSuperclass mappedSuperclass = (EclipseLinkMappedSuperclass) javaPersistentType().getMapping();
		ChangeTracking changeTracking = mappedSuperclass.getChangeTracking();
		assertEquals(true, changeTracking.hasChangeTracking());
		
		changeTracking.setChangeTracking(false);
		JavaResourcePersistentType typeResource = jpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		assertNull(typeResource.getAnnotation(ChangeTrackingAnnotation.ANNOTATION_NAME));
		assertFalse(changeTracking.hasChangeTracking());
		
		changeTracking.setChangeTracking(true);
		assertNotNull(typeResource.getAnnotation(ChangeTrackingAnnotation.ANNOTATION_NAME));
		assertTrue(changeTracking.hasChangeTracking());
	}
	
	public void testGetSpecifiedChangeTracking() throws Exception {
		createTestMappedSuperclassWithChangeTracking();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		EclipseLinkMappedSuperclass mappedSuperclass = (EclipseLinkMappedSuperclass) javaPersistentType().getMapping();
		ChangeTracking changeTracking = mappedSuperclass.getChangeTracking();
		assertEquals(null, changeTracking.getSpecifiedChangeTrackingType());
		
		JavaResourcePersistentType typeResource = jpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		ChangeTrackingAnnotation changeTrackingAnnotation = (ChangeTrackingAnnotation) typeResource.getAnnotation(ChangeTrackingAnnotation.ANNOTATION_NAME);
		changeTrackingAnnotation.setValue(org.eclipse.jpt.eclipselink.core.resource.java.ChangeTrackingType.OBJECT);
		
		assertEquals(ChangeTrackingType.OBJECT, changeTracking.getSpecifiedChangeTrackingType());

		changeTrackingAnnotation.setValue(null);
		assertEquals(null, changeTracking.getSpecifiedChangeTrackingType());

		changeTrackingAnnotation.setValue(org.eclipse.jpt.eclipselink.core.resource.java.ChangeTrackingType.DEFERRED);
		assertEquals(ChangeTrackingType.DEFERRED, changeTracking.getSpecifiedChangeTrackingType());
		
		typeResource.removeAnnotation(ChangeTrackingAnnotation.ANNOTATION_NAME);
		assertEquals(null, changeTracking.getSpecifiedChangeTrackingType());
	}
	
	public void testSetSpecifiedChangeTracking() throws Exception {
		createTestMappedSuperclassWithChangeTracking();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		EclipseLinkMappedSuperclass mappedSuperclass = (EclipseLinkMappedSuperclass) javaPersistentType().getMapping();
		ChangeTracking changeTracking = mappedSuperclass.getChangeTracking();
		assertEquals(null, changeTracking.getSpecifiedChangeTrackingType());
		
		JavaResourcePersistentType typeResource = jpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		ChangeTrackingAnnotation changeTrackingAnnotation = (ChangeTrackingAnnotation) typeResource.getAnnotation(ChangeTrackingAnnotation.ANNOTATION_NAME);
		assertEquals(null, changeTrackingAnnotation.getValue());
		
		changeTracking.setSpecifiedChangeTrackingType(ChangeTrackingType.OBJECT);	
		assertEquals(org.eclipse.jpt.eclipselink.core.resource.java.ChangeTrackingType.OBJECT, changeTrackingAnnotation.getValue());

		changeTracking.setSpecifiedChangeTrackingType(null);
		assertEquals(null, changeTrackingAnnotation.getValue());
		
		changeTracking.setSpecifiedChangeTrackingType(ChangeTrackingType.ATTRIBUTE);	
		assertEquals(org.eclipse.jpt.eclipselink.core.resource.java.ChangeTrackingType.ATTRIBUTE, changeTrackingAnnotation.getValue());
		
		changeTracking.setChangeTracking(false);
		assertNull(typeResource.getAnnotation(ChangeTrackingAnnotation.ANNOTATION_NAME));
	}
	
	public void testGetDefaultChangeTracking() throws Exception {
		createTestMappedSuperclassWithChangeTracking();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		EclipseLinkMappedSuperclass mappedSuperclass = (EclipseLinkMappedSuperclass) javaPersistentType().getMapping();
		ChangeTracking changeTracking = mappedSuperclass.getChangeTracking();
		assertEquals(ChangeTracking.DEFAULT_CHANGE_TRACKING_TYPE, changeTracking.getDefaultChangeTrackingType());
		
		JavaResourcePersistentType typeResource = jpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		typeResource.removeAnnotation(ChangeTrackingAnnotation.ANNOTATION_NAME);
		assertEquals(ChangeTracking.DEFAULT_CHANGE_TRACKING_TYPE, changeTracking.getDefaultChangeTrackingType());
		
		changeTracking.setSpecifiedChangeTrackingType(ChangeTrackingType.ATTRIBUTE);	
		assertEquals(ChangeTracking.DEFAULT_CHANGE_TRACKING_TYPE, changeTracking.getDefaultChangeTrackingType());
	}
	
	public void testGetChangeTracking() throws Exception {
		createTestMappedSuperclassWithChangeTracking();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		EclipseLinkMappedSuperclass mappedSuperclass = (EclipseLinkMappedSuperclass) javaPersistentType().getMapping();
		ChangeTracking changeTracking = mappedSuperclass.getChangeTracking();
		assertEquals(ChangeTracking.DEFAULT_CHANGE_TRACKING_TYPE, changeTracking.getChangeTrackingType());
		
		JavaResourcePersistentType typeResource = jpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		typeResource.removeAnnotation(ChangeTrackingAnnotation.ANNOTATION_NAME);
		assertEquals(ChangeTracking.DEFAULT_CHANGE_TRACKING_TYPE, changeTracking.getChangeTrackingType());
		
		changeTracking.setSpecifiedChangeTrackingType(ChangeTrackingType.DEFERRED);	
		assertEquals(ChangeTrackingType.DEFERRED, changeTracking.getChangeTrackingType());
	}
}
