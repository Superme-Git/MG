package com.locojoy.jmonitor;

import java.util.Map;

public interface IServer {
	
	public int getId();

	public String getUrl(); 

	public String getDescription();

	public Map<String, String> getExtra();
	
	public boolean isOpened();
}