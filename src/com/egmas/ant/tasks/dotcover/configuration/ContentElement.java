package com.egmas.ant.tasks.dotcover.configuration;

/**
 * @author emuenz
 * 
 */
public class ContentElement {
	private String contents = "";

	public ContentElement() {
	}

	public ContentElement(String string) {
		this.contents = string;
	}

	public void addText(String contents) {
		this.contents += contents;
	}

	public String getText() {
		return contents;
	}
}
