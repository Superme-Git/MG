package com.locojoy.jmonitor.beancoder;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

public class MXBeanTypeManager {

	static private final MXBeanTypeManager instance = new MXBeanTypeManager();
	static public final MXBeanTypeManager getInstance() {
		return instance;
	}
	
	static public interface MXBeanTypeCreator {
		MXBeanType newInstance();
	}

	private final Map<String,MXBeanTypeCreator> creators = new HashMap<String,MXBeanTypeCreator>();
	
	void registMXBeanType( String type, MXBeanTypeCreator creator) {
		creators.put( type, creator);
	}
	
	MXBeanType createMXBeanTypeByElement( Element ele) {
		String type = ele.getAttribute( "type");
		MXBeanTypeCreator	creator = creators.get( type);
		if( null == creator)
			return null;
		MXBeanType	mxbean = creator.newInstance();
		if( mxbean.initializeByXMLElement( ele))
			return mxbean;
		else
			return null;
	}
}
