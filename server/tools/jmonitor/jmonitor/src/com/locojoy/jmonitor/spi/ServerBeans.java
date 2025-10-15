package com.locojoy.jmonitor.spi;

import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.ArrayType;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularType;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;


import com.locojoy.jmonitor.spi.TableColumn.ColumnType;
import com.locojoy.jmonitor.util.JMXTimeoutConnect;

final public class ServerBeans {

	public static class Bean {
		String domain;
		String type;
		String name;
		String identification;

		Map<String, String> keyattr_map = new TreeMap<String, String>();
		Map<String, String> valattr_map = new TreeMap<String, String>();

		private Bean(String d, String t, String n) {
			domain = d;
			type = t;
			name = n;
			identification = Table.makeIdentification(domain, type, name);
		}

		boolean valid() {
			return !valattr_map.isEmpty();
		}

		public void dump(StringBuilder sb) {
			sb.append(identification).append(":\n");
			for (Map.Entry<String, String> entry : keyattr_map.entrySet()) 
				sb.append("\t-key- ").append( entry.getKey() ).append(": ").append(entry.getValue()).append("\n");
			
			for (Map.Entry<String, String> entry : valattr_map.entrySet()) 
				sb.append("\t").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
			
		}
	}

	private Map<String, Bean> map = new TreeMap<String, Bean>();

	public synchronized Map<String, Bean> getBeans() {
		return new TreeMap<String, Bean>(map);
	}

	public synchronized void fetch(MBeanServerConnection mbserver) throws Exception {
		map.clear();

		for (ObjectName obj : mbserver.queryNames(null, null)) {

			String type = obj.getKeyProperty("type");
			String name = obj.getKeyProperty("name");
			
			if (null == type)
				continue;
			if (null == name)
				name = "";
			
			Bean bean = new Bean(obj.getDomain(), type, name);
			if (map.containsKey(bean.identification))
				continue;

			MBeanInfo mbinfo = mbserver.getMBeanInfo(obj);
			MBeanAttributeInfo[] ais = mbinfo.getAttributes();

			if (ais.length == 1 && ais[0].getName().equals(type) && ais[0] instanceof OpenMBeanAttributeInfoSupport) {
				OpenMBeanAttributeInfoSupport openai = (OpenMBeanAttributeInfoSupport)  ais[0];
				OpenType<?> ot = openai.getOpenType();

				if (ot instanceof TabularType) {
					TabularType tt = (TabularType) ot;
					addNumberAttr("", tt.getRowType().getType("key"), true, bean);
					addNumberAttr("", tt.getRowType().getType("value"), false, bean);
					if (bean.valid())
						map.put(bean.identification, bean);
					continue;
				}
			}

			for (MBeanAttributeInfo ai : ais) {
				//if (obj.getDomain().equals("Counter") )
					//System.out.println(ai);
				
				if (ai instanceof OpenMBeanAttributeInfoSupport) {
					OpenMBeanAttributeInfoSupport openai = (OpenMBeanAttributeInfoSupport) ai;
					addNumberAttr(ai.getName(), openai.getOpenType(), false, bean);
				} else{
					String t = ATTR_TYPE_MAP.get(ai.getType());
					if (null != t)
						bean.valattr_map.put(ai.getName(), t);
				}
					
					
			}
			if (bean.valid())
				map.put(bean.identification, bean);

		}
	}

	static private void addNumberAttr(String prefix, OpenType<?> ot, boolean askey, Bean bean) {
		if (ot instanceof CompositeType) {
			CompositeType ct = (CompositeType) ot;
			for (String key : ct.keySet()) {
				OpenType<?> cot = ct.getType(key);
				String pre = prefix.length() == 0 ? key : prefix + "." + key;
				addNumberAttr(pre, cot, askey, bean);
			}
		} else if (ot instanceof ArrayType) {
		} else if (ot instanceof TabularType) {
		} else if (ot instanceof SimpleType) {
			String type = SIMPLE_TYPE_MAP.get(((SimpleType<?>) ot).getTypeName());
			if (null != type) {
				if (askey)
					bean.keyattr_map.put(prefix, type);
				else
					bean.valattr_map.put(prefix, type);
			}
		} else {
		}
	}

