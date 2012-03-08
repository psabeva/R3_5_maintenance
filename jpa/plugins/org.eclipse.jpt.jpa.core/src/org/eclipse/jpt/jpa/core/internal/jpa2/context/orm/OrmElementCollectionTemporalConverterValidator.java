/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa2.context.orm;

import org.eclipse.jpt.jpa.core.context.BaseTemporalConverter;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.AbstractTemporalConverterValidator;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.ConverterTextRangeResolver;
import org.eclipse.jpt.jpa.core.internal.validation.JpaValidationMessages;
import org.eclipse.jpt.jpa.core.jpa2.context.orm.OrmElementCollectionMapping2_0;

public class OrmElementCollectionTemporalConverterValidator extends AbstractTemporalConverterValidator
{

	public OrmElementCollectionTemporalConverterValidator(BaseTemporalConverter converter, ConverterTextRangeResolver textRangeResolver) {
		super(converter, textRangeResolver);
	}

	@Override
	protected OrmElementCollectionMapping2_0 getAttributeMapping() {
		return (OrmElementCollectionMapping2_0) super.getAttributeMapping();
	}

	@Override
	protected String getTypeName() {
		return this.getAttributeMapping().getFullyQualifiedTargetClass();
	}

	@Override
	protected String getInvalidTemporalMappingType() {
		return JpaValidationMessages.PERSISTENT_ATTRIBUTE_ELEMENT_COLLECTION_INVALID_VALUE_TYPE;
	}

	@Override
	protected String getVirtualAttributeInvalidTemporalMappingType() {
		throw new UnsupportedOperationException();
	}

}
