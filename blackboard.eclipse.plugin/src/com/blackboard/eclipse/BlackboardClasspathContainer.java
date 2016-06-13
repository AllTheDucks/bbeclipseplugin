package com.blackboard.eclipse;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import com.blackboard.eclipse.sdk.BlackboardSdk;

public class BlackboardClasspathContainer implements IClasspathContainer {
	
	public static final String CONTAINER_ID = "blackboard.eclipse.plugin.CLASSPATH_CONTAINER";
	
	private IPath containerPath;
	private IJavaProject project;
	private String version;
	private File sdkDir;

	public BlackboardClasspathContainer(IPath containerPath, IJavaProject project) {
		this.containerPath = containerPath;
		this.project = project;
		
		version = containerPath.removeFirstSegments(1).toString();
		BlackboardSdk sdk = BbPlugin.getDefault().getSdkManager().getBlackboardSdk(version);
		
		if (sdk != null) {
			sdkDir = new File(sdk.getPath());
		}
		
	}

	
	
	@Override
	public IClasspathEntry[] getClasspathEntries() {
		ArrayList<IClasspathEntry> classpathEntries = new ArrayList<IClasspathEntry>();
		if (sdkDir == null) {
			return classpathEntries.toArray(new IClasspathEntry[]{});
		}
		File[] libs = sdkDir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.getName().endsWith("jar");
			}
		});
		Arrays.sort(libs, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return o1.getAbsolutePath().compareToIgnoreCase(o2.getAbsolutePath());
			}
		});
		for (File lib : libs) {
			classpathEntries.add(JavaCore.newLibraryEntry(new Path(lib.getAbsolutePath()), null, new Path("/")));
		}
		return classpathEntries.toArray(new IClasspathEntry[]{});
	}

	@Override
	public String getDescription() {
		//TODO change this version to a decorator
		return "Blackboard Library ["+version+"]";
	}

	@Override
	public int getKind() {
		// TODO Auto-generated method stub
		return K_APPLICATION;
	}

	@Override
	public IPath getPath() {
		return containerPath;
	}
	
	public String getVersion() {
		return version;
	}

	
}
