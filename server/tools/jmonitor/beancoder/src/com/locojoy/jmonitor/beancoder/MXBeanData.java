package com.locojoy.jmonitor.beancoder;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import org.w3c.dom.Element;

public class MXBeanData {

	private String 		classname;
	private MXBeanType	mxbean;
	private boolean 		rolecount;
	private boolean 		absvalue;
	
	MXBeanData( Element ele) {
		classname = ele.getAttribute( "name");
		mxbean = MXBeanTypeManager.getInstance().createMXBeanTypeByElement( ele);
		rolecount = BeanVariable.getBooleanValueByString( ele.getAttribute( "rolecount"));
		absvalue = BeanVariable.getBooleanValueByString( ele.getAttribute( "abs"));
	}
	
	public final String getClassName() {
		return classname;
	}

	public final boolean isRoleCount() {
		return rolecount;
	}
	
	public final MXBeanType getMXBeanType() {
		return mxbean;
	}
	
	public final void outMXBeanInterfaceCodes( final String path, final String namespace) throws IOException {
		String filename = path + "/jmx/" + classname + "MXBean.java";

		PrintStream ps = new PrintStream( new BufferedOutputStream( new FileOutputStream( filename)));
		ps.println( "package " + namespace + ".jmx;");
		ps.println();

		ps.println( "public interface " + classname + "MXBean {");
		ps.println();
		
		ps.print( "\t");
		mxbean.outInterfaceRawTypeCodes( namespace, ps);
		ps.println( " get" + BeanVariable.upcaseNameFistChar( classname) + "();");
		ps.println();
		
		ps.println( "}");
		ps.println();
		
		ps.close();
	}
	
