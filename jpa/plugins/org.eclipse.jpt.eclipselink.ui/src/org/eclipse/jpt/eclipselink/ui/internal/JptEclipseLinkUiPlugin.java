/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.ui.internal;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class JptEclipseLinkUiPlugin extends AbstractUIPlugin
{
	private static JptEclipseLinkUiPlugin INSTANCE;

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.jpt.eclipselink.ui";

	// The shared instance
	private static JptEclipseLinkUiPlugin plugin;

	// ********** constructors **********
	public JptEclipseLinkUiPlugin() {
		super();
		INSTANCE = this;
	}

	/**
	 * Returns the singleton Plugin
	 */
	public static JptEclipseLinkUiPlugin getPlugin() {
		return INSTANCE;
	}

	public static void log(IStatus status) {
        INSTANCE.getLog().log(status);
    }

	public static void log(String msg) {
        log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, msg, null));
    }

	public static void log(Throwable throwable) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, throwable.getLocalizedMessage(), throwable));
	}

	// ********** Image API **********
	/**
	 * This gets a .gif from the icons folder.
	 */
	public static ImageDescriptor getImageDescriptor(String key) {
		if (! key.startsWith("icons/")) {
			key = "icons/" + key;
		}
		if (! key.endsWith(".gif")) {
			key = key + ".gif";
		}
		return imageDescriptorFromPlugin(PLUGIN_ID, key);
	}

	/**
	 * This returns an image for a .gif from the icons folder
	 */
	public static Image getImage(String key) {
		ImageDescriptor desc = getImageDescriptor(key);
		return (desc == null) ? null : desc.createImage();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static JptEclipseLinkUiPlugin getDefault() {
		return plugin;
	}
}