	static private Map<String, String> SIMPLE_TYPE_MAP = new HashMap<String, String>();
	static private Map<String, String> ATTR_TYPE_MAP = new HashMap<String, String>();
	static {
		SIMPLE_TYPE_MAP.put(SimpleType.BYTE.getTypeName(), ColumnType.TBYTE.toString());
		SIMPLE_TYPE_MAP.put(SimpleType.SHORT.getTypeName(), ColumnType.TSHORT.toString());
		SIMPLE_TYPE_MAP.put(SimpleType.INTEGER.getTypeName(), ColumnType.TINT.toString());
		SIMPLE_TYPE_MAP.put(SimpleType.LONG.getTypeName(), ColumnType.TLONG.toString());
		
		for ( ColumnType ct : ColumnType.values())
			ATTR_TYPE_MAP.put(ct.toString(), ct.toString());
		ATTR_TYPE_MAP.put("java.lang.Long", ColumnType.TLONG.toString());
		ATTR_TYPE_MAP.put("java.lang.Integer", ColumnType.TINT.toString());
		ATTR_TYPE_MAP.put("java.lang.Short", ColumnType.TSHORT.toString());
		ATTR_TYPE_MAP.put("java.lang.Byte", ColumnType.TBYTE.toString());
	}
	
	
	////////////////////////////////
	//测试
	public static interface TestMXBean{  
		public Integer getXXX();
	}
	
	public static class Test implements TestMXBean{  
		
		public Integer getXXX(){
			return 11;
		}
		
	}
	public static void main(String[] argv) throws Exception{
		JMXServiceURL url;
		JMXConnectorServer cs = null;
		if (argv.length > 0){
			url = new JMXServiceURL(argv[0]);
		}else{
			int rmiport = 1098;
			int serverport = 1099;
			LocateRegistry.createRegistry(rmiport);
			url = new JMXServiceURL(String.format("service:jmx:rmi://localhost:%d/jndi/rmi://localhost:%d/jmxrmi", serverport, rmiport));
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
			
			cs.start();
			
			com.goldhuman.Common.Counter storage = new com.goldhuman.Common.Counter("TestCommon");
			storage.increment("testcommon");
			storage.increment("test22common");
			mbs.registerMBean(new Test(), new ObjectName("Counter:type=Test"));
			
			System.out.println("jmx service started!");
		}
		
		JMXConnector connector = JMXTimeoutConnect.connectWithTimeout(url, 5, TimeUnit.SECONDS);
		
		ServerBeans sb = new ServerBeans();
		MBeanServerConnection mbserver = connector.getMBeanServerConnection();
		sb.fetch(mbserver);
		
		for (ObjectName on : mbserver.queryNames(new ObjectName("Counter:type=Test"), null)) {
			Set<String> attrs = new HashSet<String>();
			for(MBeanAttributeInfo mi : mbserver.getMBeanInfo(on).getAttributes() )
				attrs.add(mi.getName());
			AttributeList al = mbserver.getAttributes(on, attrs.toArray(new String[0]));
			for (Attribute a : al.asList()){
				if (a.getValue() instanceof Number){
					System.out.println(a.getName() + "," + a.getValue());
				}
			}
		}
		
		for (ObjectName on : mbserver.queryNames(new ObjectName("Counter:type=Test"), null)) {
			System.out.println(on.toString());
			Set<String> attrs = new HashSet<String>();
			attrs.add("XXX");
			
			AttributeList al = mbserver.getAttributes(on, attrs.toArray(new String[0]));
			for (Attribute a : al.asList())
				System.out.println(a.getName() + "," + a.getValue());
		}
			
		StringBuilder builder = new StringBuilder();
		for(Map.Entry<String, Bean> e : sb.getBeans().entrySet()){
			if (!e.getKey().startsWith("java_lang") && !e.getKey().startsWith("java_nio"))
				e.getValue().dump(builder);
		}
		System.out.println(builder.toString());
		
		if (null != cs)
			cs.stop();
	}


}
