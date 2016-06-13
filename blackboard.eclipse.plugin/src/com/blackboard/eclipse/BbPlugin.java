package com.blackboard.eclipse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.blackboard.eclipse.sdk.SdkManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class BbPlugin extends AbstractUIPlugin {

	public static final String PROJECT_TEMPLATES_FILENAME = "templates/project-templates.xml";

	// The plug-in ID
	public static final String PLUGIN_ID = "blackboard.eclipse.plugin"; //$NON-NLS-1$
	
	// The name of the property holding the path to the Blackboard SDKs
	public static final String SDK_ROOT_PATH_PROP = "sdkRootPath";
	
	/** The valid Elements in a templates.xml file. */
	public enum TemplateElement {
	    TEMPLATES, TEMPLATE, NAME, DESCRIPTION 
	}
	
	// The shared instance
	private static BbPlugin plugin;
	
	private SdkManager sdkManager;
	
	private ArrayList<ProjectTemplate> templates;
	
	/**
	 * The constructor
	 */
	public BbPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		sdkManager = new SdkManager();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static BbPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public String getSdkRootDirPath() {
		return getPreferenceStore().getString(SDK_ROOT_PATH_PROP);
	}
	
	public void setSdkRootDirPath(String path) {
		getPreferenceStore().setValue(SDK_ROOT_PATH_PROP, path);
		getSdkManager().loadSdks(path);
	}

	public void setSdkManager(SdkManager sdkManager) {
		this.sdkManager = sdkManager;
	}

	public SdkManager getSdkManager() {
		return sdkManager;
	}
	
	public List<ProjectTemplate> getTemplates() {
		if (templates == null) {
			loadTemplates();
		}
		return templates;
	}

	private void loadTemplates() {
		templates = new ArrayList<ProjectTemplate>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = null;
		try {
			saxParser = factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		DefaultHandler projectTemplateSaxHandler = new DefaultHandler() {
			ProjectTemplate currentTemplate;
			TemplateElement currentElement;
			StringBuffer buff = new StringBuffer();

			public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes)
					throws org.xml.sax.SAXException {
				if (qName.equalsIgnoreCase(TemplateElement.TEMPLATE.toString())) {
					currentTemplate = new ProjectTemplate();
					currentTemplate.setId(attributes.getValue("id"));
					templates.add(currentTemplate);
					currentElement = TemplateElement.TEMPLATE;
				}
				if (qName.equalsIgnoreCase(TemplateElement.NAME.toString())) {
					currentElement = TemplateElement.NAME;
				}
				if (qName.equalsIgnoreCase(TemplateElement.DESCRIPTION.toString())) {
					currentElement = TemplateElement.DESCRIPTION;
				}

			};

			@Override
			public void endElement(String uri, String localName, String qName) throws SAXException {
				if (qName.equalsIgnoreCase(TemplateElement.NAME.toString())) {
					currentTemplate.setName(buff.toString());
					buff = new StringBuffer();
					currentElement = null;
				}
				if (qName.equalsIgnoreCase(TemplateElement.DESCRIPTION.toString())) {
					currentTemplate.setDescription(buff.toString());
					buff = new StringBuffer();
					currentElement = null;
				}
			}

			@Override
			public void characters(char[] ch, int start, int length) throws SAXException {
				if (currentElement == TemplateElement.NAME || currentElement == TemplateElement.DESCRIPTION) {
					buff.append(Arrays.copyOfRange(ch, start, start + length));
				}
			}
		};
		Bundle pluginBundle = Platform.getBundle(BbPlugin.PLUGIN_ID);
		InputStream stream;
		try {
			stream = pluginBundle.getEntry(PROJECT_TEMPLATES_FILENAME).openStream();
			saxParser.parse(stream, projectTemplateSaxHandler);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} 
	}
	
	
	
}
