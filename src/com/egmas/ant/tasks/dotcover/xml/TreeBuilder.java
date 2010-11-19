package com.egmas.ant.tasks.dotcover.xml;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class TreeBuilder {
	private Map<Class<?>, String> mappedClasses;
	private Map<Class<?>, String> implicitCollections;

	public TreeBuilder(Map<Class<?>, String> mappedClasses,
			Map<Class<?>, String> implicitCollections) {
		this.mappedClasses = mappedClasses;
		this.implicitCollections = implicitCollections;
	}

	/**
	 * Recurses over the three formed by the object <code>root</code>
	 * <p>
	 * Takes into consideration if implicitCollections were set
	 * 
	 * @param object
	 *            the object containing the data
	 * @param parent
	 *            the parent node
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private ObjectTree recurse(Object object, Object parentObject,
			ObjectTree parent) throws IllegalArgumentException,
			IllegalAccessException {
		Class<?> cls = object.getClass();
		ObjectTree node = new ObjectTree(cls, parent);

		String str = mappedClasses.get(cls);
		if (str != null)
			node.setName(str);

		if (isPrimitive(cls))
			return handlePrimitive(object, node);

		if (isCollection(cls))
			return handleCollection(object, parentObject, node);

		for (Field field : cls.getDeclaredFields()) {
			field.setAccessible(true);

			ObjectTree child = recurse(field.get(object), object, node);
			if (child == null)
				continue;

			child.setName(field.getName());
			node.addChild(child);
		}

		return node;
	}

	private ObjectTree handlePrimitive(Object root, ObjectTree node) {
		// at the primitive level there is content
		String content = (String) root;

		node.setContent(content);

		return node;
	}

	/**
	 * When the field refers to a collection we must iterate over its values and
	 * add them to the tree
	 * 
	 * @param root
	 * @param parentObject
	 * @param node
	 * @return
	 * @throws IllegalAccessException
	 */
	private ObjectTree handleCollection(Object root, Object parentObject,
			ObjectTree node) throws IllegalAccessException {
		List<?> l = (List<?>) root;

		String implicitCollectionField = implicitCollections.get(parentObject
				.getClass());
		boolean addToParent = implicitCollectionField != null;

		for (Object obj : l) {
			ObjectTree newNode = recurse(obj, root, node);

			if (addToParent)
				node.getParent().addChild(newNode);
			else
				node.addChild(newNode);
		}
		return addToParent ? null : node;
	}

	private boolean isCollection(Class<?> cls) {
		Class<?> other = List.class;
		return other.isAssignableFrom(cls);
	}

	private boolean isPrimitive(Class<?> cls) {
		return cls.isPrimitive() || cls.equals(String.class);
	}

	public ObjectTree makeTree(Object root) throws IllegalArgumentException,
			IllegalAccessException {
		return recurse(root, null, null);
	}
}
