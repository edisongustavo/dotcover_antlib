package com.egmas.ant.tasks.dotcover.configuration.merge;

import java.util.ArrayList;
import java.util.List;

import com.egmas.ant.tasks.dotcover.configuration.Aliasable;
import com.egmas.ant.tasks.dotcover.xml.XStream;

public class Source implements Aliasable {

	private List<String> sources = new ArrayList<String>();

	public Source() {
	}

	public Source(String sourceEntry) {
		sources.add(sourceEntry);
	}

	@Override
	public void alias(XStream xstream) {
		xstream.alias("Source", Source.class);
		xstream.addImplicitCollection(Source.class, "sources");
	}

	public void addStringEntry(String entry) {
		sources.add(entry);
	}
}
