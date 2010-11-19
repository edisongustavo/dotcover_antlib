package com.egmas.ant.tasks.dotcover.configuration.coverage;

import com.egmas.ant.tasks.dotcover.configuration.Aliasable;
import com.egmas.ant.tasks.dotcover.configuration.ContentElement;
import com.egmas.ant.tasks.dotcover.xml.XStream;

public class FilterEntry implements Aliasable {
	private String ModuleMask;
	private String ClassMask;
	private String FunctionMask;

	public void addConfiguredModuleMask(ContentElement element) {
		ModuleMask = element.getText();
	}

	public void addConfiguredFunctionMask(ContentElement element) {
		FunctionMask = element.getText();
	}

	@Override
	public void alias(XStream xstream) {
		xstream.alias("FilterEntry", FilterEntry.class);
	}

	public void addConfiguredClassMask(ContentElement element) {
		ClassMask = element.getText();
	}
}
