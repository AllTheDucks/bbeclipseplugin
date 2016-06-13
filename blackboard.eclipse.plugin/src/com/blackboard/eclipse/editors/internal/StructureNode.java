package com.blackboard.eclipse.editors.internal;

import java.util.ArrayList;
import java.util.List;

public class StructureNode {
	private String name;
	private StructureNode parent;
	private List<StructureNode> children;
	
	public StructureNode() {
		children = new ArrayList<StructureNode>();
	}
	
	public StructureNode(String name) {
		this();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public StructureNode getParent() {
		return parent;
	}
	public void setParent(StructureNode parent) {
		this.parent = parent;
	}
	public List<StructureNode> getChildren() {
		return children;
	}
	public void setChildren(List<StructureNode> children) {
		this.children = children;
	}
	/**
	 * gets an immediate child by name, or null if no child by that name.
	 * @param name
	 * @return
	 */
	public StructureNode getChild(String name) {
		for (StructureNode child: children) {
			if (child.getName().equals(name)) {
				return child;
			}
		}
		return null;
	}
	public void addChild(StructureNode child) {
		children.add(child);
		child.setParent(this);
	}

	@Override
	public String toString() {
		return "<"+(parent==null?null:parent.getName())+">  <--  <"+this.getName()+">  -->  <"+children.size()+">";
	}
}
