package com.egmas.ant.tasks.dotcover.configuration.merge;

import com.egmas.ant.tasks.dotcover.configuration.Aliasable;
import com.egmas.ant.tasks.dotcover.xml.XStream;

public class MergeParams implements Aliasable {

	private Source Source;

	private String TempDir;
	private String Output;

	@Override
	public void alias(XStream xstream) {
		xstream.alias("MergeParams", MergeParams.class);
		Source.alias(xstream);
	}

	public void setSource(Source source) {
		Source = source;
	}

	public void setTempDir(String tempDir) {
		TempDir = tempDir;
	}

	public void setOutput(String output) {
		Output = output;
	}
}
