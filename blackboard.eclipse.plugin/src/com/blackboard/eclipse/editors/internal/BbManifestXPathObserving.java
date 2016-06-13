package com.blackboard.eclipse.editors.internal;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.eclipse.ufacekit.core.databinding.sse.dom.xpath.AbstractXPathObserving;
import org.eclipse.ufacekit.core.databinding.sse.dom.xpath.XPathObserving;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IModelManager;
import org.eclipse.wst.sse.core.internal.provisional.IStructuredModel;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public class BbManifestXPathObserving extends XPathObserving {

	private String xPathStr;
	private Node root;
	private static StructureNode structureModelRoot;
	private static String[] structure = { 
		"manifest", 
		"manifest/plugin", 
		"manifest/plugin/name",
		"manifest/plugin/handle", 
		"manifest/plugin/description", 
		"manifest/plugin/version",
		"manifest/plugin/requires", 
		"manifest/plugin/requires/bbversion", 
		"manifest/plugin/vendor",
		"manifest/plugin/vendor/id", 
		"manifest/plugin/vendor/name", 
		"manifest/plugin/vendor/url",
		"manifest/plugin/vendor/description", 
		"manifest/plugin/http-actions",
		"manifest/plugin/http-actions/config", 
		"manifest/plugin/http-actions/remove",
		"manifest/plugin/application-defs", 
		"manifest/plugin/application-defs/application",
		"manifest/plugin/application-defs/application/description",
		"manifest/plugin/application-defs/application/links",
		"manifest/plugin/application-defs/application/links/link",
		"manifest/plugin/application-defs/application/links/link/type",
		"manifest/plugin/application-defs/application/links/link/name",
		"manifest/plugin/application-defs/application/links/link/url",
		"manifest/plugin/application-defs/application/links/link/description",
		"manifest/plugin/application-defs/application/links/link/icons",
		"manifest/plugin/application-defs/application/links/link/icons/listitem", 
		"manifest/plugin/application-defs/application/links/link/icons/toolbar",
		"manifest/plugin/module-defs",
		"manifest/plugin/module-defs/module-type", 
		"manifest/plugin/module-defs/module-type/jsp-dir",
		"manifest/plugin/module-defs/module-type/jsp", 
		"manifest/plugin/module-defs/module-type/jsp/view",
		"manifest/plugin/content-handlers", 
		"manifest/plugin/content-handlers/content-handler",
		"manifest/plugin/content-handlers/content-handler/name",
		"manifest/plugin/content-handlers/content-handler/handle",
		"manifest/plugin/content-handlers/content-handler/http-actions",
		"manifest/plugin/content-handlers/content-handler/http-actions/create",
		"manifest/plugin/content-handlers/content-handler/http-actions/modify",
		"manifest/plugin/content-handlers/content-handler/http-actions/remove",
		"manifest/plugin/content-handlers/content-handler/http-actions/can-copy",
		"manifest/plugin/content-handlers/content-handler/icons",
		"manifest/plugin/content-handlers/content-handler/icons/toolbar",
		"manifest/plugin/content-handlers/content-handler/icons/listitem", 
		"manifest/plugin/permissions",
		"manifest/plugin/permissions/permission" 
		};

	public BbManifestXPathObserving(Node root, String xPathExpression, boolean lazy) throws XPathExpressionException {
		super(root, xPathExpression, lazy);
		createStructureModel();
		xPathStr = xPathExpression;
		this.root = root;
	}

	
	@Override
	public Object createObserved() {
		System.out.println("Creating " + xPathStr);
		StringTokenizer tokenizer = new StringTokenizer(xPathStr, "/");
		Element rootElem = null;
		Element child = null;
		Document doc = null;
		int elementIndex = 0;
		if (root instanceof IDOMDocument) {
			doc = (Document) root;
			rootElem = doc.getDocumentElement();
		} else if (root instanceof Element) {
			rootElem = (Element) root;
		}
		if (rootElem == null) {
			child = doc.createElement(tokenizer.nextToken());
			root.appendChild(child);
		}

		Element currentParent = rootElem;
		StructureNode currStructureNode = structureModelRoot;

		while (tokenizer.hasMoreTokens()) {
			elementIndex = 0;
			String elemName = tokenizer.nextToken();
			NodeList namedChildren = null;
			int openBraceIndex = elemName.indexOf('[');
			int closeBraceIndex = elemName.indexOf(']');
			if (openBraceIndex != -1) {
				// XPath syntax has the first element index as 1, not 0
				elementIndex = Integer.parseInt(elemName.substring(openBraceIndex + 1, closeBraceIndex)) - 1;
				elemName = elemName.substring(0, openBraceIndex);
			}

			if (rootElem.getNodeName().equals(elemName)) {
				child = rootElem;
			} else {
				namedChildren = currentParent.getElementsByTagName(elemName);
				if (namedChildren.getLength() != 0 && namedChildren.getLength() > elementIndex) {
					child = (Element) namedChildren.item(elementIndex);
				} else {
					child = null;
				}
			}
			StructureNode structureNodeChild = currStructureNode.getChild(elemName);

			if (child == null) {

				Node nextSibling = null;

				if (namedChildren.getLength() == 0) {
					// There are no existing children with the element name
					// we're looking for, so figure out where to put the new one
					int refChildPos;
					// figure out what position the structure node is
					for (refChildPos = 0; refChildPos < currStructureNode.getChildren().size(); refChildPos++) {
						if (structureNodeChild.equals(currStructureNode.getChildren().get(refChildPos))) {
							break;
						}
					}

					// for each subsequent sibling, check if it exists in the
					// model
					for (refChildPos = refChildPos + 1; refChildPos < currStructureNode.getChildren().size(); refChildPos++) {
						StructureNode nextStrucNode = currStructureNode.getChildren().get(refChildPos);
						nextSibling = getNamedChildFromModel(currentParent, nextStrucNode.getName());
						if (nextSibling != null) {
							break;
						}
					}
				} else {
					int lastChildIndex = namedChildren.getLength() - 1;
					Element lastChild = (Element) namedChildren.item(lastChildIndex);
					nextSibling = lastChild.getNextSibling();
				}

				// insert enough children to match the index of the target
				// (child) element
				for (int i = namedChildren.getLength(); i <= elementIndex; i++) {
					child = (currentParent).getOwnerDocument().createElement(elemName);
					if (nextSibling != null) {
						currentParent.insertBefore(child, nextSibling);
					} else {
						currentParent.appendChild(child);
					}
				}

			}
			currStructureNode = structureNodeChild;
			currentParent = child;
		}

		return child;
	}

	public final IStructuredModel getModelForEdit(IStructuredDocument document) {

		if (document != null) {
			IModelManager mm = StructuredModelManager.getModelManager();
			if (mm != null) {
				return mm.getModelForEdit(document);
			}
		}
		return null;
	}

	/**
	 * build a model for comparison when creating new nodes, so that we insert
	 * nodes in the right order.
	 */
	public void createStructureModel() {
		if (structureModelRoot != null) {
			return;
		}
		for (String path : structure) {
			StringTokenizer tokenizer = new StringTokenizer(path, "/");
			StructureNode currentParent = structureModelRoot;
			while (tokenizer.hasMoreTokens()) {
				String elemName = tokenizer.nextToken();
				if (structureModelRoot == null) {
					structureModelRoot = new StructureNode(elemName);
					currentParent = structureModelRoot;
				}
				if (currentParent.getChild(elemName) != null) {
					currentParent = currentParent.getChild(elemName);
				} else {
					currentParent.addChild(new StructureNode(elemName));
				}

			}
		}
	}

	private Element getNamedChildFromModel(Node modelNode, String name) {
		NodeList children = modelNode.getChildNodes();
		Element child = null;
		for (int i = 0; i < children.getLength(); i++) {
			if (children.item(i).getNodeName().equals(name)) {
				Node childNode = children.item(i);
				if (childNode instanceof Element) {
					child = (Element) childNode;
				}
			}
		}
		return child;
	}
}
