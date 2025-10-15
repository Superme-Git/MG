package com.locojoy.jmonitor.beancoder;

import java.io.PrintStream;
import org.w3c.dom.Element;

public interface MXBeanType {

	boolean initializeByXMLElement( Element ele);
	void outInterfaceMethodCodes( String namespace, PrintStream ps);
	void outInterfaceRawTypeCodes( String namespace, PrintStream ps);
	String getKeyBeanDataType();
	String getValueBeanDataType();
}
