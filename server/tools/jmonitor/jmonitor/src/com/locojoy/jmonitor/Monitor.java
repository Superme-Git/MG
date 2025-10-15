package com.locojoy.jmonitor;

import com.locojoy.jmonitor.spi.ManagerImpl;


public class Monitor  {

	static private Manager manager = new ManagerImpl();
	static public Manager getManager(){
		return manager;
	}

}
