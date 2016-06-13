package com.blackboard.eclipse.editors;

import java.io.StringWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.SharedHeaderFormEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.document.DocumentTypeAdapter;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.blackboard.eclipse.editors.internal.XmlDocBinder;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class BbManifestMultiPageEditor extends FormEditor {

	private StructuredTextEditor xmlEditor = new StructuredTextEditor();
	IDocument mDocument;
	private IDOMModel model;

	protected void addPages() {
		try {
			
			
			addPage(new GeneralDetailsFormPage(this));
			addPage(new AppDefsFormPage(this));
//			addPage(new ModuleDefsFormPage(this)); //TODO do this later
//			addPage(new ContentHandlersFormPage(this)); //TODO do this later
			addPage(new PermissionsFormPage(this));
			xmlEditor = new StructuredTextEditor();
			addPage(xmlEditor, this.getEditorInput());
			this.setPageText(3, Messages.xmlPage_label);
			this.setPartName("gu-assignprint");

			IDocumentProvider provider = xmlEditor.getDocumentProvider();
			mDocument = provider.getDocument(getEditorInput());
			IModelManager modelManager = StructuredModelManager.getModelManager();
			model = (IDOMModel) modelManager.getExistingModelForEdit(mDocument);			


//			IDocumentProvider provider = xmlEditor.getDocumentProvider();
//			mDocument = provider.getDocument(getEditorInput());

//			mDocument.addDocumentListener(new XmlDocBinder());
//			mDocument.addDocumentListener(new IDocumentListener() {
//				public void documentChanged(DocumentEvent event) {
//					// onDocumentChanged(event);
//					System.out.println("Document Changed.");
//				}
//
//				public void documentAboutToBeChanged(DocumentEvent event) {
//					// ignore
//				}
//			});

//			

//			model.addModelStateListener(new XmlDocBinder());
//			IDOMDocument doc = model.getDocument();
			
			
			
			
			
//			System.out.println(doc.getDocumentElement().getNodeName());
//			NodeList pluginList = doc.getElementsByTagName("name");
//			IDOMElement root = (IDOMElement) doc.getDocumentElement();
//			IDOMElement pluginElement = getElement(root, "plugin");
//			IDOMElement vendorElement = getElement(pluginElement, "vendor");
//			((IDOMNode) vendorElement).addAdapter(new INodeAdapter() {
//
//				@Override
//				public void notifyChanged(INodeNotifier notifier,
//						int eventType, Object changedFeature, Object oldValue,
//						Object newValue, int pos) {
//
//					System.out.print("Node Change type: ");
//
//					switch (eventType) {
//					case INodeNotifier.ADD:
//						System.out.print("ADD ");
//						break;
//					case INodeNotifier.CHANGE:
//						System.out.print("CHANGE ");
//						break;
//					case INodeNotifier.CONTENT_CHANGED:
//						System.out.print("CONTENT_CHANGED ");
//						break;
//					case INodeNotifier.REMOVE:
//						System.out.print("REMOVE ");
//						break;
//					case INodeNotifier.STRUCTURE_CHANGED:
//						System.out.print("STRUCTURE_CHANGED ");
//						break;
//
//					default:
//						System.out.print("Unknown ");
//						break;
//					}
//					System.out.println(changedFeature == null?"":changedFeature.getClass());
//				}
//
//				@Override
//				public boolean isAdapterForType(Object type) {
//					System.out.println("isAdapterForType("+type.getClass()+")");
//					return true;
//				}
//			});
//			String pluginHandle = getElement(pluginElement, "handle")
//					.getAttribute("value");
//			String vendorId = getElement(vendorElement, "id").getAttribute(
//					"value");
//
//			this.setPartName(vendorId + "-" + pluginHandle);

			// System.out.println(pluginNode.getAttributes().getNamedItem("value").getTextContent());

		} catch (PartInitException e) {
			//
		}
	}

	public static IDOMElement getElement(IDOMElement base, String tagName) {
		NodeList list = base.getElementsByTagName(tagName);
		if (list.getLength() == 0) {
			return null;
		} else {
			return (IDOMElement) list.item(0);
		}
	}

	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		xmlEditor.doSave(progressMonitor);
		
	}

	@Override
	public void doSaveAs() {
		// Can't Do a save as using this editor

	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	public void setEditorTitle(String title) {
		this.setPartName(title);
	}

	public IDOMModel getModel() {
		IFile file = ((IFileEditorInput) this.getEditorInput()).getFile();
		try {
			this.model = getDOMModel(file);
			return getDOMModel(file);
		} catch (Exception e) {
			throw new RuntimeException("Invalid Input: Must be DOM", e);
		}
	}
	
	private IDOMModel getDOMModel(IFile file) throws Exception {

		IModelManager manager = StructuredModelManager.getModelManager();
		IStructuredModel model = manager.getExistingModelForRead(file);
		if (model == null) {
			model = manager.getModelForRead(file);
		}
		if (model == null) {
			throw new Exception(
					"DOM Model is null, check the content type of your file (it appears that it's not *.xml file)");
		}
		if (!(model instanceof IDOMModel)) {
			throw new Exception("Model retrieved is not a DOM Model!!!");
		}

		// Add listener to observe change of DOM (change is done with another
		// editor).
//		model.addModelStateListener(listener);

		// model.setReinitializeNeeded(true);

		return (IDOMModel) model;
	}
	
}
