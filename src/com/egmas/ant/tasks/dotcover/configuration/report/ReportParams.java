package com.egmas.ant.tasks.dotcover.configuration.report;

import com.egmas.ant.tasks.dotcover.configuration.Aliasable;
import com.egmas.ant.tasks.dotcover.xml.XStream;

public class ReportParams implements Aliasable {
	private String Source;
	private String Output;

	public void setSource(String source) {
		Source = source;
	}

	public void setOutput(String output) {
		Output = output;
	}

	@Override
	public void alias(XStream xstream) {
		xstream.alias("ReportParams", ReportParams.class);
	}
}
