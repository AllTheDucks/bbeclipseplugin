package com.blackboard.eclipse;

import java.io.File;

import org.eclipse.ant.core.AntRunner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class DeployB2Action implements IObjectActionDelegate {

	private IProject selectedProject;

	@Override
	public void run(IAction action) {

		final File buildFile = new File(selectedProject.getLocation().toOSString() + "/build.xml");
		

		if (buildFile.exists()) {
			Shell workbenchShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			Job b2Job = new Job("Deploy Building Block") {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("Deploy Building block", 3);
					AntRunner runner = new AntRunner();
					runner.setBuildFileLocation(buildFile.getAbsolutePath());
					runner.setExecutionTargets(new String[] {"deploy-b2"});
					runner.setArguments("-Dmessage=Building -verbose");
					runner.addBuildListener("org.eclipse.ant.internal.core.ant.ProgressBuildListener");
					monitor.subTask("Uploading B2");

					try {
						runner.run(monitor);
						while(runner.isBuildRunning()) {
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								runner.stop();
								return Status.CANCEL_STATUS;
							}
						}
					} catch (CoreException e) {
						e.printStackTrace();
						monitor.setCanceled(true);
					}
					return Status.OK_STATUS;
				}
			};
			b2Job.setUser(true);
			b2Job.schedule();
//			ProgressMonitorDialog pmd = new ProgressMonitorDialog(workbenchShell);
//			pmd.setCancelable(true);
//			pmd.create();
//			IProgressMonitor monitor = pmd.getProgressMonitor();
//			
//			try {
//				pmd.run(true, true, new IRunnableWithProgress() {
//
//					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
//
//						int steps = 3;
//
//
//					}
//				});
//			} catch (InvocationTargetException ex1) {
//				// TODO Do something here.
//			} catch (InterruptedException ex2) {
//				// TODO Do something here.
//			}
//			if (monitor.isCanceled()) {
//				// TODO Do something here.
//			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		Object element = ((StructuredSelection) selection).getFirstElement();
		if (element instanceof IProject) {
			selectedProject = (IProject) element;
		}
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

}
