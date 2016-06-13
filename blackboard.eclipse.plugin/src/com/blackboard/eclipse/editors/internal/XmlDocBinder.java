package com.blackboard.eclipse.editors.internal;

import java.util.HashMap;

import org.eclipse.jdt.core.jdom.IDOMNode;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wst.sse.core.internal.provisional.IModelStateListener;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMElement;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlDocBinder implements IDocumentListener, INodeAdapter, IModelStateListener {
	
	
	HashMap<String, Text> pathToTextMapping = new HashMap<String, Text>();

	@Override
	public void documentAboutToBeChanged(DocumentEvent arg0) {
		
		
	}

	@Override
	public void documentChanged(DocumentEvent arg0) {
		System.out.println("Document Changed");
		
	}

	
	
	@Override
	public boolean isAdapterForType(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void notifyChanged(INodeNotifier notifier,
			int eventType, Object changedFeature, Object oldValue,
			Object newValue, int pos) {

		System.out.print("Node Change type: ");

		switch (eventType) {
		case INodeNotifier.ADD:
			System.out.print("ADD ");
			break;
		case INodeNotifier.CHANGE:
			System.out.print("CHANGE ");
			break;
		case INodeNotifier.CONTENT_CHANGED:
			System.out.print("CONTENT_CHANGED ");
			break;
		case INodeNotifier.REMOVE:
			System.out.print("REMOVE ");
			break;
		case INodeNotifier.STRUCTURE_CHANGED:
			System.out.print("STRUCTURE_CHANGED ");
			break;

		default:
			System.out.print("Unknown ");
			break;
		}
		System.out.println(changedFeature == null?"":changedFeature.getClass());
	}

	@Override
	public void modelAboutToBeChanged(IStructuredModel arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modelAboutToBeReinitialized(IStructuredModel arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modelChanged(IStructuredModel structuredModel) {
		System.out.println("Model Changed");
		IDOMModel model = (IDOMModel)structuredModel;
		
		Element rootElement = model.getDocument().getDocumentElement();
		String nodeName = rootElement.getNodeName();
		Element currentElement = rootElement;
		NodeList children = currentElement.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i) instanceof Element) {
				System.out.println(children.item(i).getNodeName());
			}
		}
		System.out.println(nodeName);
	}
	

	@Override
	public void modelDirtyStateChanged(IStructuredModel arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modelReinitialized(IStructuredModel arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modelResourceDeleted(IStructuredModel arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modelResourceMoved(IStructuredModel arg0, IStructuredModel arg1) {
		// TODO Auto-generated method stub
		
	}

	
	public void bindTextToAttribute(Text text, String elementPath, String attributeName) {
		pathToTextMapping.put(elementPath + ":" + attributeName, text);
	}
	
}
