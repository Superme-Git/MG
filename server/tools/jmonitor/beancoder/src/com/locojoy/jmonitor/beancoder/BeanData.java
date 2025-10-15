package com.locojoy.jmonitor.beancoder;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BeanData {
	
	final private Collection<BeanVariable> variables = new LinkedList<BeanVariable>();
	private String 	beanname = "";
	private String 	extendclass = "";
	private boolean	rolebase = false;
	
	public BeanData() {}
	
	public final Collection<BeanVariable> getVariables() {
		return variables;
	}
	
	public final String getName() {
		return beanname;
	}
	
	public final boolean isRolebase() {
		return rolebase;
	}
	
	public final String getExtendClass() {
		return extendclass;
	}
	
	public final void initializeFromElement( Element ele) {
		beanname = ele.getAttribute( "name");
		rolebase = BeanVariable.getBooleanValueByString( ele.getAttribute( "rolebase"));
		extendclass = ele.getAttribute( "extends");

		NodeList nl = ele.getChildNodes();
		int count = nl.getLength();
		for( int i = 0; i < count; i ++) {
			Node node = nl.item( i);
			if( Node.ELEMENT_NODE != node.getNodeType())
				continue;
			if( 0 == node.getNodeName().compareToIgnoreCase( "variable"))
				variables.add( new BeanVariable( (Element)node));
		}
	}
	
	public final void outArgs( PrintStream ps) {
		boolean first = true;
		for( final BeanVariable bd : variables) {
			if( first)
				first = false;
			else
				ps.print( ", ");
			ps.print( bd.getType() + " " + bd.getName());
		}
	}
	public final void outVars( PrintStream ps) {
		boolean first = true;
		for( final BeanVariable bd : variables) {
			if( first)
				first = false;
			else
				ps.print( ", ");
			ps.print( bd.getName());
		}
	}

	private final void outHashCode( PrintStream ps) {
		boolean first = true;
		for( final BeanVariable bd : variables) {
			if( first)
				first = false;
			else
				ps.print( " ^ ");
			
			if( 0 == bd.getType().compareToIgnoreCase( "int"))
				ps.print( bd.getName());
			else if( 0 == bd.getType().compareToIgnoreCase( "long"))
				ps.print( "(int)(" + bd.getName() + " ^ (" + bd.getName() + " >>> 32 ))");
			else
				throw new RuntimeException( "unsupport type " + bd.getType());
		}
	}
	
	private final void outEquals( PrintStream ps) {
		boolean first = true;
		for( final BeanVariable bd : variables) {
			if( first)
				first = false;
			else
				ps.print( " && ");
			
			ps.print( "( " + bd.getName() + " == dst." + bd.getName() + ")");
		}
	}
	
	public final void outCodes( final String path, final String namespace) throws IOException {
		String filename = path + "/bean/" + beanname + ".java";
		
		PrintStream ps = new PrintStream( new BufferedOutputStream( new FileOutputStream( filename)));
		ps.println( "package " + namespace + ".bean;");
		ps.println();
		
		if( extendclass.isEmpty())
			ps.println( "public class " + beanname + " {");
		else
			ps.println( "public class " + beanname + " extends " + extendclass + " {");
		
		ps.println();
		
		for( final BeanVariable bd : variables)
			ps.println( "\tpublic " + bd.getType() + " " + bd.getName() + ";");

		ps.println();
		
		ps.println( "\tpublic " + beanname + "() {");
		for( final BeanVariable bd : variables)
			ps.println( "\t\t" + bd.getName() + " = 0;");
		ps.println( "\t}");
		ps.println();

		ps.println( "\tpublic " + beanname + "( " + beanname + " _src_) {");
		if( !extendclass.isEmpty())
			ps.println( "\t\tsuper( _src_);");
		for( final BeanVariable bd : variables)
			ps.println( "\t\t" + bd.getName() + " = _src_." + bd.getName() + ";");
		ps.println( "\t}");
		ps.println();

		if( extendclass.isEmpty()) {

			ps.print( "\tpublic " + beanname + "( ");
			outArgs( ps);
			ps.println( ") {");

			for( final BeanVariable bd : variables)
				ps.println( "\t\tthis." + bd.getName() + " = " + bd.getName() + ";");
			
			ps.println( "\t}");
		} else {

			ps.print( "\tpublic " + beanname + "( " + extendclass + " extendbean, ");
			outArgs( ps);
			ps.println( ") {");
			ps.println( "\t\tsuper( extendbean);");

			for( final BeanVariable bd : variables)
				ps.println( "\t\tthis." + bd.getName() + " = " + bd.getName() + ";");
			
			ps.println( "\t}");
		}
		
		ps.println();
		for( final BeanVariable bd : variables) {
			ps.println( "\tpublic " + bd.getType() + " get" + bd.getFistCharUpcaseName() + "() {");
			ps.println( "\t\treturn " + bd.getName() + ";");
			ps.println( "\t}");
			ps.println();
		}
		
		ps.println( "\t@Override");
		ps.println( "\tpublic int hashCode() {");
		ps.print( "\t\treturn ");
		if( !extendclass.isEmpty())
			ps.print(  "super.hashCode() ^ ");
		outHashCode( ps);
		ps.println( ";");
		ps.println( "\t}");
		ps.println();
		
		ps.println( "\t@Override");
		ps.println( "\tpublic boolean equals(Object obj) {");
		if( !extendclass.isEmpty()) {
			ps.println(  "\t\tif( !super.equals(obj))");
			ps.println(  "\t\t\treturn false;");
		}
		ps.println( "\t\t" + beanname + " dst = (" + beanname + ")obj;");
		ps.print( "\t\treturn ");
		outEquals( ps);
		ps.println( ";");
		ps.println( "\t}");
		ps.println();
		
		ps.println( "}");
		ps.println();
		
		ps.close();
	}
}
