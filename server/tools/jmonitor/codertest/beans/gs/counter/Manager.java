package gs.counter;

public class Manager {

	public static void registerMBeans() throws Exception {
		javax.management.MBeanServer server = java.lang.management.ManagementFactory.getPlatformMBeanServer();
		server.registerMBean( gs.counter.jmx.LevelRoles.getInstance(), javax.management.ObjectName.getInstance("gs.counter:type=LevelRoles"));
		server.registerMBean( gs.counter.jmx.MoneyIOState.getInstance(), javax.management.ObjectName.getInstance("gs.counter:type=MoneyIOState"));
	}

	public static RoleLoginLogout getRoleLoginLogout() {
		return gs.counter.impl.RoleLoginLogout.getInstance();
	}

	static public MoneyIOState getMoneyIOState() {
		return gs.counter.impl.MoneyIOState.getInstance();
	}

}

