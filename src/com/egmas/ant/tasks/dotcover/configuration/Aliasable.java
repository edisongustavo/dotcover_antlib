package com.egmas.ant.tasks.dotcover.configuration;

import com.egmas.ant.tasks.dotcover.xml.XStream;

public interface Aliasable {

	/**
	 * Provides the alias so that XStream can serialize the class correctly
	 * 
	 * @param xstream
	 */
	public abstract void alias(XStream xstream);

}