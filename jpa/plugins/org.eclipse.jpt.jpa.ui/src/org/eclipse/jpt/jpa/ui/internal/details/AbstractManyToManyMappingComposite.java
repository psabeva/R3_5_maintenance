/*******************************************************************************
 * Copyright (c) 2006, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.details;

import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.core.context.AccessHolder;
import org.eclipse.jpt.jpa.core.context.Cascade;
import org.eclipse.jpt.jpa.core.context.ManyToManyMapping;
import org.eclipse.jpt.jpa.core.context.ManyToManyRelationship;
import org.eclipse.jpt.jpa.ui.details.JpaComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Section;

/**
 * Here the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | TargetEntityComposite                                                 | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | JoiningStrategyComposite                                              | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | FetchTypeComposite                                                    | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | CascadeComposite                                                      | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | OrderingComposite                                                     | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see {@link ManyToManyMapping}
 * @see {@link TargetEntityClassChooser}
 * @see {@link ManyToManyJoiningStrategyPane}
 * @see {@link FetchTypeComboViewer}
 * @see {@link CascadeComposite}
 * @see {@link OrderingComposite}
 *
 * @version 2.3
 * @since 1.0
 */
public abstract class AbstractManyToManyMappingComposite<T extends ManyToManyMapping, R extends ManyToManyRelationship> 
	extends Pane<T>
    implements JpaComposite
{
	/**
	 * Creates a new <code>ManyToManyMappingComposite</code>.
	 *
	 * @param subjectHolder The holder of the subject <code>IManyToManyMapping</code>
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create various common widgets
	 */
	protected AbstractManyToManyMappingComposite(PropertyValueModel<? extends T> subjectHolder,
									PropertyValueModel<Boolean> enabledModel,
									Composite parent,
	                                WidgetFactory widgetFactory) {

		super(subjectHolder, enabledModel, parent, widgetFactory);
	}

	@Override
	protected void initializeLayout(Composite container) {
		initializeManyToManyCollapsibleSection(container);
		initializeJoiningStrategyCollapsibleSection(container);
		initializeOrderingCollapsibleSection(container);
	}
	
	protected void initializeManyToManyCollapsibleSection(Composite container) {
		final Section section = this.getWidgetFactory().createSection(container,
				ExpandableComposite.TITLE_BAR |
				ExpandableComposite.TWISTIE |
				ExpandableComposite.EXPANDED);
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		section.setText(JptUiDetailsMessages.ManyToManySection_title);
		section.setClient(this.initializeManyToManySection(section));
	}

	protected abstract Control initializeManyToManySection(Composite container);

	protected void initializeJoiningStrategyCollapsibleSection(Composite container) {
		new ManyToManyJoiningStrategyPane(this, buildJoiningHolder(), container);
	}
	
	protected void initializeOrderingCollapsibleSection(Composite container) {
		final Section section = this.getWidgetFactory().createSection(container, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE);
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		section.setText(JptUiDetailsMessages.OrderingComposite_orderingGroup);

		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanging(ExpansionEvent e) {
				if (e.getState() && section.getClient() == null) {
					section.setClient(initializeOrderingSection(section));
				}
			}
		});
	}

	protected Control initializeOrderingSection(Composite container) {
		return new OrderingComposite(this, container).getControl();
	}

	
	protected PropertyValueModel<R> buildJoiningHolder() {
		return new TransformationPropertyValueModel<T, R>(
				getSubjectHolder()) {
			@SuppressWarnings("unchecked")
			@Override
			protected R transform_(T value) {
				return (R) value.getRelationship();
			}
		};
	}
	
	protected PropertyValueModel<Cascade> buildCascadeHolder() {
		return new TransformationPropertyValueModel<T, Cascade>(getSubjectHolder()) {
			@Override
			protected Cascade transform_(T value) {
				return value.getCascade();
			}
		};
	}
	
	protected PropertyValueModel<AccessHolder> buildAccessHolderHolder() {
		return new PropertyAspectAdapter<T, AccessHolder>(getSubjectHolder()) {
			@Override
			protected AccessHolder buildValue_() {
				return this.subject.getPersistentAttribute();
			}
		};
	}
}
