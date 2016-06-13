package com.blackboard.eclipse.editors.internal;

import java.util.StringTokenizer;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.ufacekit.core.databinding.instance.observable.ILazyObserving;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BbManifestObserving implements ILazyObserving{
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
			"manifest/plugin/permissions/permission" };

	public BbManifestObserving(Node root, String xPathExpression,
			boolean lazy) throws XPathExpressionException {
		createStructureModel();
		xPathStr = xPathExpression;
		this.root = root;
	}

	public Object getObserved() {
		Element currElem = null;
		if (root instanceof IDOMDocument) {
			Document doc = (Document) root;
			currElem = doc.getDocumentElement();
		} else if (root instanceof Element) {
			currElem = (Element) root;
		}
		StringTokenizer tokenizer = new StringTokenizer(xPathStr, "/");
		String currToken = tokenizer.nextToken();
		while (tokenizer.hasMoreTokens()) {
			currToken = tokenizer.nextToken();
			currElem = getNamedChildFromModel(currElem, currToken);
			if (currElem == null) {
				System.out.println("returning null for "+xPathStr);
				return currElem;
			}
		}
		System.out.println("returning "+currElem.getLocalName()+" node for "+xPathStr);
		return currElem;
	}
	
	@Override
	public Object createObserved() {
		System.out.println("Creating "+xPathStr);
		StringTokenizer tokenizer = new StringTokenizer(xPathStr, "/");
		Element rootElem = null;
		Element child = null;
		Document doc = null;
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
			String elemName = tokenizer.nextToken();
			if (rootElem.getNodeName().equals(elemName)) {
				child = rootElem;
			} else {
				child = getNamedChildFromModel(currentParent, elemName);
			}
			StructureNode structureNodeChild = currStructureNode
					.getChild(elemName);
			
			if (child == null) {
				int refChildPos;
				// figure out what position the structure node is
				for (refChildPos = 0; refChildPos < currStructureNode
						.getChildren().size(); refChildPos++) {
					if (structureNodeChild.equals(currStructureNode
							.getChildren().get(refChildPos))) {
						
						break;
					}
				}
				Element nextSibling= null;
				
				// for each subsequent sibling, check if it exists in the model
				for (refChildPos = refChildPos + 1; refChildPos < currStructureNode
						.getChildren().size(); refChildPos++) {
					StructureNode nextStrucNode =currStructureNode.getChildren().get(refChildPos);
					nextSibling = getNamedChildFromModel(currentParent, nextStrucNode.getName());
					if (nextSibling != null) {
						break;
					}
				}
				child = (currentParent).getOwnerDocument().createElement(elemName);
				if (nextSibling != null) {
					currentParent.insertBefore(child, nextSibling);
				} else {
					currentParent.appendChild(child);	
				}
//				currentParent.notifyAll();
			}
			currStructureNode = structureNodeChild;
			currentParent = child;
		}
		
		return child;
	}

	/**
	 * build a model for comparison when creating new nodes, so that we insert
	 * nodes in the right order.
	 */
	public void createStructureModel() {
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
