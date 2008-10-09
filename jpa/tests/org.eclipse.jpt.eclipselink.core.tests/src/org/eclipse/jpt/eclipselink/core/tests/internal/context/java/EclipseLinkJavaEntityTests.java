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
import org.eclipse.jpt.eclipselink.core.context.Customizer;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkEntity;
import org.eclipse.jpt.eclipselink.core.resource.java.CustomizerAnnotation;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLinkJPA;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;

public class EclipseLinkJavaEntityTests extends EclipseLinkJavaContextModelTestCase
{

	private void createCustomizerAnnotation() throws Exception {
		this.createAnnotationAndMembers(EclipseLinkJPA.PACKAGE, "Customizer", "Class value();");		
	}
	
	private ICompilationUnit createTestEntityWithConvertAndCustomizerClass() throws Exception {
		createCustomizerAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, EclipseLinkJPA.CUSTOMIZER);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
				sb.append("    @Customizer(Foo.class");
			}
		});
	}

	public EclipseLinkJavaEntityTests(String name) {
		super(name);
	}


	public void testGetCustomizerClass() throws Exception {
		createTestEntityWithConvertAndCustomizerClass();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		Customizer customizer = ((EclipseLinkEntity) javaPersistentType().getMapping()).getCustomizer();
		
		assertEquals("Foo", customizer.getCustomizerClass());
	}

	public void testSetCustomizerClass() throws Exception {
		createTestEntityWithConvertAndCustomizerClass();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		Customizer customizer = ((EclipseLinkEntity) javaPersistentType().getMapping()).getCustomizer();
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
		createTestEntityWithConvertAndCustomizerClass();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		EclipseLinkEntity entity = (EclipseLinkEntity) javaPersistentType().getMapping();
		Customizer customizer = entity.getCustomizer();

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
}
