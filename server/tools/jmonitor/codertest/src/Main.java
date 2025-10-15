import gs.counter.RoleLoginLogout;

import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.Random;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

public class Main {

	static public void main( String args[]) throws Exception {
		int rmiport = 26000;
		int serverport = rmiport + 1;
		int serverid = 5;
		
		LocateRegistry.createRegistry(rmiport);
		JMXServiceURL url = new JMXServiceURL(String.format(
				"service:jmx:rmi://localhost:%d/jndi/rmi://localhost:%d/jmxrmi", serverport, rmiport));
		MBeanServer server = ManagementFactory.getPlatformMBeanServer();

		gs.counter.Manager.getInstance().registerMBeans();

		JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, server);
		cs.start();
		System.out.println(cs.getAddress());
		System.out.println("jmx started");
		
		// ×¢²á
		try{
			JMXServiceURL registerurl = new JMXServiceURL(
					"service:jmx:rmi://localhost:1099/jndi/rmi://localhost:1098/jmxrmi");
			JMXConnector c = JMXConnectorFactory.connect(registerurl, null);
			MBeanServerConnection mbserver = c.getMBeanServerConnection();
			ObjectName objname = ObjectName.getInstance("com.locojoy.jmonitor:type=JMXMonitor");
			Object res = mbserver.invoke(objname, "monitor", new Object[] { cs.getAddress().toString(),  serverid, "codertestµÄ²âÊÔ"}, new String[] {
					"java.lang.String", "int",  "java.lang.String"});
			System.out.println("register " + res);
			c.close();
		}catch(Exception e){
			System.out.println("register err");
		}
		

		int rolecount = 10000;
		int rolestartid = 100;
		int levelcnt = 100;
		int menpaicnt = 8;
		
		RoleLoginLogout roleloginlogout = gs.counter.Manager.getInstance().getRoleLoginLogout();
		Random r = new Random();
		
		while(true){
			
			for( int i = 0; i < rolecount; i ++) {
				if (r.nextInt(100) < 5)
					roleloginlogout.roleLogin( rolestartid + i, r.nextInt(levelcnt)+1, r.nextInt(2), r.nextInt(menpaicnt));
				
				if (r.nextInt(100) < 2)
					roleloginlogout.roleLevelUp(rolestartid + i, r.nextInt(levelcnt)+1, r.nextInt(2), r.nextInt(menpaicnt));
				
				if (r.nextInt(100) < 5)
					roleloginlogout.roleLogout(rolestartid + i);
			}
			
			Thread.sleep(r.nextInt(100));
		}
		

//		cs.stop();

	}
}
