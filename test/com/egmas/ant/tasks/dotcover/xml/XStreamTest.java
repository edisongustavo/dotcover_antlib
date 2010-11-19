package com.egmas.ant.tasks.dotcover.xml;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class XStreamTest {

	private static class Tree {
		public String stringField;
	}

	private static class Subclass {
		public String subStringField;
	}

	private static class TreeWithSubclassInside {
		public Subclass subclassField;
	}

	private static class TreeWithCollection {
		List<String> names = new ArrayList<String>();
	}

	private XStream stream = new XStream();

	@Test
	public void testToXML() {
		Tree tree = new Tree();
		tree.stringField = "Hello";

		String expectedXml = "" + //
				"<Tree>" + //
				"	<stringField>Hello</stringField>" + //
				"</Tree>";
		compare(expectedXml, stream.toXML(tree));
	}

	@Test
	public void testToXMLWithSubclassInside() {
		TreeWithSubclassInside tree = new TreeWithSubclassInside();

		Subclass subclassField = new Subclass();
		subclassField.subStringField = "Hello";
		tree.subclassField = subclassField;

		String expectedXml = "" + //
				"<TreeWithSubclassInside>" + //
				"	<subclassField>" + //
				"		<subStringField>Hello</subStringField>" + //
				"	</subclassField>" + //
				"</TreeWithSubclassInside>";
		compare(expectedXml, stream.toXML(tree));
	}

	@Test
	public void testToXMLWithCollection() {
		TreeWithCollection tree = new TreeWithCollection();

		tree.names.add("Mark");
		tree.names.add("Harris");

		String expectedXml = "" + //
				"<TreeWithCollection>" + //
				"	<names>" + //
				"		<String>Mark</String>" + //
				"		<String>Harris</String>" + //
				"	</names>" + //
				"</TreeWithCollection>";
		compare(expectedXml, stream.toXML(tree));
	}

	private void compare(String expectedXml, String xml) {
		xml = xml.replaceAll("\n", "");
		Assert.assertEquals(expectedXml, xml);
	}

	@Test
	public void testAliasOnCollections() {
		TreeWithCollection tree = new TreeWithCollection();

		tree.names.add("Mark");
		tree.names.add("Harris");

		String expectedXml = "" + //
				"<TreeWithCollection>" + //
				"	<names>" + //
				"		<string>Mark</string>" + //
				"		<string>Harris</string>" + //
				"	</names>" + //
				"</TreeWithCollection>";
		stream.alias("string", String.class);
		compare(expectedXml, stream.toXML(tree));
	}

	@Test
	public void testAddImplicitCollection() {
		TreeWithCollection tree = new TreeWithCollection();

		tree.names.add("Mark");
		tree.names.add("Harris");

		String expectedXml = "" + //
				"<TreeWithCollection>" + //
				"	<String>Mark</String>" + //
				"	<String>Harris</String>" + //
				"</TreeWithCollection>";

		stream.addImplicitCollection(TreeWithCollection.class, "names");
		compare(expectedXml, stream.toXML(tree));
	}

}
