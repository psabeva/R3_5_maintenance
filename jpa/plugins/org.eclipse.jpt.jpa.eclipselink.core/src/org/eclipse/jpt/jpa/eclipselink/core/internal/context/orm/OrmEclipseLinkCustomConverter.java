/*******************************************************************************
 * Copyright (c) 2008, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.internal.context.orm;

import org.eclipse.jdt.core.IType;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.utility.internal.StringTools;
import org.eclipse.jpt.jpa.core.context.XmlContextNode;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkConverter;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkCustomConverter;
import org.eclipse.jpt.jpa.eclipselink.core.internal.EclipseLinkJpaValidationMessages;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlConverter;
import org.eclipse.text.edits.ReplaceEdit;

public class OrmEclipseLinkCustomConverter
	extends OrmEclipseLinkConverterClassConverter<XmlConverter>
	implements EclipseLinkCustomConverter
{
	public OrmEclipseLinkCustomConverter(XmlContextNode parent, XmlConverter xmlConverter) {
		super(parent, xmlConverter, xmlConverter.getClassName());
	}


	// ********** converter class **********

	@Override
	protected String getXmlConverterClass() {
		return this.xmlConverter.getClassName();
	}

	@Override
	protected void setXmlConverterClass(String converterClass) {
		this.xmlConverter.setClassName(converterClass);
	}


	// ********** misc **********

	public Class<EclipseLinkCustomConverter> getType() {
		return EclipseLinkCustomConverter.class;
	}


	// ********** refactoring **********

	@Override
	protected ReplaceEdit createRenameEdit(IType originalType, String newName) {
		return this.xmlConverter.createRenameEdit(originalType, newName);
	}

	@Override
	protected ReplaceEdit createRenamePackageEdit(String newName) {
		return this.xmlConverter.createRenamePackageEdit(newName);
	}


	// ********** validation **********
	
	@Override
	protected String getEclipseLinkConverterInterface() {
		return ECLIPSELINK_CONVERTER_CLASS_NAME;
	}

	@Override
	protected String getEclipseLinkConverterInterfaceErrorMessage() {
		return EclipseLinkJpaValidationMessages.CONVERTER_CLASS_IMPLEMENTS_CONVERTER;
	}

	@Override
	protected TextRange getXmlConverterClassTextRange() {
		return this.xmlConverter.getConverterClassTextRange();
	}

	@Override
	public boolean isIdentical(EclipseLinkConverter eclipseLinkConverter) {
		return super.isIdentical(eclipseLinkConverter) && 
				StringTools.stringsAreEqual(this.getConverterClass(), ((EclipseLinkCustomConverter)eclipseLinkConverter).getConverterClass());
	}
}
