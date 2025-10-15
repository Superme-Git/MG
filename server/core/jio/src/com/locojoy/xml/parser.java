
package com.locojoy.xml;

import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class parser extends org.xml.sax.helpers.DefaultHandler
{
	private Stack<xmlobject> stack = new Stack<xmlobject>();
	private StringBuffer sbcp;

	public parser(String base)
	{
		sbcp = new StringBuffer(base);
	}

	public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes)
	{
		try
		{
			if ( sbcp.length() > 0 )
				sbcp.append(".");
			sbcp.append(qName);
			xmlobject obj = (xmlobject)Class.forName(sbcp + ".handler").newInstance();
			if ( stack.empty() )
			{
				obj.setparent(null);
			}
			else
			{
				xmlobject parent = (xmlobject)stack.peek();
				obj.setparent(parent);
				parent.setchild(obj);
			}
			stack.push(obj);
			obj.setattr(attributes);
		}	
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void endElement(String uri, String localName, String qName)
	{
		xmlobject obj = (xmlobject)stack.pop();
		if ( stack.empty() )
			obj.action();
		else
			sbcp.delete(sbcp.lastIndexOf("."), sbcp.length());
	}

	public void characters(char[] ch, int start, int length)
	{
		xmlobject parent = (xmlobject)stack.peek();
		parent.content += new String(ch, start, length);
	}

	public static void parse(java.io.InputStream is, String base)
	{
		try
		{
			SAXParser sap = SAXParserFactory.newInstance().newSAXParser();
			sap.parse(is, new parser(base));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void parse(java.io.InputStream is)
	{
		parse(is, "");
	}
}
