package com.egmas.ant.tasks.dotcover.configuration.coverage;

import java.util.ArrayList;
import java.util.List;

import com.egmas.ant.tasks.dotcover.configuration.Aliasable;
import com.egmas.ant.tasks.dotcover.xml.XStream;

public class ExcludeFilters implements Aliasable {

	@Override
	public void alias(XStream xstream) {
		xstream.alias("ExcludeFilters", ExcludeFilters.class);
		xstream.addImplicitCollection(ExcludeFilters.class, "entries");
	}

	public void addFilterEntry(FilterEntry filterEntry) {
		entries.add(filterEntry);
	}

	private List<FilterEntry> entries = new ArrayList<FilterEntry>();
}
