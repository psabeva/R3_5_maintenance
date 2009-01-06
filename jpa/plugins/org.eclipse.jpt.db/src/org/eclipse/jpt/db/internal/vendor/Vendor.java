/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.db.internal.vendor;

import java.util.List;

import org.eclipse.datatools.modelbase.sql.schema.Catalog;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.Schema;

/**
 * Delegate vendor-specific behavior to implementations of this interface:
 *   - catalog support
 *   - default catalog and schema
 *   - converting names to identifiers and vice-versa
 * 
 * Note:
 * We use "name" when dealing with the unmodified name of a database object
 * as supplied by the database itself (i.e. it is not delimited and it is always
 * case-sensitive).
 * We use "identifier" when dealing with a string representation of a database
 * object name (i.e. it may be delimited and, depending on the vendor, it may
 * be case-insensitive).
 */
public interface Vendor {

	/**
	 * This must match the DTP vendor name.
	 * @see org.eclipse.datatools.modelbase.sql.schema.Database#getVendor()
	 */
	String getDTPVendorName();

	/**
	 * Return whether the vendor supports "real" catalogs (e.g. Sybase).
	 */
	boolean supportsCatalogs(Database database);

	/**
	 * Return the specified database's catalogs.
	 */
	List<Catalog> getCatalogs(Database database);

	/**
	 * Return the specified database's default catalog identifiers for the
	 * specified user.
	 */
	List<String> getDefaultCatalogIdentifiers(Database database, String userName);

	/**
	 * Return the specified database's schemas.
	 */
	List<Schema> getSchemas(Database database);

	/**
	 * Return the specified database's default schema identifiers for the
	 * specified user.
	 */
	List<String> getDefaultSchemaIdentifiers(Database database, String userName);

	/**
	 * Convert the specified database object name to a vendor identifier.
	 * Return null if the identifier matches the specified default name.
	 */
	String convertNameToIdentifier(String name, String defaultName);

	/**
	 * Convert the specified database object name to a vendor identifier.
	 */
	String convertNameToIdentifier(String name);

	/**
	 * Convert the specified database object identifier to a vendor name.
	 */
	String convertIdentifierToName(String identifier);

}
