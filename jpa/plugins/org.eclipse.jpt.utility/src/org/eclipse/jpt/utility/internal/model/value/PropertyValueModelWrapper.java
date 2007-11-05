/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.internal.model.value;

import org.eclipse.jpt.utility.internal.model.AbstractModel;
import org.eclipse.jpt.utility.internal.model.ChangeSupport;
import org.eclipse.jpt.utility.internal.model.SingleAspectChangeSupport;
import org.eclipse.jpt.utility.internal.model.event.PropertyChangeEvent;
import org.eclipse.jpt.utility.internal.model.listener.PropertyChangeListener;

/**
 * This abstract class provides the infrastructure needed to wrap
 * another property value model, "lazily" listen to it, and propagate
 * its change notifications.
 */
public abstract class PropertyValueModelWrapper
	extends AbstractModel
	implements PropertyValueModel
{

	/** The wrapped property value model. */
	protected PropertyValueModel valueHolder;

	/** A listener that allows us to synch with changes to the wrapped value holder. */
	protected PropertyChangeListener valueChangeListener;


	// ********** constructors **********

	/**
	 * Construct a property value model with the specified wrapped
	 * property value model. The value holder is required.
	 */
	protected PropertyValueModelWrapper(PropertyValueModel valueHolder) {
		super();
		if (valueHolder == null) {
			throw new NullPointerException();
		}
		this.valueHolder = valueHolder;
	}
	

	// ********** initialization **********

    @Override
	protected void initialize() {
		super.initialize();
		this.valueChangeListener = this.buildValueChangeListener();
	}
	
	@Override
	protected ChangeSupport buildChangeSupport() {
		return new SingleAspectChangeSupport(this, VALUE);
	}

	protected PropertyChangeListener buildValueChangeListener() {
		return new PropertyChangeListener() {
			public void propertyChanged(PropertyChangeEvent e) {
				PropertyValueModelWrapper.this.valueChanged(e);
			}
		    @Override
			public String toString() {
				return "value change listener";
			}
		};
	}
	

	// ********** extend change support **********

	/**
	 * Extend to start listening to the nested model if necessary.
	 */
    @Override
	public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
		if (this.hasNoPropertyChangeListeners(VALUE)) {
			this.engageValueHolder();
		}
		super.addPropertyChangeListener(listener);
	}
	
	/**
	 * Extend to start listening to the nested model if necessary.
	 */
    @Override
	public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		if (propertyName == VALUE && this.hasNoPropertyChangeListeners(VALUE)) {
			this.engageValueHolder();
		}
		super.addPropertyChangeListener(propertyName, listener);
	}
	
	/**
	 * Extend to stop listening to the nested model if necessary.
	 */
    @Override
	public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
		super.removePropertyChangeListener(listener);
		if (this.hasNoPropertyChangeListeners(VALUE)) {
			this.disengageValueHolder();
		}
	}
	
	/**
	 * Extend to stop listening to the nested model if necessary.
	 */
    @Override
	public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		super.removePropertyChangeListener(propertyName, listener);
		if (propertyName == VALUE && this.hasNoPropertyChangeListeners(VALUE)) {
			this.disengageValueHolder();
		}
	}
	

	// ********** behavior **********
	
	/**
	 * Begin listening to the value holder.
	 */
	protected void engageValueHolder() {
		this.valueHolder.addPropertyChangeListener(VALUE, this.valueChangeListener);
	}
	
	/**
	 * Stop listening to the value holder.
	 */
	protected void disengageValueHolder() {
		this.valueHolder.removePropertyChangeListener(VALUE, this.valueChangeListener);
	}
	
	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.valueHolder);
	}


	// ********** property change support **********

	/**
	 * The value of the wrapped value holder has changed;
	 * propagate the change notification appropriately.
	 */
	protected abstract void valueChanged(PropertyChangeEvent e);

}
