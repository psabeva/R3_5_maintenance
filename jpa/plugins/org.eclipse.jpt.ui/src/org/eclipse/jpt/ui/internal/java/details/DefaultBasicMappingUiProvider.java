/*******************************************************************************
 * Copyright (c) 2006, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.java.details;

import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.BasicMapping;
import org.eclipse.jpt.ui.JpaUiFactory;
import org.eclipse.jpt.ui.details.AttributeMappingUiProvider;
import org.eclipse.jpt.ui.details.JpaComposite;
import org.eclipse.jpt.ui.internal.mappings.JptUiMappingsMessages;
import org.eclipse.jpt.ui.internal.widgets.WidgetFactory;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;

public class DefaultBasicMappingUiProvider
	implements AttributeMappingUiProvider<BasicMapping>
{
	// singleton
	private static final DefaultBasicMappingUiProvider INSTANCE = new DefaultBasicMappingUiProvider();

	/**
	 * Return the singleton.
	 */
	public static AttributeMappingUiProvider<BasicMapping> instance() {
		return INSTANCE;
	}

	/**
	 * Ensure non-instantiability.
	 */
	private DefaultBasicMappingUiProvider() {
		super();
	}

	public String mappingKey() {
		return MappingKeys.BASIC_ATTRIBUTE_MAPPING_KEY;
	}

	public String label() {
		return NLS.bind(
			JptUiMappingsMessages.DefaultBasicMappingUiProvider_Default,
			JptUiMappingsMessages.PersistentAttributePage_BasicLabel
		);
	}

	public JpaComposite<BasicMapping> buildAttributeMappingComposite(JpaUiFactory factory,
			PropertyValueModel<BasicMapping> subjectHolder,
			Composite parent,
			WidgetFactory widgetFactory) {

		return factory.createBasicMappingComposite(subjectHolder, parent, widgetFactory);
	}
}
