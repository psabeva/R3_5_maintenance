/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.context.java;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jpt.common.core.resource.java.JavaResourceField;
import org.eclipse.jpt.common.core.resource.java.JavaResourceMethod;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.utility.MethodSignature;
import org.eclipse.jpt.common.utility.internal.ArrayTools;
import org.eclipse.jpt.common.utility.internal.iterables.ListIterable;
import org.eclipse.jpt.jpa.core.context.AccessType;
import org.eclipse.jpt.jpa.core.context.PersistentAttribute;
import org.eclipse.jpt.jpa.core.context.PersistentType;
import org.eclipse.jpt.jpa.core.context.ReadOnlyPersistentAttribute;
import org.eclipse.jpt.jpa.core.context.java.JavaElementReference;
import org.eclipse.jpt.jpa.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.jpa.core.internal.context.JptValidator;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.PersistentPropertyValidator;

public class PropertyAccessor
	extends AbstractAccessor
{
	private final JavaResourceMethod resourceGetter;

	private final JavaResourceMethod resourceSetter;


	public PropertyAccessor(ReadOnlyPersistentAttribute parent, JavaResourceMethod resourceGetter, JavaResourceMethod resourceSetter) {
		super(parent);
		this.resourceGetter = resourceGetter;
		this.resourceSetter = resourceSetter;
	}

	public JavaResourceMethod getResourceAttribute() {
		return (this.resourceGetter != null) ? this.resourceGetter : this.resourceSetter;
	}

	public boolean isFor(JavaResourceField field) {
		return false;
	}

	public boolean isFor(JavaResourceMethod getterMethod, JavaResourceMethod setterMethod) {
		return (this.resourceGetter == getterMethod) && (this.resourceSetter == setterMethod);
	}

	public AccessType getDefaultAccess() {
		return AccessType.PROPERTY;
	}

	public JavaResourceMethod getResourceGetter() {
		return this.resourceGetter;
	}

	public JavaResourceMethod getResourceSetter() {
		return this.resourceSetter;
	}

	public JavaPersistentAttribute buildUnannotatedJavaAttribute(PersistentType type) {
		return this.buildJavaAttribute(type, this.buildUnannotatedJavaResourceGetter(), this.buildUnannotatedJavaResourceSetter());
	}

	/**
	 * Build a Java resource method that wraps the original Java resource
	 * getter method and behaves as though it has no annotations. This will cause
	 * all the settings in the Java <em>context</em> attribute to default.
	 */
	protected JavaResourceMethod buildUnannotatedJavaResourceGetter() {
		return new UnannotatedJavaResourceMethod(this.resourceGetter);
	}

	/**
	 * Build a Java resource method that wraps the original Java resource
	 * setter method and behaves as though it has no annotations. This will cause
	 * all the settings in the Java <em>context</em> attribute to default.
	 */
	protected JavaResourceMethod buildUnannotatedJavaResourceSetter() {
		return new UnannotatedJavaResourceMethod(this.resourceSetter);
	}

	protected JavaPersistentAttribute buildJavaAttribute(PersistentType type, JavaResourceMethod getterMethod, JavaResourceMethod setterMethod) {
		return this.getJpaFactory().buildJavaPersistentProperty(type, getterMethod, setterMethod);
	}

	public JptValidator buildAttributeValidator(PersistentAttribute persistentAttribute) {
		return new PersistentPropertyValidator(persistentAttribute, this);
	}

	public TextRange getValidationTextRange() {
		return this.getResourceAttribute().getNameTextRange();
	}

	public IJavaElement getJavaElement() {
		PersistentType persistentType = this.getAttribute().getOwningPersistentType();
		if (persistentType instanceof JavaElementReference) {
			IType jdtType = (IType) ((JavaElementReference) persistentType).getJavaElement();
			if (jdtType != null) {
				JavaResourceMethod method = this.getResourceAttribute();
				if (method != null) {
					String[] parmTypeNames = ArrayTools.array(method.getParameterTypeNames(), new String[method.getParametersSize()]);
					return jdtType.getMethod(method.getMethodName(), parmTypeNames);
				}
			}
		}
		return null;
	}


	// ********** unannotated Java resource method **********

	/**
	 * Wrap another Java resource method and suppress all its annotations.
	 */
	protected class UnannotatedJavaResourceMethod
		extends UnannotatedJavaResourceAttribute<JavaResourceMethod>
		implements JavaResourceMethod
	{
		protected UnannotatedJavaResourceMethod(JavaResourceMethod method){
			super(method);
		}

		public Kind getKind() {
			return Kind.METHOD;
		}

		public String getMethodName() {
			return this.member.getMethodName();
		}

		public ListIterable<String> getParameterTypeNames() {
			return this.member.getParameterTypeNames();
		}

		public String getParameterTypeName(int index) {
			return this.member.getParameterTypeName(index);
		}

		public int getParametersSize() {
			return this.member.getParametersSize();
		}

		public boolean isConstructor() {
			return this.member.isConstructor();
		}

		public boolean isFor(MethodSignature methodSignature, int occurrence) {
			return this.member.isFor(methodSignature, occurrence);
		}

		public void synchronizeWith(MethodDeclaration methodDeclaration) {
			// NOP
		}
	}
}
