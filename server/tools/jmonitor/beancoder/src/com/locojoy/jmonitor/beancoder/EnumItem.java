package com.locojoy.jmonitor.beancoder;

import org.w3c.dom.Element;

public class EnumItem {

	private String		name = "";
	private String		value = "";
	
	public EnumItem() {}
	
	public EnumItem( Element ele) {
		name = ele.getAttribute( "name");
		value = ele.getAttribute( "value");
	}
	
	public final String getName() {
		return name;
	}

	public final int getValue( int defaultvalue) {
		if( value.isEmpty())
			return defaultvalue;
		else
			return Integer.parseInt( value);
	}

}
