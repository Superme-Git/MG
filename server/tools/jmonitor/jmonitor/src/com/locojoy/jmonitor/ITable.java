package com.locojoy.jmonitor;

import java.util.List;

public interface ITable {

	public String getIdentification();

	public String getXmlDescription();

	public List<String> getKeyList(); //包含ZID, TIME

	public List<String> getValueList();
	
	public boolean isWorking();
	
}