	public final void outMXBeanClassCodes( final String path, final Coder coder) throws IOException {
		
		final String namespace = coder.getNamespace();
		final Map<String,BeanData> beandatas = coder.getBeandatas();
		final String rolebasename = coder.getRolebase().getName();
		
		String filename = path + "/jmx/" + classname + ".java";
		
		PrintStream ps = new PrintStream( new BufferedOutputStream( new FileOutputStream( filename)));
		ps.println( "package " + namespace + ".jmx;");
		ps.println();
		
		ps.println( "public class " + classname + " implements " + classname + "MXBean {");
		ps.println();
		
		ps.print( "\tprivate ");
		mxbean.outInterfaceMethodCodes( namespace, ps);
		ps.print( " map = new ");
		mxbean.outInterfaceMethodCodes( namespace, ps);
		ps.println( "();");
		ps.println();
	

		{
			ps.println( "\tprivate static class GetTask extends com.locojoy.jmonitor.counter.GetTask {");
			ps.print( "\t\tprivate ");
			mxbean.outInterfaceMethodCodes( namespace, ps);
			ps.println( " result = null;");
			
			ps.println( "\t\t@Override");
			ps.println( "\t\tpublic void doGetTask() {");
			if( absvalue) {
				ps.println( "\t\t\t@SuppressWarnings(\"unchecked\")");
				
				ps.print( "\t\t\tfinal ");
				mxbean.outInterfaceMethodCodes( namespace, ps);
				ps.print( " temp = (");
				mxbean.outInterfaceMethodCodes( namespace, ps);
				ps.println( ")"+ classname + ".getInstance().map.clone();");
				
				ps.println( "\t\t\tresult = temp;");
			} else {
				ps.println( "\t\t\tresult = " + classname + ".getInstance().map;");
				ps.print( "\t\t\t"+ classname + ".getInstance().map = new ");
				mxbean.outInterfaceMethodCodes( namespace, ps);
				ps.println( "();");
			}
			ps.println( "\t\t}");

			ps.print( "\t\tpublic ");
			mxbean.outInterfaceMethodCodes( namespace, ps);
			ps.println( " getResult() {");
			ps.println( "\t\t\treturn result;");
			ps.println( "\t\t}");
			
			ps.println( "\t}");
			ps.println();
		}
		
		ps.println( "\t@Override");
		ps.print( "\tpublic ");
		mxbean.outInterfaceRawTypeCodes( namespace, ps);
		ps.println( " get" + BeanVariable.upcaseNameFistChar( classname) + "() {");
		ps.println( "\t\tGetTask task = new GetTask();");
		ps.println( "\t\ttask.runTask();");
		ps.println( "\t\treturn task.getResult();");
		ps.println( "\t}");
		ps.println();
	
		{
			final String keytypename = namespace + ".bean." + mxbean.getKeyBeanDataType();
			final String valuetypename = namespace + ".bean." + mxbean.getValueBeanDataType();
			ps.println( "\tfinal private " + valuetypename + " get( " + keytypename + " key) {");
			ps.println( "\t\t" + valuetypename + " result = map.get( key);");
			ps.println( "\t\tif( null == result) {");
			ps.println( "\t\t\tresult = new " + valuetypename + "();");
			ps.println( "\t\t\tmap.put( key, result);");
			ps.println( "\t\t}");
			ps.println( "\t\treturn result;");
			ps.println( "\t}");
			ps.println();
		}
		
		{
			final String keytypename = namespace + ".bean." + mxbean.getKeyBeanDataType();
			final String valuetypename = namespace + ".bean." + mxbean.getValueBeanDataType();
			final BeanData keybeandata = beandatas.get( mxbean.getKeyBeanDataType());
			final BeanData valuebeandata = beandatas.get( mxbean.getValueBeanDataType());
			
			for( final BeanVariable bv : valuebeandata.getVariables()) {

				ps.print( "\tpublic void setVariable_" + bv.getName() + "( ");
				if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
					ps.print( "long roleid, ");
					keybeandata.outArgs( ps);
				} else if( 0 == keybeandata.getName().compareToIgnoreCase( rolebasename)) {
					ps.print( "long roleid");
				} else if( keybeandata.getExtendClass().isEmpty()) {
					keybeandata.outArgs( ps);
				} else {
					throw new RuntimeException( keybeandata.getName() + " unknow extend " + keybeandata.getExtendClass());
				}
				ps.println( ", " + bv.getType() + " _value_) {");
				
				ps.print( "\t\t" + keytypename + " beankey = ");

				if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
					ps.print( "new " + keytypename + "( " + namespace + ".impl.RoleLoginLogout.getInstance().get( roleid), ");
					keybeandata.outVars( ps);
					ps.println( ");");
				} else if( 0 == keybeandata.getName().compareToIgnoreCase( rolebasename)) {
					ps.println( namespace + ".impl.RoleLoginLogout.getInstance().get( roleid);");
				} else if( keybeandata.getExtendClass().isEmpty()) {
					ps.print( "new " + keytypename + "( ");
					keybeandata.outVars( ps);
					ps.println( ");");
				} else {
					throw new RuntimeException( keybeandata.getName() + " unknow extend " + keybeandata.getExtendClass());
				}

				ps.println( "\t\t" + valuetypename + " beanvalue = get( beankey);");
				
				if( bv.isAbs())
					ps.println( "\t\tbeanvalue." + bv.getName() + " = _value_;");
				else
					ps.println( "\t\tbeanvalue." + bv.getName() + " += _value_;");

				ps.println( "\t}");
				ps.println();
			}
		}
		
		if( rolecount) {
			
			final String keytypename = namespace + ".bean." + mxbean.getKeyBeanDataType();
			final String valuetypename = namespace + ".bean." + mxbean.getValueBeanDataType();
			final BeanData valuebeandata = beandatas.get( mxbean.getValueBeanDataType());
			final BeanVariable variable = valuebeandata.getVariables().iterator().next();
			
			ps.println( "\tpublic void setRoleCountValue( " + keytypename + " beankey, " + variable.getType() + " _value_) {");
			ps.println( "\t\t" + valuetypename + " beanvalue = get( beankey);");
			ps.println( "\t\tbeanvalue." + variable.getName() + " += _value_;");
			ps.println( "\t}");
			ps.println();
		}
		
		ps.println( "\tprivate " + classname + "() {");
		ps.println( "\t}");

		ps.println( "\tstatic final private " + classname + " instance = new " + classname + "();");
		ps.println( "\tstatic public " + classname + " getInstance() {");
		ps.println( "\t\treturn instance;");
		ps.println( "\t}");

		ps.println( "}");
		ps.println();
	
		ps.close();
	}
	
	public final void outGameInterfaceCodes( final String path, final Coder coder) throws IOException {

		final String 	namespace = coder.getNamespace();
		final String filename = path + "/" + classname + ".java";

		PrintStream ps = new PrintStream( new BufferedOutputStream( new FileOutputStream( filename)));
		ps.println( "package " + namespace + ";");
		ps.println();

		ps.println( "public abstract class " + classname + " {");
		ps.println();
		
		final Map<String,BeanData> beandatas = coder.getBeandatas();
		final String rolebasename = coder.getRolebase().getName();
		
		final BeanData keybeandata = beandatas.get( mxbean.getKeyBeanDataType());
		final BeanData valuebeandata = beandatas.get( mxbean.getValueBeanDataType());
		
		for( final BeanVariable bv : valuebeandata.getVariables()) {

			ps.print( "\tprotected abstract void raw_set" + bv.getName() + "Value( ");
			if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
				ps.print( "long roleid, ");
				keybeandata.outArgs( ps);
			} else if( 0 == keybeandata.getName().compareToIgnoreCase( rolebasename)) {
				ps.print( "long roleid");
			} else if( keybeandata.getExtendClass().isEmpty()) {
				keybeandata.outArgs( ps);
			} else {
				throw new RuntimeException( keybeandata.getName() + " unknow extend " + keybeandata.getExtendClass());
			}
			ps.println( ", " + bv.getType() + " _value_);");
		}
		ps.println();

		for( final BeanVariable bv : valuebeandata.getVariables()) {

			ps.println( "\tprivate class Set" + bv.getName() + "ValueTask extends xdb.Procedure.Task { ");
			ps.println();
			if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
				ps.println( "\t\tprivate long roleid;");
				for( BeanVariable kbv : keybeandata.getVariables())
					ps.println( "\t\tprivate " + kbv.getType() + " " + kbv.getName() + ";");
			} else if( 0 == keybeandata.getName().compareToIgnoreCase( rolebasename)) {
				ps.println( "\t\tprivate long roleid;");
			} else if( keybeandata.getExtendClass().isEmpty()) {
				for( BeanVariable kbv : keybeandata.getVariables())
					ps.println( "\t\tprivate " + kbv.getType() + " " + kbv.getName() + ";");
			} else {
				throw new RuntimeException( keybeandata.getName() + " unknow extend " + keybeandata.getExtendClass());
			}
			ps.println( "\t\tprivate " + bv.getType() + " _value_;");
			ps.println();

			{
				ps.print( "\t\tpublic Set" + bv.getName() + "ValueTask( ");
				if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
					ps.print( "long roleid, ");
					keybeandata.outArgs( ps);
				} else if( 0 == keybeandata.getName().compareToIgnoreCase( rolebasename)) {
					ps.print( "long roleid");
				} else if( keybeandata.getExtendClass().isEmpty()) {
					keybeandata.outArgs( ps);
				} else {
					throw new RuntimeException( keybeandata.getName() + " unknow extend " + keybeandata.getExtendClass());
				}

				ps.println( ", " + bv.getType() + " _value_) {");
				
				if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
					ps.println( "\t\t\tthis.roleid = roleid;");
					for( BeanVariable kbv : keybeandata.getVariables())
						ps.println( "\t\t\tthis." + kbv.getName() + " = " + kbv.getName() + ";");
				} else if( 0 == keybeandata.getName().compareToIgnoreCase( rolebasename)) {
					ps.println( "\t\t\tthis.roleid = roleid;");
				} else if( keybeandata.getExtendClass().isEmpty()) {
					for( BeanVariable kbv : keybeandata.getVariables())
						ps.println( "\t\t\tthis." + kbv.getName() + " = " + kbv.getName() + ";");
				} else {
					throw new RuntimeException( keybeandata.getName() + " unknow extend " + keybeandata.getExtendClass());
				}
				ps.println( "\t\t\tthis._value_ = _value_;");
				ps.println( "\t\t}");
				
				ps.println();
			}
		
			ps.println("\t\t@Override");
			ps.println("\t\tpublic void run() {");
			ps.print("\t\t\traw_set" + bv.getName() + "Value( ");
			if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
				ps.print( "roleid, ");
				keybeandata.outVars( ps);
			} else if( 0 == keybeandata.getName().compareToIgnoreCase( rolebasename)) {
				ps.print( "roleid");
			} else if( keybeandata.getExtendClass().isEmpty()) {
				keybeandata.outVars( ps);
			} else {
				throw new RuntimeException( keybeandata.getName() + " unknow extend " + keybeandata.getExtendClass());
			}
			ps.println( ", _value_);");
			ps.println( "\t\t}");
			ps.println();

			ps.println( "\t}");
			ps.println();

			ps.print( "\tpublic void set" + bv.getName() + "Value( ");
			if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
				ps.print( "long roleid, ");
				keybeandata.outArgs( ps);
			} else if( 0 == keybeandata.getName().compareToIgnoreCase( rolebasename)) {
				ps.print( "long roleid");
			} else if( keybeandata.getExtendClass().isEmpty()) {
				keybeandata.outArgs( ps);
			} else {
				throw new RuntimeException( keybeandata.getName() + " unknow extend " + keybeandata.getExtendClass());
			}
			ps.println( ", " + bv.getType() + " _value_) {");
			
			ps.println( "\t\tif( null != xdb.Transaction.current())");
			ps.print( "\t\t\txdb.Procedure.ppostWhileCommit( new Set" + bv.getName() + "ValueTask( ");
			if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
				ps.print( "roleid, ");
				keybeandata.outVars( ps);
			} else if( 0 == keybeandata.getName().compareToIgnoreCase( rolebasename)) {
				ps.print( "roleid");
			} else if( keybeandata.getExtendClass().isEmpty()) {
				keybeandata.outVars( ps);
			} else {
				throw new RuntimeException( keybeandata.getName() + " unknow extend " + keybeandata.getExtendClass());
			}
			ps.println( ", _value_));");
			ps.println( "\t\telse");
			ps.print( "\t\t\traw_set" + bv.getName() + "Value( ");
			if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
				ps.print( "roleid, ");
				keybeandata.outVars( ps);
			} else if( 0 == keybeandata.getName().compareToIgnoreCase( rolebasename)) {
				ps.print( "roleid");
			} else if( keybeandata.getExtendClass().isEmpty()) {
				keybeandata.outVars( ps);
			} else {
				throw new RuntimeException( keybeandata.getName() + " unknow extend " + keybeandata.getExtendClass());
			}
			ps.println( ", _value_);");
			
			ps.println( "\t}");
			ps.println();
		}

		boolean hasenum = false;
		for( final BeanVariable bv : keybeandata.getVariables()) {
			if( !bv.isEnum())
				continue;
			int defaultvalue = 0;
			for( EnumItem ei : bv.getEnumItems()) {
				defaultvalue = ei.getValue( defaultvalue);
				ps.println( "\tstatic public final int " + bv.getName() + "_Value_" + ei.getName() + " = " + defaultvalue + ";");
				defaultvalue ++;
			}
			ps.println();
			hasenum = true;
		}
		
		if( hasenum)
			new OutGameEnumCodes( this, coder).outCodes( ps);
		
		ps.println( "}");
		ps.println();
		
		ps.close();
	}
	
	public final void outGameClassCodes( final String path, final Coder coder) throws IOException { 
	
		final String 	namespace = coder.getNamespace();
		final String filename = path + "/impl/" + classname + ".java";

		PrintStream ps = new PrintStream( new BufferedOutputStream( new FileOutputStream( filename)));
		ps.println( "package " + namespace + ".impl;");
		ps.println();

		ps.println( "public class " + classname + " extends " + namespace + "." + classname + " {");
		ps.println();
		
		final Map<String,BeanData> beandatas = coder.getBeandatas();
		final String rolebasename = coder.getRolebase().getName();
		
		final BeanData keybeandata = beandatas.get( mxbean.getKeyBeanDataType());
		final BeanData valuebeandata = beandatas.get( mxbean.getValueBeanDataType());
		
		for( final BeanVariable bv : valuebeandata.getVariables()) {

			final String 	settaskclassname = "Set" + bv.getFistCharUpcaseName() + "Task";
			
			ps.println( "\tstatic private class " + settaskclassname + " extends com.locojoy.jmonitor.counter.SetTask {");
			ps.println();
			
			ps.println( "\t\t" + bv.getType() + " _value_;");
			if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
				
				ps.println( "\t\tlong roleid;");
				for( BeanVariable keybv : keybeandata.getVariables())
					ps.println( "\t\t" + keybv.getType() + " " + keybv.getName() + ";");
				
				ps.println();
				
				ps.print( "\t\tpublic " + settaskclassname + "( long roleid, ");
				keybeandata.outArgs( ps);
				ps.println( ", "+ bv.getType() + " _value_) {");
				ps.println( "\t\t\tthis._value_ = _value_;");
				ps.println( "\t\t\tthis.roleid = roleid;");
				for( BeanVariable keybv : keybeandata.getVariables())
					ps.println( "\t\t\tthis." + keybv.getName() + " = " + keybv.getName() + ";");
				ps.println( "\t\t}");
				
			} else if( 0 == keybeandata.getName().compareToIgnoreCase( rolebasename)) {
				ps.print( "long roleid;");
				ps.println();
				
				ps.println( "\t\tpublic " + settaskclassname + "( long roleid, " + bv.getType() + " _value_) {");
				ps.println( "\t\t\tthis._value_ = _value_;");
				ps.println( "\t\t\tthis.roleid = roleid;");
				ps.println( "\t\t}");
				
			} else if( keybeandata.getExtendClass().isEmpty()) {
				
				for( BeanVariable keybv : keybeandata.getVariables())
					ps.println( "\t\t" + keybv.getType() + " " + keybv.getName() + ";");
				
				ps.println();
				
				ps.print( "\t\tpublic " + settaskclassname + "( ");
				keybeandata.outArgs( ps);
				ps.println( ", " + bv.getType() + " _value_) {");

				ps.println( "\t\t\tthis._value_ = _value_;");
				for( BeanVariable keybv : keybeandata.getVariables())
					ps.println( "\t\t\tthis." + keybv.getName() + " = " + keybv.getName() + ";");
				ps.println( "\t\t}");
			} else {
				throw new RuntimeException( keybeandata.getName() + " unknow extend " + keybeandata.getExtendClass());
			}

			ps.println();
			ps.println( "\t\t@Override");
			ps.println( "\t\tpublic void doRunTask() {");
			ps.print( "\t\t\t" + namespace + ".jmx." + classname + ".getInstance().setVariable_" + bv.getName() + "( ");
		
			if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
				ps.print( "roleid, ");
				keybeandata.outVars( ps);
			} else if( 0 == keybeandata.getName().compareToIgnoreCase( rolebasename)) {
				ps.print( "roleid");
			} else if( keybeandata.getExtendClass().isEmpty()) {
				keybeandata.outVars( ps);
			}
			
			ps.println( ", _value_);");
			ps.println( "\t\t}");
			
			ps.println( "\t};");
			ps.println();

			ps.println( "\t@Override");
			ps.print( "\tprotected void raw_set" + bv.getName() + "Value( ");
			if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
				ps.print( "long roleid, ");
				keybeandata.outArgs( ps);
			} else if( 0 == keybeandata.getName().compareToIgnoreCase( rolebasename)) {
				ps.print( "long roleid");
			} else if( keybeandata.getExtendClass().isEmpty()) {
				keybeandata.outArgs( ps);
			} else {
				throw new RuntimeException( keybeandata.getName() + " unknow extend " + keybeandata.getExtendClass());
			}
			ps.println( ", " + bv.getType() + " _value_) {");
			
			ps.print( "\t\tnew " + settaskclassname + "( ");
			if( 0 == keybeandata.getExtendClass().compareTo( rolebasename)) {
				ps.print( "roleid, ");
				keybeandata.outVars( ps);
			} else if( 0 == keybeandata.getName().compareToIgnoreCase( rolebasename)) {
				ps.print( "roleid");
			} else if( keybeandata.getExtendClass().isEmpty()) {
				keybeandata.outVars( ps);
			}
			ps.println( ", _value_).runTask();");
			
			ps.println( "\t}");
			ps.println();
		}
		ps.println();
	
		ps.println( "\tstatic private final " + classname + " instance = new " + classname + "();");
		ps.println( "\tstatic public " + classname + " getInstance() {");
		ps.println( "\t\treturn instance;");
		ps.println( "\t}");
		ps.println();
		
		ps.println( "}");
		ps.println();
		
		ps.close();
	}
	
	public final void outCodes( final String path, final Coder coder) throws IOException {
		outMXBeanInterfaceCodes( path, coder.getNamespace());
		outMXBeanClassCodes( path, coder);
		
		if( !rolecount) {
			outGameInterfaceCodes( path, coder);
			outGameClassCodes( path, coder);
		}
	}
}
