package com.mammoth.logger.gui;

import java.util.*;
public class TreeEntity {
	protected String name;
	protected List children;
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setChildren(List children){
		this.children = children;
	}
	
	public List getChildren(){
		return children;
	}

}
