/*******************************************************************************
 * Copyright (c) 2006, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal;

import org.eclipse.osgi.util.NLS;

/**
 * Localized messages used by Dali core.
 */
public class JptCoreMessages {

	public static String NONE;
	public static String VALIDATE_JOB;
	public static String VALIDATE_PROJECT_NOT_SPECIFIED;
	public static String VALIDATE_SOURCE_FOLDER_NOT_SPECIFIED;
	public static String VALIDATE_SOURCE_FOLDER_ILLEGAL;
	public static String VALIDATE_SOURCE_FOLDER_DOES_NOT_EXIST;
	public static String VALIDATE_SOURCE_FOLDER_NOT_IN_PROJECT;
	public static String VALIDATE_SOURCE_FOLDER_NOT_SOURCE_FOLDER;
	public static String VALIDATE_FILE_PATH_NOT_SPECIFIED;
	public static String VALIDATE_FILE_ALREADY_EXISTS;
	public static String VALIDATE_FILE_VERSION_NOT_SUPPORTED;
	public static String VALIDATE_FILE_VERSION_NOT_SUPPORTED_FOR_FACET_VERSION;
	public static String VALIDATE_PERSISTENCE_UNIT_DOES_NOT_SPECIFIED;
	public static String VALIDATE_PERSISTENCE_UNIT_NOT_IN_PROJECT;
	public static String VALIDATE_PLATFORM_NOT_SPECIFIED;
	public static String VALIDATE_CONNECTION_NOT_SPECIFIED;
	public static String VALIDATE_CONNECTION_INVALID;
	public static String VALIDATE_CONNECTION_NOT_CONNECTED;
	public static String VALIDATE_DEFAULT_CATALOG_NOT_SPECIFIED;
	public static String VALIDATE_CONNECTION_DOESNT_CONTAIN_CATALOG;
	public static String VALIDATE_DEFAULT_SCHEMA_NOT_SPECIFIED;
	public static String VALIDATE_CONNECTION_DOESNT_CONTAIN_SCHEMA;
	public static String VALIDATE_RUNTIME_NOT_SPECIFIED;
	public static String VALIDATE_RUNTIME_DOES_NOT_SUPPORT_EJB_30;
	public static String VALIDATE_LIBRARY_NOT_SPECIFIED;
	public static String SYNCHRONIZE_CLASSES_JOB;
	public static String SYNCHRONIZING_CLASSES_TASK;
	public static String INVALID_PERSISTENCE_XML_CONTENT;
	public static String ERROR_SYNCHRONIZING_CLASSES_COULD_NOT_VALIDATE;
	public static String ERROR_WRITING_FILE;
	public static String REGISTRY_MISSING_ATTRIBUTE;
	public static String REGISTRY_DUPLICATE;
	public static String REGISTRY_FAILED_INSTANTIATION;
	public static String UPDATE_JOB_NAME;
	public static String SYNCHRONIZE_METAMODEL_JOB_NAME;
	public static String PLATFORM_ID_DOES_NOT_EXIST;
	
	private static final String BUNDLE_NAME = "jpa_core"; //$NON-NLS-1$
	private static final Class<?> BUNDLE_CLASS = JptCoreMessages.class;
	static {
		NLS.initializeMessages(BUNDLE_NAME, BUNDLE_CLASS);
	}
	
	private JptCoreMessages() {
		throw new UnsupportedOperationException();
	}

}
