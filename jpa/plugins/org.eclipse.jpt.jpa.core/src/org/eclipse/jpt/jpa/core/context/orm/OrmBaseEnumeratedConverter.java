/*******************************************************************************
 * Copyright (c) 2010, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.context.orm;

import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.jpa.core.context.BaseEnumeratedConverter;
import org.eclipse.jpt.jpa.core.context.Converter;
import org.eclipse.jpt.jpa.core.internal.context.JptValidator;
import org.eclipse.jpt.jpa.core.resource.orm.EnumType;
import org.eclipse.jpt.jpa.core.resource.orm.XmlAttributeMapping;
import org.eclipse.jpt.jpa.core.resource.orm.XmlConvertibleMapping;
import org.eclipse.jpt.jpa.core.resource.orm.v2_0.XmlMapKeyConvertibleMapping_2_0;

/**
 * <code>orm.xml</code> enumerated/map key enumerated converter
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface OrmBaseEnumeratedConverter
	extends BaseEnumeratedConverter, OrmConverter
{
	// ********** owner **********

	/**
	 * 
	 */
	public interface Owner
		extends OrmConverter.Owner
	{
		EnumType getXmlEnumType();

		void setXmlEnumType(EnumType enumType);

		TextRange getEnumTextRange();
	}

	// ********** enumerated adapter **********

	public static class BasicAdapter
		implements OrmConverter.Adapter
	{
		private static final BasicAdapter INSTANCE = new BasicAdapter();
		public static BasicAdapter instance() {
			return INSTANCE;
		}

		private BasicAdapter() {
			super();
		}

		public Class<? extends Converter> getConverterType() {
			return BaseEnumeratedConverter.class;
		}

		public OrmConverter buildConverter(OrmAttributeMapping parent, OrmXmlContextNodeFactory factory) {
			XmlConvertibleMapping xmlMapping = (XmlConvertibleMapping) parent.getXmlAttributeMapping();
			return (xmlMapping.getEnumerated() == null) ? null : factory.buildOrmBaseEnumeratedConverter(parent, this.buildOwner(xmlMapping));
		}

		protected OrmBaseEnumeratedConverter.Owner buildOwner(final XmlConvertibleMapping mapping) {
			return new OrmBaseEnumeratedConverter.Owner() {
				public void setXmlEnumType(EnumType enumType) {
					mapping.setEnumerated(enumType);
				}
				public EnumType getXmlEnumType() {
					return mapping.getEnumerated();
				}
				public TextRange getEnumTextRange() {
					return mapping.getEnumeratedTextRange();
				}
				public JptValidator buildValidator(Converter converter) {
					return JptValidator.Null.instance();
				}
			};
		}

		public boolean isActive(XmlAttributeMapping xmlMapping) {
			return ((XmlConvertibleMapping) xmlMapping).getEnumerated() != null;
		}

		public OrmConverter buildNewConverter(OrmAttributeMapping parent, OrmXmlContextNodeFactory factory) {
			return factory.buildOrmBaseEnumeratedConverter(parent, this.buildOwner((XmlConvertibleMapping) parent.getXmlAttributeMapping()));
		}

		public void clearXmlValue(XmlAttributeMapping xmlMapping) {
			((XmlConvertibleMapping) xmlMapping).setEnumerated(null);
		}
	}

	// ********** map key enumerated adapter **********

	public static class MapKeyAdapter
		implements OrmConverter.Adapter
	{
		private static final MapKeyAdapter INSTANCE = new MapKeyAdapter();
		public static MapKeyAdapter instance() {
			return INSTANCE;
		}

		private MapKeyAdapter() {
			super();
		}

		public Class<? extends Converter> getConverterType() {
			return BaseEnumeratedConverter.class;
		}

		public OrmConverter buildConverter(OrmAttributeMapping parent, OrmXmlContextNodeFactory factory) {
			XmlMapKeyConvertibleMapping_2_0 xmlMapping = (XmlMapKeyConvertibleMapping_2_0) parent.getXmlAttributeMapping();
			return (xmlMapping.getMapKeyEnumerated() == null) ? null : factory.buildOrmBaseEnumeratedConverter(parent, this.buildOwner(xmlMapping));
		}

		protected OrmBaseEnumeratedConverter.Owner buildOwner(final XmlMapKeyConvertibleMapping_2_0 mapping) {
			return new OrmBaseEnumeratedConverter.Owner() {
				public void setXmlEnumType(EnumType enumType) {
					mapping.setMapKeyEnumerated(enumType);
				}
				
				public EnumType getXmlEnumType() {
					return mapping.getMapKeyEnumerated();
				}
				
				public TextRange getEnumTextRange() {
					return mapping.getMapKeyEnumeratedTextRange();
				}

				public JptValidator buildValidator(Converter converter) {
					return JptValidator.Null.instance();
				}
			};
		}

		public boolean isActive(XmlAttributeMapping xmlMapping) {
			return ((XmlMapKeyConvertibleMapping_2_0) xmlMapping).getMapKeyEnumerated() != null;
		}

		public OrmConverter buildNewConverter(OrmAttributeMapping parent, OrmXmlContextNodeFactory factory) {
			return factory.buildOrmBaseEnumeratedConverter(parent, this.buildOwner((XmlMapKeyConvertibleMapping_2_0) parent.getXmlAttributeMapping()));
		}

		public void clearXmlValue(XmlAttributeMapping xmlMapping) {
			((XmlMapKeyConvertibleMapping_2_0) xmlMapping).setMapKeyEnumerated(null);
		}
	}
}
