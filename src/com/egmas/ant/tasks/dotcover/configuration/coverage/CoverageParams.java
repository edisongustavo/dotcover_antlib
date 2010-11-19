package com.egmas.ant.tasks.dotcover.configuration.coverage;

import com.egmas.ant.tasks.dotcover.configuration.Aliasable;
import com.egmas.ant.tasks.dotcover.xml.XStream;

public class CoverageParams implements Aliasable {

	/**
	 * Path to the nunit
	 */
	private String Executable;

	/**
	 * All the assemblies separated by space
	 */
	private String Arguments;

	private String WorkingDir;

	private String TempDir;

	private String Output;

	private Filters Filters;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.egmas.ant.tasks.dotcover.configuration.Aliasable#aliasXml(com.
	 * thoughtworks.xstream.XStream)
	 */
	@Override
	public void alias(XStream xstream) {
		xstream.alias("CoverageParams", CoverageParams.class);
		Filters.alias(xstream);
	}

	public void setExecutable(String executable) {
		this.Executable = executable;
	}

	public void setArguments(String arguments) {
		this.Arguments = arguments;
	}

	public void setWorkingDir(String workingDir) {
		this.WorkingDir = workingDir;
	}

	public void setTempDir(String tempDir) {
		this.TempDir = tempDir;
	}

	public void setOutput(String output) {
		this.Output = output;
	}

	public void setFilters(Filters filters) {
		this.Filters = filters;
	}
}
