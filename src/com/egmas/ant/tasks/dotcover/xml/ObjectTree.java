package com.egmas.ant.tasks.dotcover.xml;

import java.util.ArrayList;
import java.util.List;

public class ObjectTree {
	private ObjectTree parent;
	private String name;

	private List<ObjectTree> children = new ArrayList<ObjectTree>();
	private String content;

	public ObjectTree(Class<?> name) {
		this(name, null);
	}

	public ObjectTree(Class<?> name, ObjectTree parent) {
		this.name = name.getSimpleName();
		this.parent = parent;
	}

	public void addChild(ObjectTree child) {
		children.add(child);
	}

	public boolean isLeaf() {
		return children.isEmpty();
	}

	public List<ObjectTree> getChildren() {
		return children;
	}

	public String getName() {
		return name;
	}

	public ObjectTree getParent() {
		return parent;
	}

	public void setName(String name) {
		this.name = name;

	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public void removeChild(ObjectTree node) {
		children.remove(node);
	}

	@Override
	public String toString() {
		return name + " (" + children.size() + " children): " + content;
	}
}
