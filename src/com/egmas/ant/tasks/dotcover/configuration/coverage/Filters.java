package com.egmas.ant.tasks.dotcover.configuration.coverage;

import com.egmas.ant.tasks.dotcover.configuration.Aliasable;
import com.egmas.ant.tasks.dotcover.xml.XStream;

public class Filters implements Aliasable {
	private IncludeFilters IncludeFilters;
	private ExcludeFilters ExcludeFilters;

	public void setIncludeFilters(IncludeFilters includeFilters) {
		this.IncludeFilters = includeFilters;
	}

	@Override
	public void alias(XStream xstream) {
		xstream.alias("Filters", Filters.class);
		IncludeFilters.alias(xstream);
		ExcludeFilters.alias(xstream);
	}

	public void setExcludeFilters(ExcludeFilters excludeFilters) {
		this.ExcludeFilters = excludeFilters;
	}
}
