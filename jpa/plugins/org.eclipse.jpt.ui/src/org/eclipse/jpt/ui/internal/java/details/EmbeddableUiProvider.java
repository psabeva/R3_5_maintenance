/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.java.details;

import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.Embeddable;
import org.eclipse.jpt.ui.details.JpaComposite;
import org.eclipse.jpt.ui.internal.mappings.JptUiMappingsMessages;
import org.eclipse.jpt.ui.internal.mappings.details.EmbeddableComposite;
import org.eclipse.jpt.ui.java.details.TypeMappingUiProvider;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

public class EmbeddableUiProvider implements TypeMappingUiProvider<Embeddable>
{
	// singleton
	private static final EmbeddableUiProvider INSTANCE = new EmbeddableUiProvider();

	/**
	 * Return the singleton.
	 */
	public static TypeMappingUiProvider<Embeddable> instance() {
		return INSTANCE;
	}

	/**
	 * Ensure non-instantiability.
	 */
	private EmbeddableUiProvider() {
		super();
	}

	public String mappingKey() {
		return MappingKeys.EMBEDDABLE_TYPE_MAPPING_KEY;
	}

	public String label() {
		return JptUiMappingsMessages.PersistentTypePage_EmbeddableLabel;
	}

	public JpaComposite<Embeddable> buildPersistentTypeMappingComposite(
			PropertyValueModel<Embeddable> subjectHolder,
			Composite parent,
			TabbedPropertySheetWidgetFactory widgetFactory) {

		return new EmbeddableComposite(subjectHolder, parent, widgetFactory);
	}
}