package com.locojoy.jmonitor.beancoder;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class OutManagerCodes {
	private Coder 				coder;
	private String 			outpath;

	public OutManagerCodes( Coder coder, String outpath) {
		this.coder = coder;
		this.outpath = outpath;
	}

	public final void outCodes() throws IOException {

		final String namespace = coder.getNamespace();
		final String filename = outpath + "/Manager.java";

		PrintStream ps = new PrintStream( new BufferedOutputStream( new FileOutputStream( filename)));
		ps.println( "package " + namespace + ";");
		ps.println();

		ps.println( "public class Manager {");
		ps.println();
/*		ps.println( "\tprivate Manager() {}");
		ps.println( "\tprivate static final Manager instance = new Manager();");
		ps.println( "\tpublic static Manager getInstance() {");
		ps.println( "\t\treturn instance;");
		ps.println( "\t}");
		ps.println();
		*/
		ps.println( "\tpublic static void registerMBeans() throws Exception {");
		ps.println( "\t\tjavax.management.MBeanServer server = java.lang.management.ManagementFactory.getPlatformMBeanServer();");
		
		for( final MXBeanData beandata : coder.getMxbeans()) {
			ps.println( "\t\tserver.registerMBean( " + namespace + ".jmx." + beandata.getClassName() + ".getInstance(), javax.management.ObjectName.getInstance(\"" + namespace + ":type=" + beandata.getClassName() + "\"));");
		}
		
		ps.println( "\t}");
		ps.println();
		
		ps.println( "\tpublic static RoleLoginLogout getRoleLoginLogout() {");
		ps.println( "\t\treturn " + namespace + ".impl.RoleLoginLogout.getInstance();");
		ps.println( "\t}");
		ps.println();
		
		for( final MXBeanData beandata : coder.getMxbeans()) {
			if( beandata.isRoleCount())
				continue;
			ps.println( "\tstatic public " + beandata.getClassName() + " get" + beandata.getClassName() + "() {");
			ps.println( "\t\treturn " + namespace + ".impl." + beandata.getClassName() + ".getInstance();");
			ps.println( "\t}");
			ps.println();
		}
		
		ps.println( "}");
		ps.println();

		ps.close();
	}
}
