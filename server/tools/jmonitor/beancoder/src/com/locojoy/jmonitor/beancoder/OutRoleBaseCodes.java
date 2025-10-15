package com.locojoy.jmonitor.beancoder;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class OutRoleBaseCodes {

	private Coder 				coder;
	private String 			outpath;

	public OutRoleBaseCodes( Coder coder, String outpath) {
		this.coder = coder;
		this.outpath = outpath;
	}
	
	public final void outCodes() throws IOException {
		outLoginLogoutInterface();
		outLoginLogoutInterfaceImpl();
	}
	
	private final void outLoginLogoutInterface() throws IOException {
		
		String filename = outpath + "/RoleLoginLogout.java";
		
		PrintStream ps = new PrintStream( new BufferedOutputStream( new FileOutputStream( filename)));
		ps.println( "package " + coder.getNamespace() + ";");
		ps.println();
		
		ps.println( "public abstract class RoleLoginLogout {");
		
		ps.print( "\tprotected abstract void raw_roleLogin( long roleid, ");
		coder.getRolebase().outArgs( ps);
		ps.println( ");");

		ps.println( "\tprotected abstract void raw_roleLogout( long roleid);");
	
		ps.print( "\tprotected abstract void raw_roleLevelUp( long roleid, ");
		coder.getRolebase().outArgs( ps);
		ps.println( ");");

		ps.println();
		
		{
			ps.println( "\tprivate class RoleLoginTask extends xdb.Procedure.Task { ");
			ps.println();
	
			ps.println( "\t\tprivate long roleid;");
			for( BeanVariable bv : coder.getRolebase().getVariables())
				ps.println( "\t\tprivate " + bv.getType() + " " + bv.getName() + ";");
			ps.println();
	
			{
				ps.print( "\t\tpublic RoleLoginTask( long roleid, ");
				coder.getRolebase().outArgs( ps);
				ps.println( ") {");
				
				ps.println( "\t\t\tthis.roleid = roleid;");
				for( BeanVariable bv : coder.getRolebase().getVariables())
					ps.println( "\t\t\tthis." + bv.getName() + " = " + bv.getName() + ";");
				ps.println( "\t\t}");
				ps.println();
			}
		
			ps.println("\t\t@Override");
			ps.println("\t\tpublic void run() {");
			ps.print("\t\t\traw_roleLogin( ");
			ps.print( "roleid, ");
			coder.getRolebase().outVars( ps);
			ps.println( ");");
			ps.println( "\t\t}");
	
			ps.println( "\t}");
			ps.println();
	
			ps.print( "\tpublic void roleLogin( ");
			ps.print( "long roleid, ");
			coder.getRolebase().outArgs( ps);
			ps.println( ") {");
			
			ps.println( "\t\tif( null != xdb.Transaction.current())");
			ps.print( "\t\t\txdb.Procedure.ppostWhileCommit( new RoleLoginTask( roleid, ");
			coder.getRolebase().outVars( ps);
			ps.println( "));");
			ps.println( "\t\telse");
			ps.print( "\t\t\traw_roleLogin( roleid, ");
			coder.getRolebase().outVars( ps);
			ps.println( ");");
			
			ps.println( "\t}");

		}

		ps.println();

		{
			ps.println( "\tprivate class RoleLogoutTask extends xdb.Procedure.Task { ");
			ps.println();
	
			ps.println( "\t\tprivate long roleid;");
			ps.println();
	
			ps.println( "\t\tpublic RoleLogoutTask( long roleid) {");
			ps.println( "\t\t\tthis.roleid = roleid;");
			ps.println( "\t\t}");
			ps.println();
		
			ps.println("\t\t@Override");
			ps.println("\t\tpublic void run() {");
			ps.println("\t\t\traw_roleLogout( roleid);");
			ps.println( "\t\t}");
	
			ps.println( "\t}");
			ps.println();
	
			ps.println( "\tpublic void roleLogout( long roleid) {");
			ps.println( "\t\tif( null != xdb.Transaction.current())");
			ps.println( "\t\t\txdb.Procedure.ppostWhileCommit( new RoleLogoutTask( roleid));");
			ps.println( "\t\telse");
			ps.println( "\t\t\traw_roleLogout( roleid);");
			
			ps.println( "\t}");
		}

		ps.println();

		{
			ps.println( "\tprivate class RoleLevelUpTask extends xdb.Procedure.Task { ");
			ps.println();
	
			ps.println( "\t\tprivate long roleid;");
			for( BeanVariable bv : coder.getRolebase().getVariables())
				ps.println( "\t\tprivate " + bv.getType() + " " + bv.getName() + ";");
			ps.println();
	
			{
				ps.print( "\t\tpublic RoleLevelUpTask( long roleid, ");
				coder.getRolebase().outArgs( ps);
				ps.println( ") {");
				
				ps.println( "\t\t\tthis.roleid = roleid;");
				for( BeanVariable bv : coder.getRolebase().getVariables())
					ps.println( "\t\t\tthis." + bv.getName() + " = " + bv.getName() + ";");
				ps.println( "\t\t}");
				ps.println();
			}
		
			ps.println("\t\t@Override");
			ps.println("\t\tpublic void run() {");
			ps.print("\t\t\traw_roleLevelUp( ");
			ps.print( "roleid, ");
			coder.getRolebase().outVars( ps);
			ps.println( ");");
			ps.println( "\t\t}");
	
			ps.println( "\t}");
			ps.println();
	
			ps.print( "\tpublic void roleLevelUp( ");
			ps.print( "long roleid, ");
			coder.getRolebase().outArgs( ps);
			ps.println( ") {");
			
			ps.println( "\t\tif( null != xdb.Transaction.current())");
			ps.print( "\t\t\txdb.Procedure.ppostWhileCommit( new RoleLevelUpTask( roleid, ");
			coder.getRolebase().outVars( ps);
			ps.println( "));");
			ps.println( "\t\telse");
			ps.print( "\t\t\traw_roleLevelUp( roleid, ");
			coder.getRolebase().outVars( ps);
			ps.println( ");");
			
			ps.println( "\t}");
		}

		ps.println();
		
		ps.println( "}");
		ps.println();
		
		ps.close();
	}
	
	private final void outRoleCountSetVariableMethodCodes( PrintStream ps) {
		
		final MXBeanData 	rolecount = coder.getRoleCount();
		final String 		namespace = coder.getNamespace();
	
		ps.print( namespace + ".jmx." + rolecount.getClassName() + ".getInstance().setRoleCountValue");
	}

	private final void outLoginLogoutInterfaceImpl() throws IOException {

		final String 		namespace = coder.getNamespace();
		final String 		filename = outpath + "/impl/RoleLoginLogout.java";
		final String 		fullrolebasename = namespace + ".bean." + coder.getRolebase().getName();
		final BeanData		rolebase = coder.getRolebase();
		
		PrintStream ps = new PrintStream( new BufferedOutputStream( new FileOutputStream( filename)));
		ps.println( "package " + namespace + ".impl;");
		ps.println();
		
		ps.println( "public class RoleLoginLogout extends " + namespace + ".RoleLoginLogout {");
		
		ps.println();
		ps.println( "\tfinal private com.locojoy.jmonitor.counter.RoleMap<" + fullrolebasename + "> rolemap = new com.locojoy.jmonitor.counter.RoleMap<" + fullrolebasename + ">( new "+ fullrolebasename + "());");
		ps.println();
		
		
		{
			ps.println( "\tstatic private class RoleLoginTask extends com.locojoy.jmonitor.counter.SetTask {");
			ps.println( "\t\tprivate long roleid;");
			ps.println( "\t\tprivate " + fullrolebasename + " data;");
			ps.println();
			ps.println( "\t\tpublic RoleLoginTask( long roleid, " + fullrolebasename + " data) {");
			ps.println( "\t\t\tthis.roleid = roleid;");
			ps.println( "\t\t\tthis.data = data;");
			ps.println( "\t\t}");
			ps.println();
			ps.println( "\t\t@Override");
			ps.println( "\t\tpublic void doRunTask() {");
			ps.println( "\t\t\tif( RoleLoginLogout.getInstance().rolemap.newRole( roleid, data))");
			ps.print( "\t\t\t\t");
			outRoleCountSetVariableMethodCodes( ps);
			ps.println( "( data, 1);");
			ps.println( "\t\t}");
			ps.println( "\t};");
			ps.println();
		
			ps.println( "\t@Override");
			ps.print( "\tprotected void raw_roleLogin( long roleid, ");
			rolebase.outArgs( ps);
			ps.println( ") {");
			ps.print( "\t\tnew RoleLoginTask( roleid, new " + fullrolebasename + "( ");
			rolebase.outVars( ps);
			ps.println( ")).runTask();");
			ps.println( "\t}");
			ps.println();
		}
		
		{
			ps.println( "\tstatic private class RoleLogoutTask extends com.locojoy.jmonitor.counter.SetTask {");
			ps.println( "\t\tprivate long roleid;");
			ps.println();
			ps.println( "\t\tpublic RoleLogoutTask( long roleid) {");
			ps.println( "\t\t\tthis.roleid = roleid;");
			ps.println( "\t\t}");
			ps.println();
			ps.println( "\t\t@Override");
			ps.println( "\t\tpublic void doRunTask() {");
			ps.println( "\t\t\t" + fullrolebasename + " data = RoleLoginLogout.getInstance().rolemap.removeRole( roleid);");
			ps.println( "\t\t\tif( null != data)");
			ps.print( "\t\t\t\t");
			outRoleCountSetVariableMethodCodes( ps);
			ps.println( "( data, -1);");
			ps.println( "\t\t}");
			ps.println( "\t};");
			ps.println();

			ps.println( "\t@Override");
			ps.println( "\tprotected void raw_roleLogout( long roleid) {");
			ps.println( "\t\tnew RoleLogoutTask( roleid).runTask();");
			ps.println( "\t}");
			ps.println();
		}
		
		{
			ps.println( "\tstatic private class RoleLevelUpTask extends com.locojoy.jmonitor.counter.SetTask {");
			ps.println( "\t\tprivate long roleid;");
			ps.println( "\t\tprivate " + fullrolebasename + " data;");
			ps.println();
			ps.println( "\t\tpublic RoleLevelUpTask( long roleid, " + fullrolebasename + " data) {");
			ps.println( "\t\t\tthis.roleid = roleid;");
			ps.println( "\t\t\tthis.data = data;");
			ps.println( "\t\t}");
			ps.println();
			ps.println( "\t\t@Override");
			ps.println( "\t\tpublic void doRunTask() {");
			ps.println( "\t\t\tnew RoleLogoutTask( roleid).doRunTask();");
			ps.println( "\t\t\tnew RoleLoginTask( roleid, data).doRunTask();");
			ps.println( "\t\t}");
			ps.println( "\t};");
			ps.println();

			ps.println( "\t@Override");
			ps.print( "\tprotected void raw_roleLevelUp( long roleid, ");
			rolebase.outArgs( ps);
			ps.println( ") {");
			ps.print( "\t\tnew RoleLevelUpTask( roleid, new " + fullrolebasename + "( ");
			rolebase.outVars( ps);
			ps.println( ")).runTask();");
			ps.println( "\t}");
			ps.println();
		}
		
		ps.println( "\tfinal static private RoleLoginLogout instance = new RoleLoginLogout();");
		ps.println( "\tstatic public RoleLoginLogout getInstance() {");
		ps.println( "\t\treturn instance;");
		ps.println( "\t}");
		ps.println();
		
		ps.println( "\tpublic " + fullrolebasename + " get( long roleid) {");
		ps.println( "\t\treturn rolemap.getRole( roleid);");
		ps.println( "\t}");
		
		ps.println();
		ps.println( "}");
		ps.println();
		
		ps.close();
	
	}

}
