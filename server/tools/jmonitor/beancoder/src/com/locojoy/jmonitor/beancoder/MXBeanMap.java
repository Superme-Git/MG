package com.locojoy.jmonitor.beancoder;

import java.io.PrintStream;

import org.w3c.dom.Element;

public class MXBeanMap implements MXBeanType, MXBeanTypeManager.MXBeanTypeCreator {

	private 	String 	keytype = "";
	private 	String 	valuetype = "";
	
	@Override
	public boolean initializeByXMLElement(Element ele) {
		keytype = ele.getAttribute( "key");
		valuetype = ele.getAttribute( "value");
		return !keytype.isEmpty() && !valuetype.isEmpty();
	}

	@Override
	public MXBeanType newInstance() {
		return new MXBeanMap();
	}

	@Override
	public void outInterfaceMethodCodes(String namespace, PrintStream ps) {
		ps.print( "java.util.HashMap<" + namespace + ".bean." + keytype + ", " + namespace + ".bean." + valuetype + ">");		
	}

	@Override
	public void outInterfaceRawTypeCodes(String namespace, PrintStream ps) {
		ps.print( "java.util.Map<" + namespace + ".bean." + keytype + ", " + namespace + ".bean." + valuetype + ">");		
	}

	@Override
	public String getValueBeanDataType() {
		return valuetype;
	}

	@Override
	public String getKeyBeanDataType() {
		return keytype;
	}

}
