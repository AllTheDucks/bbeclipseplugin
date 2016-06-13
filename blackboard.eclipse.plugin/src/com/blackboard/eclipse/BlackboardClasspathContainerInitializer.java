package com.blackboard.eclipse;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class BlackboardClasspathContainerInitializer extends ClasspathContainerInitializer {

	@Override
	public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
		BlackboardClasspathContainer container = new BlackboardClasspathContainer(containerPath, project);
		JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project },
				new IClasspathContainer[] { container }, null);
	}

}
