/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.internal.resource.java.source;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.common.core.internal.resource.java.source.SourceAnnotation;
import org.eclipse.jpt.common.core.internal.utility.jdt.AnnotatedElementAnnotationElementAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.BooleanExpressionConverter;
import org.eclipse.jpt.common.core.internal.utility.jdt.ConversionDeclarationAnnotationElementAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.ElementAnnotationAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.EnumDeclarationAnnotationElementAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.NestedDeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.internal.utility.jdt.NumberIntegerExpressionConverter;
import org.eclipse.jpt.common.core.internal.utility.jdt.SimpleDeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAnnotatedElement;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.core.utility.jdt.AnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.AnnotationElementAdapter;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationAdapter;
import org.eclipse.jpt.common.core.utility.jdt.DeclarationAnnotationElementAdapter;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.CacheCoordinationType;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.CacheIsolationType2_2;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.CacheType;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLink;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkCacheAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkTimeOfDayAnnotation;

/**
 * <code>org.eclipse.persistence.annotations.Cache</code>
 */
public final class SourceEclipseLinkCacheAnnotation
	extends SourceAnnotation
	implements EclipseLinkCacheAnnotation
{
	private static final DeclarationAnnotationAdapter DECLARATION_ANNOTATION_ADAPTER = new SimpleDeclarationAnnotationAdapter(ANNOTATION_NAME);

	private static final DeclarationAnnotationElementAdapter<String> TYPE_ADAPTER = buildTypeAdapter();
	private final AnnotationElementAdapter<String> typeAdapter;
	private CacheType type;
	private TextRange typeTextRange;

	private static final DeclarationAnnotationElementAdapter<Integer> SIZE_ADAPTER = buildSizeAdapter();
	private final AnnotationElementAdapter<Integer> sizeAdapter;
	private Integer size;
	private TextRange sizeTextRange;

	private static final DeclarationAnnotationElementAdapter<Boolean> SHARED_ADAPTER = buildSharedAdapter();
	private final AnnotationElementAdapter<Boolean> sharedAdapter;
	private Boolean shared;
	private TextRange sharedTextRange;

	private static final DeclarationAnnotationElementAdapter<Boolean> ALWAYS_REFRESH_ADAPTER = buildAlwaysRefreshAdapter();
	private final AnnotationElementAdapter<Boolean> alwaysRefreshAdapter;
	private Boolean alwaysRefresh;
	private TextRange alwaysRefreshTextRange;

	private static final DeclarationAnnotationElementAdapter<Boolean> REFRESH_ONLY_IF_NEWER_ADAPTER = buildRefreshOnlyIfNewerAdapter();
	private final AnnotationElementAdapter<Boolean> refreshOnlyIfNewerAdapter;
	private Boolean refreshOnlyIfNewer;
	private TextRange refreshOnlyIfNewerTextRange;

	private static final DeclarationAnnotationElementAdapter<Boolean> DISABLE_HITS_ADAPTER = buildDisableHitsAdapter();
	private final AnnotationElementAdapter<Boolean> disableHitsAdapter;
	private Boolean disableHits;
	private TextRange disableHitsTextRange;

	private static final DeclarationAnnotationElementAdapter<String> COORDINATION_TYPE_ADAPTER = buildCoordinationTypeAdapter();
	private final AnnotationElementAdapter<String> coordinationTypeAdapter;
	private CacheCoordinationType coordinationType;
	private TextRange coordinationTypeTextRange;

	private static final DeclarationAnnotationElementAdapter<Integer> EXPIRY_ADAPTER = buildExpiryAdapter();
	private final AnnotationElementAdapter<Integer> expiryAdapter;
	private Integer expiry;
	private TextRange expiryTextRange;

	private static final NestedDeclarationAnnotationAdapter EXPIRY_TIME_OF_DAY_ADAPTER = buildExpiryTimeOfDayAdapter();
	private final ElementAnnotationAdapter expiryTimeOfDayAdapter;
	private EclipseLinkTimeOfDayAnnotation expiryTimeOfDay;
	private TextRange expiryTimeOfDayTextRange;

	private static final DeclarationAnnotationElementAdapter<String> ISOLATION_ADAPTER = buildIsolationAdapter();
	private final AnnotationElementAdapter<String> isolationAdapter;
	private CacheIsolationType2_2 isolation;
	private TextRange isolationTextRange;


	public SourceEclipseLinkCacheAnnotation(JavaResourceAnnotatedElement parent, AnnotatedElement element) {
		super(parent, element, DECLARATION_ANNOTATION_ADAPTER);
		this.typeAdapter = new AnnotatedElementAnnotationElementAdapter<String>(element, TYPE_ADAPTER);
		this.sizeAdapter = new AnnotatedElementAnnotationElementAdapter<Integer>(element, SIZE_ADAPTER);
		this.sharedAdapter = new AnnotatedElementAnnotationElementAdapter<Boolean>(element, SHARED_ADAPTER);
		this.alwaysRefreshAdapter = new AnnotatedElementAnnotationElementAdapter<Boolean>(element, ALWAYS_REFRESH_ADAPTER);
		this.refreshOnlyIfNewerAdapter = new AnnotatedElementAnnotationElementAdapter<Boolean>(element, REFRESH_ONLY_IF_NEWER_ADAPTER);
		this.disableHitsAdapter = new AnnotatedElementAnnotationElementAdapter<Boolean>(element, DISABLE_HITS_ADAPTER);
		this.coordinationTypeAdapter = new AnnotatedElementAnnotationElementAdapter<String>(element, COORDINATION_TYPE_ADAPTER);
		this.expiryAdapter = new AnnotatedElementAnnotationElementAdapter<Integer>(element, EXPIRY_ADAPTER);
		this.expiryTimeOfDayAdapter = new ElementAnnotationAdapter(element, EXPIRY_TIME_OF_DAY_ADAPTER);
		this.isolationAdapter = new AnnotatedElementAnnotationElementAdapter<String>(element, ISOLATION_ADAPTER);
	}

	public String getAnnotationName() {
		return ANNOTATION_NAME;
	}

	public void initialize(CompilationUnit astRoot) {
		this.type = this.buildType(astRoot);
		this.typeTextRange = this.buildTypeTextRange(astRoot);

		this.size = this.buildSize(astRoot);
		this.sizeTextRange = this.buildSizeTextRange(astRoot);

		this.shared = this.buildShared(astRoot);
		this.sharedTextRange = this.buildSharedTextRange(astRoot);

		this.alwaysRefresh = this.buildAlwaysRefresh(astRoot);
		this.alwaysRefreshTextRange = this.buildAlwaysRefreshTextRange(astRoot);

		this.refreshOnlyIfNewer = this.buildRefreshOnlyIfNewer(astRoot);
		this.refreshOnlyIfNewerTextRange = this.buildRefreshOnlyIfNewerTextRange(astRoot);

		this.disableHits = this.buildDisableHits(astRoot);
		this.disableHitsTextRange = this.buildDisableHitsTextRange(astRoot);

		this.coordinationType = this.buildCoordinationType(astRoot);
		this.coordinationTypeTextRange = this.buildCoordinationTypeTextRange(astRoot);

		this.expiry = this.buildExpiry(astRoot);
		this.expiryTextRange = this.buildExpiryTextRange(astRoot);

		this.initializeExpiryTimeOfDay(astRoot);
		this.expiryTimeOfDayTextRange = this.buildExpiryTimeOfDayTextRange(astRoot);

		this.isolation = this.buildIsolation(astRoot);
		this.isolationTextRange = this.buildIsolationTextRange(astRoot);
	}

	private void initializeExpiryTimeOfDay(CompilationUnit astRoot) {
		if (this.expiryTimeOfDayAdapter.getAnnotation(astRoot) != null) {
			this.expiryTimeOfDay = this.buildExpiryTimeOfDay();
			this.expiryTimeOfDay.initialize(astRoot);
		}
	}

	public void synchronizeWith(CompilationUnit astRoot) {
		this.syncType(this.buildType(astRoot));
		this.typeTextRange = this.buildTypeTextRange(astRoot);

		this.syncSize(this.buildSize(astRoot));
		this.sizeTextRange = this.buildSizeTextRange(astRoot);

		this.syncShared(this.buildShared(astRoot));
		this.sharedTextRange = this.buildSharedTextRange(astRoot);

		this.syncAlwaysRefresh(this.buildAlwaysRefresh(astRoot));
		this.alwaysRefreshTextRange = this.buildAlwaysRefreshTextRange(astRoot);

		this.syncRefreshOnlyIfNewer(this.buildRefreshOnlyIfNewer(astRoot));
		this.refreshOnlyIfNewerTextRange = this.buildRefreshOnlyIfNewerTextRange(astRoot);

		this.syncDisableHits(this.buildDisableHits(astRoot));
		this.disableHitsTextRange = this.buildDisableHitsTextRange(astRoot);

		this.syncCoordinationType(this.buildCoordinationType(astRoot));
		this.coordinationTypeTextRange = this.buildCoordinationTypeTextRange(astRoot);

		this.syncExpiry(this.buildExpiry(astRoot));
		this.expiryTextRange = this.buildExpiryTextRange(astRoot);

		this.syncExpiryTimeOfDay(astRoot);
		this.expiryTimeOfDayTextRange = this.buildExpiryTimeOfDayTextRange(astRoot);

		this.syncIsolation(this.buildIsolation(astRoot));
		this.isolationTextRange = this.buildIsolationTextRange(astRoot);
	}

	@Override
	public boolean isUnset() {
		return super.isUnset() &&
				(this.type == null) &&
				(this.size == null) &&
				(this.shared == null) &&
				(this.alwaysRefresh == null) &&
				(this.refreshOnlyIfNewer == null) &&
				(this.disableHits == null) &&
				(this.coordinationType == null) &&
				(this.expiry == null) &&
				(this.expiryTimeOfDay == null) &&
				(this.isolation == null);
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.type);
	}


	// ********** CacheAnnotation implementation **********

	// ***** type
	public CacheType getType() {
		return this.type;
	}

	public void setType(CacheType type) {
		if (this.attributeValueHasChanged(this.type, type)) {
			this.type = type;
			this.typeAdapter.setValue(CacheType.toJavaAnnotationValue(type));
		}
	}

	private void syncType(CacheType astType) {
		CacheType old = this.type;
		this.type = astType;
		this.firePropertyChanged(TYPE_PROPERTY, old, astType);
	}

	private CacheType buildType(CompilationUnit astRoot) {
		return CacheType.fromJavaAnnotationValue(this.typeAdapter.getValue(astRoot));
	}

	public TextRange getTypeTextRange() {
		return this.typeTextRange;
	}

	private TextRange buildTypeTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(TYPE_ADAPTER, astRoot);
	}

	// ***** size
	public Integer getSize() {
		return this.size;
	}

	public void setSize(Integer size) {
		if (this.attributeValueHasChanged(this.size, size)) {
			this.size = size;
			this.sizeAdapter.setValue(size);
		}
	}

	private void syncSize(Integer astSize) {
		Integer old = this.size;
		this.size = astSize;
		this.firePropertyChanged(SIZE_PROPERTY, old, astSize);
	}

	private Integer buildSize(CompilationUnit astRoot) {
		return this.sizeAdapter.getValue(astRoot);
	}

	public TextRange getSizeTextRange() {
		return this.sizeTextRange;
	}

	private TextRange buildSizeTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(SIZE_ADAPTER, astRoot);
	}

	// ***** shared
	public Boolean getShared() {
		return this.shared;
	}

	public void setShared(Boolean shared) {
		if (this.attributeValueHasChanged(this.shared, shared)) {
			this.shared = shared;
			this.sharedAdapter.setValue(shared);
		}
	}

	private void syncShared(Boolean astShared) {
		Boolean old = this.shared;
		this.shared = astShared;
		this.firePropertyChanged(SHARED_PROPERTY, old, astShared);
	}

	private Boolean buildShared(CompilationUnit astRoot) {
		return this.sharedAdapter.getValue(astRoot);
	}

	public TextRange getSharedTextRange() {
		return this.sharedTextRange;
	}

	private TextRange buildSharedTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(SHARED_ADAPTER, astRoot);
	}

	// ***** always refresh
	public Boolean getAlwaysRefresh() {
		return this.alwaysRefresh;
	}

	public void setAlwaysRefresh(Boolean alwaysRefresh) {
		if (this.attributeValueHasChanged(this.alwaysRefresh, alwaysRefresh)) {
			this.alwaysRefresh = alwaysRefresh;
			this.alwaysRefreshAdapter.setValue(alwaysRefresh);
		}
	}

	private void syncAlwaysRefresh(Boolean astAlwaysRefresh) {
		Boolean old = this.alwaysRefresh;
		this.alwaysRefresh = astAlwaysRefresh;
		this.firePropertyChanged(ALWAYS_REFRESH_PROPERTY, old, astAlwaysRefresh);
	}

	private Boolean buildAlwaysRefresh(CompilationUnit astRoot) {
		return this.alwaysRefreshAdapter.getValue(astRoot);
	}

	public TextRange getAlwaysRefreshTextRange() {
		return this.alwaysRefreshTextRange;
	}

	private TextRange buildAlwaysRefreshTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(ALWAYS_REFRESH_ADAPTER, astRoot);
	}

	// ***** refresh only if newer
	public Boolean getRefreshOnlyIfNewer() {
		return this.refreshOnlyIfNewer;
	}

	public void setRefreshOnlyIfNewer(Boolean refreshOnlyIfNewer) {
		if (this.attributeValueHasChanged(this.refreshOnlyIfNewer, refreshOnlyIfNewer)) {
			this.refreshOnlyIfNewer = refreshOnlyIfNewer;
			this.refreshOnlyIfNewerAdapter.setValue(refreshOnlyIfNewer);
		}
	}

	private void syncRefreshOnlyIfNewer(Boolean astRefreshOnlyIfNewer) {
		Boolean old = this.refreshOnlyIfNewer;
		this.refreshOnlyIfNewer = astRefreshOnlyIfNewer;
		this.firePropertyChanged(REFRESH_ONLY_IF_NEWER_PROPERTY, old, astRefreshOnlyIfNewer);
	}

	private Boolean buildRefreshOnlyIfNewer(CompilationUnit astRoot) {
		return this.refreshOnlyIfNewerAdapter.getValue(astRoot);
	}

	public TextRange getRefreshOnlyIfNewerTextRange() {
		return this.refreshOnlyIfNewerTextRange;
	}

	private TextRange buildRefreshOnlyIfNewerTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(REFRESH_ONLY_IF_NEWER_ADAPTER, astRoot);
	}

	// ***** disable hits
	public Boolean getDisableHits() {
		return this.disableHits;
	}

	public void setDisableHits(Boolean disableHits) {
		if (this.attributeValueHasChanged(this.disableHits, disableHits)) {
			this.disableHits = disableHits;
			this.disableHitsAdapter.setValue(disableHits);
		}
	}

	private void syncDisableHits(Boolean astDisableHits) {
		Boolean old = this.disableHits;
		this.disableHits = astDisableHits;
		this.firePropertyChanged(DISABLE_HITS_PROPERTY, old, astDisableHits);
	}

	private Boolean buildDisableHits(CompilationUnit astRoot) {
		return this.disableHitsAdapter.getValue(astRoot);
	}

	public TextRange getDisableHitsTextRange() {
		return this.disableHitsTextRange;
	}

	private TextRange buildDisableHitsTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(DISABLE_HITS_ADAPTER, astRoot);
	}

	// ***** coordination type
	public CacheCoordinationType getCoordinationType() {
		return this.coordinationType;
	}

	public void setCoordinationType(CacheCoordinationType coordinationType) {
		if (this.attributeValueHasChanged(this.coordinationType, coordinationType)) {
			this.coordinationType = coordinationType;
			this.coordinationTypeAdapter.setValue(CacheCoordinationType.toJavaAnnotationValue(coordinationType));
		}
	}

	private void syncCoordinationType(CacheCoordinationType astCoordinationType) {
		CacheCoordinationType old = this.coordinationType;
		this.coordinationType = astCoordinationType;
		this.firePropertyChanged(TYPE_PROPERTY, old, astCoordinationType);
	}

	private CacheCoordinationType buildCoordinationType(CompilationUnit astRoot) {
		return CacheCoordinationType.fromJavaAnnotationValue(this.coordinationTypeAdapter.getValue(astRoot));
	}

	public TextRange getCoordinationTypeTextRange() {
		return this.coordinationTypeTextRange;
	}

	private TextRange buildCoordinationTypeTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(COORDINATION_TYPE_ADAPTER, astRoot);
	}

	// ***** expiry
	public Integer getExpiry() {
		return this.expiry;
	}

	public void setExpiry(Integer expiry) {
		if (this.attributeValueHasChanged(this.expiry, expiry)) {
			this.expiry = expiry;
			this.expiryAdapter.setValue(expiry);
		}
	}

	private void syncExpiry(Integer astExpiry) {
		Integer old = this.expiry;
		this.expiry = astExpiry;
		this.firePropertyChanged(EXPIRY_PROPERTY, old, astExpiry);
	}

	private Integer buildExpiry(CompilationUnit astRoot) {
		return this.expiryAdapter.getValue(astRoot);
	}

	public TextRange getExpiryTextRange() {
		return this.expiryTextRange;
	}

	private TextRange buildExpiryTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(EXPIRY_ADAPTER, astRoot);
	}

	// ***** expiry time of day
	public EclipseLinkTimeOfDayAnnotation getExpiryTimeOfDay() {
		return this.expiryTimeOfDay;
	}

	public EclipseLinkTimeOfDayAnnotation addExpiryTimeOfDay() {
		if (this.expiryTimeOfDay != null) {
			throw new IllegalStateException("'expiryTimeOfDay' element already exists: " + this.expiryTimeOfDay); //$NON-NLS-1$
		}
		this.expiryTimeOfDay = this.buildExpiryTimeOfDay();
		this.expiryTimeOfDay.newAnnotation();
		return this.expiryTimeOfDay;
	}

	public void removeExpiryTimeOfDay() {
		if (this.expiryTimeOfDay == null) {
			throw new IllegalStateException("'expiryTimeOfDay' element does not exist"); //$NON-NLS-1$
		}
		EclipseLinkTimeOfDayAnnotation old = this.expiryTimeOfDay;
		this.expiryTimeOfDay = null;
		old.removeAnnotation();
	}

	private EclipseLinkTimeOfDayAnnotation buildExpiryTimeOfDay() {
		return new SourceEclipseLinkTimeOfDayAnnotation(this, this.annotatedElement, EXPIRY_TIME_OF_DAY_ADAPTER);
	}

	private void syncExpiryTimeOfDay(CompilationUnit astRoot) {
		if (this.expiryTimeOfDayAdapter.getAnnotation(astRoot) == null) {
			this.syncExpiryTimeOfDay_(null);
		} else {
			if (this.expiryTimeOfDay == null) {
				EclipseLinkTimeOfDayAnnotation tod = this.buildExpiryTimeOfDay();
				tod.initialize(astRoot);
				this.syncExpiryTimeOfDay_(tod);
			} else {
				this.expiryTimeOfDay.synchronizeWith(astRoot);
			}
		}
	}

	private void syncExpiryTimeOfDay_(EclipseLinkTimeOfDayAnnotation astExpiryTimeOfDay) {
		EclipseLinkTimeOfDayAnnotation old = this.expiryTimeOfDay;
		this.expiryTimeOfDay = astExpiryTimeOfDay;
		this.firePropertyChanged(EXPIRY_TIME_OF_DAY_PROPERTY, old, astExpiryTimeOfDay);
	}

	public TextRange getExpiryTimeOfDayTextRange() {
		return this.expiryTimeOfDayTextRange;
	}

	private TextRange buildExpiryTimeOfDayTextRange(CompilationUnit astRoot) {
		return this.buildTextRange(this.expiryTimeOfDayAdapter.getAstNode(astRoot));
	}

	// ***** isolation
	public CacheIsolationType2_2 getIsolation() {
		return this.isolation;
	}

	public void setIsolation(CacheIsolationType2_2 isolation) {
		if (this.attributeValueHasChanged(this.isolation, isolation)) {
			this.isolation = isolation;
			this.isolationAdapter.setValue(CacheIsolationType2_2.toJavaAnnotationValue(isolation));
		}
	}

	private void syncIsolation(CacheIsolationType2_2 astIsolation) {
		CacheIsolationType2_2 old = this.isolation;
		this.isolation = astIsolation;
		this.firePropertyChanged(ISOLATION_PROPERTY, old, astIsolation);
	}

	private CacheIsolationType2_2 buildIsolation(CompilationUnit astRoot) {
		return CacheIsolationType2_2.fromJavaAnnotationValue(this.isolationAdapter.getValue(astRoot));
	}

	public TextRange getIsolationTextRange() {
		return this.isolationTextRange;
	}

	private TextRange buildIsolationTextRange(CompilationUnit astRoot) {
		return this.getElementTextRange(ISOLATION_ADAPTER, astRoot);
	}


	// ********** static methods **********

	private static DeclarationAnnotationElementAdapter<String> buildTypeAdapter() {
		return new EnumDeclarationAnnotationElementAdapter(DECLARATION_ANNOTATION_ADAPTER, EclipseLink.CACHE__TYPE);
	}

	private static DeclarationAnnotationElementAdapter<Integer> buildSizeAdapter() {
		return new ConversionDeclarationAnnotationElementAdapter<Integer>(DECLARATION_ANNOTATION_ADAPTER, EclipseLink.CACHE__SIZE, NumberIntegerExpressionConverter.instance());
	}

	private static DeclarationAnnotationElementAdapter<Boolean> buildSharedAdapter() {
		return new ConversionDeclarationAnnotationElementAdapter<Boolean>(DECLARATION_ANNOTATION_ADAPTER, EclipseLink.CACHE__SHARED, BooleanExpressionConverter.instance());
	}

	private static DeclarationAnnotationElementAdapter<Integer> buildExpiryAdapter() {
		return new ConversionDeclarationAnnotationElementAdapter<Integer>(DECLARATION_ANNOTATION_ADAPTER, EclipseLink.CACHE__EXPIRY, NumberIntegerExpressionConverter.instance());
	}

	private static DeclarationAnnotationElementAdapter<Boolean> buildAlwaysRefreshAdapter() {
		return new ConversionDeclarationAnnotationElementAdapter<Boolean>(DECLARATION_ANNOTATION_ADAPTER, EclipseLink.CACHE__ALWAYS_REFRESH, BooleanExpressionConverter.instance());
	}

	private static DeclarationAnnotationElementAdapter<Boolean> buildRefreshOnlyIfNewerAdapter() {
		return new ConversionDeclarationAnnotationElementAdapter<Boolean>(DECLARATION_ANNOTATION_ADAPTER, EclipseLink.CACHE__REFRESH_ONLY_IF_NEWER, BooleanExpressionConverter.instance());
	}

	private static DeclarationAnnotationElementAdapter<Boolean> buildDisableHitsAdapter() {
		return new ConversionDeclarationAnnotationElementAdapter<Boolean>(DECLARATION_ANNOTATION_ADAPTER, EclipseLink.CACHE__DISABLE_HITS, BooleanExpressionConverter.instance());
	}

	private static DeclarationAnnotationElementAdapter<String> buildCoordinationTypeAdapter() {
		return new EnumDeclarationAnnotationElementAdapter(DECLARATION_ANNOTATION_ADAPTER, EclipseLink.CACHE__COORDINATION_TYPE);
	}

	private static NestedDeclarationAnnotationAdapter buildExpiryTimeOfDayAdapter() {
		return new NestedDeclarationAnnotationAdapter(DECLARATION_ANNOTATION_ADAPTER, EclipseLink.CACHE__EXPIRY_TIME_OF_DAY, EclipseLink.TIME_OF_DAY);
	}

	private static DeclarationAnnotationElementAdapter<String> buildIsolationAdapter() {
		return new EnumDeclarationAnnotationElementAdapter(DECLARATION_ANNOTATION_ADAPTER, EclipseLink.CACHE__ISOLATION);
	}

}
