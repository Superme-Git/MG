package com.locojoy.jmonitor.beancoder;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Coder {

	private String 				namespace;
	private BeanData				rolebase = null;
	private MXBeanData			rolecount = null;
	private final Map<String,BeanData> 		beandatas = new HashMap<String,BeanData>();
	private final Collection<MXBeanData>	mxbeans = new LinkedList<MXBeanData>(); 
	
	final public String getNamespace() {
		return namespace;
	}

	final public BeanData getRolebase() {
		return rolebase;
	}

	final public MXBeanData getRoleCount() {
		return rolecount;
	}
	
	final public Map<String, BeanData> getBeandatas() {
		return beandatas;
	}

	final public Collection<MXBeanData> getMxbeans() {
		return mxbeans;
	}

	public Coder( String xmlfile) throws SAXException, IOException, ParserConfigurationException {

		System.out.println( "parse " + xmlfile);
		
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( xmlfile);
		Element 	root = doc.getDocumentElement();
		
		namespace = root.getAttribute( "domain");
		System.out.println( "domain = " + namespace);
		
		NodeList nl = root.getChildNodes();
		int count = nl.getLength();
		for( int i = 0; i < count; i ++) {
			Node node = nl.item( i);
			if( Node.ELEMENT_NODE != node.getNodeType())
				continue;
			if( 0 == node.getNodeName().compareToIgnoreCase( "bean"))
				parseElementBean( (Element)node);
			else if( 0 == node.getNodeName().compareToIgnoreCase( "mxbean"))
				parseElementMXBean( (Element)node);
		}
	}
	
	private final void parseElementBean( Element ele) {
		BeanData	data = new BeanData();
		data.initializeFromElement( ele);
		if( data.isRolebase())
			rolebase = data;
		beandatas.put( data.getName(), data);
	}

	private final void parseElementMXBean( Element ele) {
		MXBeanData mxbd = new MXBeanData( ele);
		if( mxbd.isRoleCount())
			rolecount = mxbd;
		mxbeans.add( mxbd);
	}

	private final void outCodes( String path) {
	
		path = path + "/" + namespace.replace( '.', '/');
		new File( path).mkdirs();
		new File( path + "/impl").mkdirs();
		new File( path + "/bean").mkdirs();
		new File( path + "/jmx").mkdirs();
		
		try {
			new OutRoleBaseCodes( this, path).outCodes();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	
		for( final Map.Entry<String,BeanData> e : beandatas.entrySet()) {
			try {
				e.getValue().outCodes( path, namespace);
			} catch (IOException e1) {
				System.out.println( "BeanData.outCodes " + e.getKey());
				e1.printStackTrace();
			}
		}
		
		for( final MXBeanData mxb : mxbeans) {
			try {
				mxb.outCodes( path, this);
			} catch (IOException e1) {
				System.out.println( "MXBeanData.outCodes " + mxb.getClassName());
				e1.printStackTrace();
			}
		}
		
		try {
			new OutManagerCodes( this, path).outCodes();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	
	}
	
	public static void main( String args[]) throws Exception {
		
		MXBeanTypeManager.getInstance().registMXBeanType( "map", new MXBeanMap());
		
		new Coder( args[0]).outCodes( args[1]);
	}
}
