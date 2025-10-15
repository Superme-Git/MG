package mkdb.util;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import mkdb.Trace;

/**
 * <p>ע��MBean��Ҫ�޸�java�Ĳ��������ļ�(java.policy)������Ȩ�ޣ���ʽ���£�
 * <br>-- permission javax.management.MBeanTrustPermission "register";
 * 
 * <p> java.policy �ļ�����Ŀ¼��
 * <br>JDK_HOME/jre/lib/security/java.policy
 * <br>JRE_HOME/lib/security/java.policy 
 * <br>��ð�jdk,jre�����ö��޸ġ�
 *
 * <p>[ע��] ʹ��MBean��������Ӧ��ʱ��������Ҫ����������������ο���Ӧjvm�汾��jmx���á�
 * @see JMXServer
 */

public class MBeans {
	public static ObjectName newObjectName(String name) {
		try {
			return new ObjectName(name);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static class Manager {
		private java.util.Set<ObjectName> mbeans = new java.util.HashSet<ObjectName>();
		/**
		 * ע��� MBean ʵ��
		 * @param object MBean ʵ��
		 * @param name   MBean ����
		 */
		public ObjectName register(Object object, String name) {
			synchronized (mbeans) {
				ObjectName objname = newObjectName(name);

				if (false == mbeans.add(objname))
					throw new RuntimeException("duplicate mbean name of '" + objname + "'");

				try {
					MBeanServer server = java.lang.management.ManagementFactory.getPlatformMBeanServer();
					server.registerMBean(object, objname);
					return objname;
				} catch (Throwable e) {
					mbeans.remove(objname);
					throw new RuntimeException("see mkdb.Mkdb.registerMBean", e);
				}
			}
		}

		/**
		 * ע������ͨ�������ע���mbeans��
		 */
		public void unregisterAll() {
			synchronized (mbeans) {
				MBeanServer server = java.lang.management.ManagementFactory.getPlatformMBeanServer();
				for (ObjectName name : mbeans) {
					try {
						server.unregisterMBean(name);
					} catch (Throwable e) {
						Trace.error("unregisterMBean name=" + name, e);
					}
				}
				mbeans.clear();
			}
		}

		/**
		 * ע����MBean��
		 * 
		 * @param objname �������֣�register ���ص����֡�
		 */
		public void unregister(ObjectName objname) {
			synchronized (mbeans) {
				try {
					if (mbeans.remove(objname)) {
						MBeanServer server = java.lang.management.ManagementFactory.getPlatformMBeanServer();
						server.unregisterMBean(objname);
					}
				} catch (Throwable e) {
					Trace.error("unregisterMBean name=" + objname, e);
				}
			}
		}
	}

	////////////////////////////////////////////////////////
	// global instance, Ϊ�˼��ݼ���������
	private static Manager local = new Manager();

	/**
	 * ע��� MBean ʵ��
	 * @param object MBean ʵ��
	 * @param name   MBean ����
	 */
	@Deprecated
	public static ObjectName register(Object object, String name) {
		return local.register(object, name);
	}

	/**
	 * Mkdb ֹͣ��ʱ���Զ����á�����Ҫ��ʾ���á�
	 */
	@Deprecated
	public static void unregisterAll() {
		local.unregisterAll();
	}

	/**
	 * ע����MBean��
	 * 
	 * @param objname �������֣�register ���ص����֡�
	 */
	@Deprecated
	public static void unregister(ObjectName objname) {
		local.unregister(objname);
	}
}
