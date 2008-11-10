/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal.persistence.customization;

import org.eclipse.jpt.eclipselink.core.internal.context.persistence.customization.Customization;
import org.eclipse.jpt.eclipselink.ui.internal.EclipseLinkUiMessages;
import org.eclipse.jpt.ui.internal.widgets.FormPane;
import org.eclipse.jpt.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.utility.model.value.WritablePropertyValueModel;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;

/**
 * WeavingInternalComposite
 */
public class WeavingInternalComposite extends FormPane<Customization>
{
	/**
	 * Creates a new <code>WeavingInternalComposite</code>.
	 *
	 * @param parentController
	 *            The parent container of this one
	 * @param parent
	 *            The parent container
	 */
	public WeavingInternalComposite(
					FormPane<? extends Customization> parentComposite,
					Composite parent) {

		super(parentComposite, parent);
	}

	private WritablePropertyValueModel<Boolean> buildWeavingInternalHolder() {
		return new PropertyAspectAdapter<Customization, Boolean>(getSubjectHolder(), Customization.WEAVING_INTERNAL_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return subject.getWeavingInternal();
			}

			@Override
			protected void setValue_(Boolean value) {
				subject.setWeavingInternal(value);
			}

			@Override
			protected void subjectChanged() {
				Object oldValue = this.getValue();
				super.subjectChanged();
				Object newValue = this.getValue();

				// Make sure the default value is appended to the text
				if (oldValue == newValue && newValue == null) {
					this.fireAspectChange(Boolean.TRUE, newValue);
				}
			}
		};
	}

	private PropertyValueModel<String> buildWeavingInternalStringHolder() {
		return new TransformationPropertyValueModel<Boolean, String>(buildWeavingInternalHolder()) {
			@Override
			protected String transform(Boolean value) {
				if ((getSubject() != null) && (value == null)) {
					Boolean defaultValue = getSubject().getDefaultWeavingInternal();
					if (defaultValue != null) {
						String defaultStringValue = defaultValue ? EclipseLinkUiMessages.Boolean_True : EclipseLinkUiMessages.Boolean_False;
						return NLS.bind(EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingInternalLabelDefault, defaultStringValue);
					}
				}
				return EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingInternalLabel;
			}
		};
	}

	@Override
	protected void initializeLayout(Composite container) {

		this.addTriStateCheckBoxWithDefault(
			container,
			EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingInternalLabel,
			this.buildWeavingInternalHolder(),
			this.buildWeavingInternalStringHolder(),
			null
//			EclipseLinkHelpContextIds._WEAVING_INTERNAL
		);
	}
}
