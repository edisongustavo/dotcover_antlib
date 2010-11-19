package com.egmas.ant.tasks.dotcover.xml;

import java.util.HashMap;
import java.util.Map;

import com.egmas.ant.tasks.dotcover.utils.StringUtils;

/**
 * Mimicks the XStream library (http://xstream.codehaus.org/)
 * <p>
 * Note that it doesn't have support to output xml attributes at the elements
 * <p>
 * I wrote this class because I really like the API of XStream and I needed a
 * very small part of it to make dotCover work. Since I didn't want to put a
 * dependency on an external jar I wrote this.
 * 
 * @author emuenz
 * 
 */
public class XStream {

	private StringBuffer ss;
	private Map<Class<?>, String> mappedClasses = new HashMap<Class<?>, String>();
	private Map<Class<?>, String> implicitCollections = new HashMap<Class<?>, String>();

	public String toXML(Object root) {
		try {
			ss = new StringBuffer();
			TreeBuilder treeBuilder = new TreeBuilder(mappedClasses,
					implicitCollections);
			ObjectTree tree = treeBuilder.makeTree(root);
			serialize(tree);

			return ss.toString();

		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private void serialize(ObjectTree tree) {
		serialize(tree, 0);
	}

	private void serialize(ObjectTree tree, int identation) {
		String name = tree.getName();

		ss.append(StringUtils.repeat("\t", identation));
		startTag(name);

		if (!tree.isLeaf())
			ss.append("\n");

		if (tree.getContent() != null)
			ss.append(normalize(tree.getContent()));

		for (ObjectTree child : tree.getChildren())
			serialize(child, identation + 1);

		if (!tree.isLeaf())
			ss.append(StringUtils.repeat("\t", identation));
		endTag(name);
		ss.append("\n");
	}

	private String normalize(String content) {
		content = content.replaceAll("<", "&lt;");
		content = content.replaceAll(">", "&gt;");
		return content;
	}

	private void endTag(String name) {
		ss.append("</" + name + ">");
	}

	private void startTag(String tag) {
		ss.append("<" + tag + ">");
	}

	public void alias(String string, Class<?> cls) {
		mappedClasses.put(cls, string);
	}

	public void addImplicitCollection(Class<?> cls, String fieldName) {
		implicitCollections.put(cls, fieldName);
	}
}
