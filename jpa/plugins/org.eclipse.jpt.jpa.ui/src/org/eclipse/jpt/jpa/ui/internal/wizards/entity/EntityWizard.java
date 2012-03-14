/*******************************************************************************
 * Copyright (c) 2008, 2012 SAP AG, Walldorf and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     SAP AG - initial API and implementation
 *     Dimiter Dimitrov, d.dimitrov@sap.com - initial API and implementation     
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.wizards.entity;

import static org.eclipse.jst.j2ee.internal.common.operations.INewJavaClassDataModelProperties.OPEN_IN_EDITOR;
import static org.eclipse.jst.j2ee.internal.common.operations.INewJavaClassDataModelProperties.PROJECT;
import static org.eclipse.jst.j2ee.internal.common.operations.INewJavaClassDataModelProperties.QUALIFIED_CLASS_NAME;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jpt.common.core.internal.utility.PlatformTools;
import org.eclipse.jpt.jpa.core.context.JpaContextNode;
import org.eclipse.jpt.jpa.ui.JptJpaUiPlugin;
import org.eclipse.jpt.jpa.ui.internal.JptUiIcons;
import org.eclipse.jpt.jpa.ui.internal.wizards.entity.data.model.EntityDataModelProvider;
import org.eclipse.jst.j2ee.internal.plugin.J2EEEditorUtility;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelProvider;
import org.eclipse.wst.common.frameworks.internal.datamodel.ui.DataModelWizard;

public class EntityWizard
		extends DataModelWizard
		implements INewWizard {
	
	protected String initialProjectName;
	
	protected IStructuredSelection selection;

    public EntityWizard(IDataModel model) {
		super(model);
        setWindowTitle(EntityWizardMsg.ENTITY_WIZARD_TITLE);
        setDefaultPageImageDescriptor(JptJpaUiPlugin.getImageDescriptor(JptUiIcons.ENTITY_WIZ_BANNER));        
	}    
 
	public EntityWizard(){
    	this(null);
    }
	
	@Override
	protected void doAddPages() {
		EntityClassWizardPage page1 = new EntityClassWizardPage(
		        getDataModel(),
		        "pageOne", //$NON-NLS-1$
		        EntityWizardMsg.ENTITY_WIZARD_PAGE_DESCRIPTION,
		        EntityWizardMsg.ENTITY_WIZARD_PAGE_TITLE);
		page1.setProjectName(this.initialProjectName);
		addPage(page1);
		EntityFieldsWizardPage page2 = new EntityFieldsWizardPage(getDataModel(), "pageTwo"); //$NON-NLS-1$
		addPage(page2);
	}

	public IStructuredSelection getSelection() {
		return this.selection;
	}

	/**
	 * EntityDataModelProvider
	 */
	@Override
	protected IDataModelProvider getDefaultProvider() {
		return new EntityDataModelProvider();
	}

	@Override
	public boolean canFinish() {
		 return getDataModel().isValid();
	}
	
	/**
	 * Override the parent method in order to open the generated entity class in java editor
	 */
	@Override
    protected void postPerformFinish() throws InvocationTargetException {      
        try {
            String className = getDataModel().getStringProperty(QUALIFIED_CLASS_NAME);
            IProject p = (IProject) getDataModel().getProperty(PROJECT);
            IJavaProject javaProject = J2EEEditorUtility.getJavaProject(p);
            IFile file = (IFile) javaProject.findType(className).getResource();
            openEditor(file);
        } catch (Exception cantOpen) {
        	JptJpaUiPlugin.log(cantOpen);
        } 
    }
	
    /**
     * This method is intended for internal use only. It will open the file, sent as parameter
     * in its own java editor
     */
    private void openEditor(final IFile file) {
    	if (getDataModel().getBooleanProperty(OPEN_IN_EDITOR)) {
    		if (file != null) {
    			getShell().getDisplay().asyncExec(new Runnable() {
    				public void run() {
    					try {
    						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    						IDE.openEditor(page, file, true);
    					}
    					catch (PartInitException e) {
    						JptJpaUiPlugin.log(e);
    					}
    				}
    			});
    		}
    	}
    }
    
    public void init(IWorkbench workbench, IStructuredSelection sel) {
    	this.selection = sel;
    	this.initialProjectName = this.extractProjectName(sel.getFirstElement());
		getDataModel();
	}

    protected String extractProjectName(Object object) {
    	if (object == null) {
    		return null;
    	}

    	IResource resource = PlatformTools.getAdapter(object, IResource.class);
		if (resource != null) {
			return resource.getProject().getName();
		}

		IJavaElement javaElement = PlatformTools.getAdapter(object, IJavaElement.class);
		if (javaElement != null) {
			return javaElement.getJavaProject().getProject().getName();
		}

		JpaContextNode node = PlatformTools.getAdapter(object, JpaContextNode.class);
		if (node != null) {
			return node.getJpaProject().getProject().getName();
		}

		return null;
	}
}
