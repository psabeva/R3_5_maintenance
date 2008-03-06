/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.orm.details;

import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.orm.OrmEntity;
import org.eclipse.jpt.ui.JpaUiFactory;
import org.eclipse.jpt.ui.details.JpaComposite;
import org.eclipse.jpt.ui.details.TypeMappingUiProvider;
import org.eclipse.jpt.ui.internal.mappings.JptUiMappingsMessages;
import org.eclipse.jpt.ui.internal.widgets.WidgetFactory;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.swt.widgets.Composite;

public class OrmEntityUiProvider implements TypeMappingUiProvider<OrmEntity>
{
	// singleton
	private static final OrmEntityUiProvider INSTANCE = new OrmEntityUiProvider();

	/**
	 * Return the singleton.
	 */
	public static TypeMappingUiProvider<OrmEntity> instance() {
		return INSTANCE;
	}

	/**
	 * Ensure non-instantiability.
	 */
	private OrmEntityUiProvider() {
		super();
	}

	public String mappingKey() {
		return MappingKeys.ENTITY_TYPE_MAPPING_KEY;
	}

	public String label() {
		return JptUiMappingsMessages.PersistentTypePage_EntityLabel;
	}

	public JpaComposite<OrmEntity> buildPersistentTypeMappingComposite(
			JpaUiFactory factory,
			PropertyValueModel<OrmEntity> subjectHolder,
			Composite parent,
			WidgetFactory widgetFactory) {

		return factory.createOrmEntityComposite(subjectHolder, parent, widgetFactory);
	}
}
