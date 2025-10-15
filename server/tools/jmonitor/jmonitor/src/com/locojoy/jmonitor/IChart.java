package com.locojoy.jmonitor;

import java.util.Set;

public interface IChart {
	
	public String getChartName();
	
	public Set<String> getContrainableKeys();
	
	public String getXmlDescription() ;
	
	
}
