package com.blackboard.eclipse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

import com.blackboard.eclipse.wizards.BuildingBlockNewWizard;

import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ProjectHelper {

	private static final String PACKAGE_PATH_STR = "[[PACKAGE-PATH]]";

	public static void createFilesFromTemplate(IProject project,
			IProgressMonitor monitor, String templateName, Map<String, Object> templateModel) throws CoreException {
		
		Configuration cfg = new Configuration();
		cfg.setTemplateLoader(new URLTemplateLoader() {
			@Override
			protected URL getURL(String urlStr) {
				try {
					URL url = new URL(urlStr);
					return url;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		cfg.setLocalizedLookup(false);

		StringWriter writer;
		
		
//		IFolder rootFolder = project.getFolder(project.getLocation().makeAbsolute());
		IContainer rootFolder = (IContainer) project;
		
		int templatePathLength = templateName.split("/").length;
		Bundle pluginBundle = Platform.getBundle(BbPlugin.PLUGIN_ID);
		Enumeration<String> pathsEnum = pluginBundle.getEntryPaths(templateName);
		Enumeration<URL> urlsEnum = pluginBundle.findEntries(templateName, "*.*", true);
		if (pathsEnum != null) {
			List<URL> urls = Collections.list(urlsEnum);

			
			for (URL url : urls) {
				InputStream stream = null;
				writer = new StringWriter();
				//TODO Will we need to include . files? Is excluding .svn enough? Is enough for now.
				if (url.getPath().contains(".svn")) {
					continue;
				}
				//The +2accounts for the leading and following /. 
				String path = url.getPath().substring(templateName.length()+2);
				String[] filePathSegments = path.split("/");
				
				IFile fileToCreate;
				if (filePathSegments.length == 1) {
					fileToCreate = rootFolder.getFile(new Path(filePathSegments[0]));
				} else {
					IFolder currentFolder =  traverseAndCreateFolders(project, filePathSegments, (String)templateModel.get(BuildingBlockNewWizard.BASE_PACKAGE_KEY), monitor);
					fileToCreate = currentFolder.getFile(filePathSegments[filePathSegments.length - 1]);
				}
				
				try {
					// Don't process jar files using FreeMarker
					String filename = fileToCreate.getName().toLowerCase();
					if (filename.endsWith("jar")||filename.endsWith("zip")||filename.endsWith("png")
							||filename.endsWith("jpg")||filename.endsWith("gif")||filename.endsWith("ico")) {
						stream = url.openStream();
					} else {
						// Do process all other files using Freemarker
						Template template = cfg.getTemplate(url.toExternalForm());
						try {
							template.process(templateModel, writer);
						} catch (TemplateException e) {
							e.printStackTrace();
							throw new CoreException(new Status(IStatus.ERROR, BbPlugin.PLUGIN_ID, e.getMessage()));
						}
						stream = new ByteArrayInputStream(writer.toString().getBytes());
					}
					if (fileToCreate.exists()) {
						fileToCreate.delete(true, false, monitor);
					}
					fileToCreate.create(stream, true, monitor);
					
				} catch (IOException e) {
					e.printStackTrace();
					IStatus status = new Status(IStatus.ERROR, BbPlugin.PLUGIN_ID, e.getMessage());
					throw new CoreException(status);
				} finally {
					try {
						writer.close();
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		}
		
	}

	private static IFolder traverseAndCreateFolders(IProject project, String[] pathSegments, String packagePath, IProgressMonitor monitor) throws CoreException {
		ArrayList<String> packageSegments = new ArrayList<String>();
		for (String packageSegment : packagePath.split("\\.")) {
			packageSegments.add(packageSegment);
		}
		String[] pathSegmentsLocal = expandPackagePath(pathSegments, packageSegments);
		
		IFolder childFolder = project.getFolder(new Path(pathSegmentsLocal[0]));
		if (!childFolder.exists()) {
			childFolder.create(true, true, monitor);
		}
		int counter = 0;
		for (String segment : pathSegmentsLocal) {
			if (counter++ == 0) {
				continue;
			}
			if (counter == pathSegmentsLocal.length) {
				continue;
			} else {
				childFolder = childFolder.getFolder(segment);
				if (!childFolder.exists()) {
					childFolder.create(true, true, monitor);
				}
			}
		}
		return childFolder;
	}
	
	/** 
	 * given an array of path segments, and a java package path, for every segment 
	 * which has the value "[[PACKAGE-PATH]]" the package path will be expanded in place
	 * of that path segment.
	 * @param pathSegments 
	 * @param packagePath a java package of the form "com.company.product"
	 * @return 
	 */
	private static String[] expandPackagePath(String[] pathSegments, ArrayList<String> packageSegments) {

		ArrayList<String> expandedSegments = new ArrayList<String>();;
		for (int i = 0; i < pathSegments.length; i++) {
			if (pathSegments[i].equals(PACKAGE_PATH_STR)) {
				expandedSegments.addAll(packageSegments);
			} else {
				expandedSegments.add(pathSegments[i]);
			}
		}
		return expandedSegments.toArray(pathSegments);
	}

	public static void addJarsToClasspath(IJavaProject javaProject,	
			IProgressMonitor monitor, String libPath) throws CoreException {
		
		IFolder libFolder = javaProject.getProject().getFolder(libPath);
		
		if (!libFolder.exists()) {
			return;
		}
		IResource[] files = libFolder.members();

		ArrayList<IClasspathEntry> newCpEntries = new ArrayList<IClasspathEntry>();
		for (IResource resource: files) {
			if (resource.getName().endsWith("jar")) {
				IPath jarPath = resource.getFullPath();
				IClasspathEntry cpEntry = JavaCore.newLibraryEntry(jarPath, null, null);
				newCpEntries.add(cpEntry);
			}
		}
		
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		
		for (IClasspathEntry cpEntry : oldEntries) {
			newCpEntries.add(cpEntry);
		}
		
		javaProject.setRawClasspath(newCpEntries.toArray(oldEntries), monitor);
		
	}
	
}
