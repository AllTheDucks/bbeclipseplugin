package com.blackboard.eclipse.wizards;


import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jdt.internal.ui.wizards.buildpaths.BuildPathsBlock;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import org.eclipse.jface.resource.ImageDescriptor;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import java.io.*;

import org.eclipse.ui.*;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;

import com.blackboard.eclipse.BbPlugin;
import com.blackboard.eclipse.BlackboardClasspathContainer;
import com.blackboard.eclipse.ProjectHelper;


//import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

/**
 * 
 */

public class BuildingBlockNewWizard extends Wizard implements INewWizard {
	public static final String SERVER_KEY = "server";
	public static final String BASE_PACKAGE_KEY = "basePackage";
	public static final String VENDOR_ID_KEY = "vendor";
	public static final String PLUGIN_HANDLE_KEY = "handle";
	private BuildingBlockNewWizardPage page;
	private ISelection selection;
	
	private String handle;
	private String vendor;
	private String basePackage;
	private String server;
	private String templatePath;
	
//	private WizardNewProjectCreationPage namePage;

	/**
	 * Constructor for BuildingBlockNewWizard.
	 */
	public BuildingBlockNewWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
	      page = new BuildingBlockNewWizardPage(selection);
	      addPage(page);
		
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		
		vendor = page.getVendorId();
		handle = page.getPluginId();
		basePackage = page.getPackage();
		server = page.getServer();
		templatePath = "templates/"+page.getProjectTemplateId();
		
		final IProject project = page.getProject();
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(project, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			e.printStackTrace();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		
		return true;
	}
	
	/*
	 * The worker method. 
	 */

	private void doFinish(
			IProject currProject,
			IProgressMonitor monitor)
	throws CoreException {


		if (!currProject.exists()) {
			currProject.create(monitor);
			currProject.open(monitor);
			BuildPathsBlock.addJavaNature(currProject, new SubProgressMonitor(monitor, 1));
			
		}
		IJavaProject javaProject = JavaCore.create(currProject);


		IFolder srcFolder = currProject.getFolder("src");
		if (!srcFolder.exists()) {
			srcFolder.create(true, true, monitor);
			
			IClasspathEntry srcEntry = JavaCore.newSourceEntry(srcFolder.getFullPath());
			IClasspathEntry[] classpath = new IClasspathEntry[3];
			JavaCore.setClasspathContainer(new Path(BlackboardClasspathContainer.CONTAINER_ID+IPath.SEPARATOR+page.getBbVersion()), 
					new IJavaProject[] {javaProject}, 
					new IClasspathContainer[] {new BlackboardClasspathContainer(new Path(BlackboardClasspathContainer.CONTAINER_ID+IPath.SEPARATOR+page.getBbVersion()),javaProject)}, 
					monitor);
			
			IClasspathEntry bbLibsEntry = JavaCore.newContainerEntry(new Path(BlackboardClasspathContainer.CONTAINER_ID+IPath.SEPARATOR+page.getBbVersion()));
			IClasspathEntry jreEntry = JavaRuntime.getDefaultJREContainerEntry();
			classpath[0] = srcEntry;
			classpath[1] = bbLibsEntry;
			classpath[2] = jreEntry;
			javaProject.setRawClasspath(classpath, monitor);
		}

		HashMap<String, Object> templateModel = new HashMap<String, Object>();
		templateModel.put(PLUGIN_HANDLE_KEY, handle);
		templateModel.put(VENDOR_ID_KEY, vendor);
		templateModel.put(BASE_PACKAGE_KEY, basePackage);
		templateModel.put(SERVER_KEY, server);
		
		ProjectHelper.createFilesFromTemplate(currProject, monitor, "templates/common",templateModel);
		ProjectHelper.createFilesFromTemplate(currProject, monitor, templatePath, templateModel);

		ProjectHelper.addJarsToClasspath(javaProject,monitor, "lib");
		ProjectHelper.addJarsToClasspath(javaProject,monitor, "webContent/WEB-INF/lib");
		
		final IFile file = currProject.getFile(new Path("webContent/WEB-INF/bb-manifest.xml"));
		if (file.exists()) {
			monitor.worked(1);
			monitor.setTaskName("Opening Building Block manifest for editing...");
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						IDE.openEditor(page, file, true);
					} catch (PartInitException e) {
					}
				}
			});
			monitor.worked(1);
		}
		
		
	}
	


	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "PluginTest", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		ImageDescriptor banner = AbstractUIPlugin.imageDescriptorFromPlugin(BbPlugin.PLUGIN_ID, "icons/b2-wiz-banner.png");
		this.setDefaultPageImageDescriptor(banner);
	}
	
}