package com.locojoy.jmonitor.beancoder;

import java.util.Collection;
import java.util.LinkedList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BeanVariable {
	private String 	name = "";
	private String 	type = "";
	private boolean	valueabs = false;
	
	final private Collection<EnumItem>	EnumItems = new LinkedList<EnumItem>();
	
	public BeanVariable() {}
		
	public BeanVariable( Element ele) {
		name = ele.getAttribute( "name");
		type = ele.getAttribute( "type");
		valueabs = getBooleanValueByString( ele.getAttribute( "abs"));
		
		NodeList nl = ele.getChildNodes();
		int count = nl.getLength();
		for( int i = 0; i < count; i ++) {
			Node node = nl.item( i);
			if( Node.ELEMENT_NODE != node.getNodeType())
				continue;
			if( 0 == node.getNodeName().compareToIgnoreCase( "enum"))
				EnumItems.add( new EnumItem( (Element)node));
		}
	}
	
	public final String getName() {
		return name;
	}

	public final String getFistCharUpcaseName() {
		return upcaseNameFistChar( name);
	}
	public final String getType() {
		return type;
	}
	
	public final boolean isAbs() {
		return valueabs;
	}
	
	public final boolean isEnum() {
		return !EnumItems.isEmpty();
	}
	
	public final Collection<EnumItem> getEnumItems() {
		return EnumItems;
	}
	
	public static String upcaseNameFistChar( String str) {
		if( str.isEmpty())
			return str;
		
		if( Character.isUpperCase( str.charAt( 0)))
			return str;
		
		StringBuffer	sb = new StringBuffer();
		sb.append( Character.toUpperCase( str.charAt( 0)));
		sb.append( str.substring( 1));
		return sb.toString();
	}
	public static boolean getBooleanValueByString( String value) {
		if( value.isEmpty())
			return false;
		else if( 0 == value.compareToIgnoreCase( "true"))
			return true;
		else if( 0 == value.compareTo( "1"))
			return true;
		else
			return false;
	}

}
