package com.locojoy.jmonitor.spi;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;

import com.locojoy.jmonitor.IServer;
import com.locojoy.jmonitor.util.JMXTimeoutConnect;

class Server implements IServer {

	static Logger logger = Logger.getLogger(Server.class.getName());

	private int serverId;
	private String serverUrl;
	private String serverDescription;

	private Map<String, String> extra;

	private JMXConnector connector;
	private boolean opened;
	private long lastPullTime;

	Server(ServerManager man, String url, int id, String desc, Map<String, String> ext) {
		serverId = id;
		serverUrl = url;
		serverDescription = desc;
		extra = ext;
	}

	@Override
	public int getId() {
		return serverId;
	}

	@Override
	public String getUrl() {
		return serverUrl;
	}

	@Override
	public String getDescription() {
		return serverDescription;
	}

	@Override
	public Map<String, String> getExtra() {
		return extra;
	}

	@Override
	public boolean isOpened() {
		return opened;
	}

	@Override
	public String toString() {
		return "serverId: " + serverId + ", serverUrl: " + serverUrl + ", serverDescription: " + serverDescription;
	}

	JMXConnector getConnector() {
		return connector;
	}

	long getLastPullTime() {
		return lastPullTime;
	}

	void connect() throws IOException {
		if (opened)
			return;

		try {
			JMXServiceURL url = new JMXServiceURL(serverUrl);
			connector = JMXTimeoutConnect.connectWithTimeout(url, 5, TimeUnit.SECONDS);
			opened = true;
			logger.info(this + " opened");
		} catch (IOException e) {
			logger.info(this + " connectErr");
			logger.throwing("Server", "connect", e);
			opened = false;
			throw e;
		}
	}

	void close() {
		if (!opened)
			return;
		try {
			connector.close();
			logger.info(this + " closed");
		} catch (IOException e) {
			logger.info(this + " closeErr" );
			logger.throwing("Server", "close", e);
		}
		opened = false;
	}

	private void pullOneTable(final long time, final Table table) throws IOException, InstanceNotFoundException, 
		IntrospectionException, ReflectionException, AttributeNotFoundException, MBeanException  {
		
		MBeanServerConnection mbeanserver = connector.getMBeanServerConnection();
		
		if (table.isDynamicMBean()) {
			
			Map<String, Long> attrmap = new HashMap<String, Long>();
			for (ObjectName on : mbeanserver.queryNames(table.getObjectName(), null) ) {
				
				Set<String> attrs = new HashSet<String>();
				for (MBeanAttributeInfo mi : mbeanserver.getMBeanInfo(on).getAttributes())
					attrs.add(mi.getName());

				if (logger.isLoggable(Level.FINEST)) {
					logger.finest(table.getIdentification() + ".d.attrs=" + attrs);
				}

				AttributeList al = mbeanserver.getAttributes(on, attrs.toArray(new String[0]));
				for (Attribute a : al.asList()) {
					String k = a.getName();
					Object v = a.getValue();
					if (logger.isLoggable(Level.FINEST)) {
						logger.finest(table.getIdentification() + ".d.attr." + k + "=" + v);
					}
					if (v instanceof Number) {
						long nv = ((Number) v).longValue();
						Long ov = attrmap.get(k);
						if (ov == null)
							attrmap.put(k, nv);
						else
							attrmap.put(k, nv + ov);
					}
				}
			}
			table.storeDb(serverId, time, attrmap);
			
			
		} else if (table.getKeyColumnList().isEmpty()) {
			Map<String, Long> attrmap = new HashMap<String, Long>();
			for (String attr : table.getColumnAttrs())
				attrmap.put(attr, 0L);

			if (logger.isLoggable(Level.FINEST)) {
				logger.finest(table.getIdentification() + ".attrs=" + attrmap.keySet());
			}

			for (ObjectName on : mbeanserver.queryNames(table.getObjectName(), null)) {
				
				if (table.getType().equals("Xdb") ){//避免getAttr里的warning
					String oname = on.getKeyProperty("name");
					if (null !=oname && !table.getName().equals(oname))
						continue;
				}
					
				AttributeList al = mbeanserver.getAttributes(on, table.getBaseAttrs().toArray(new String[0]));
				Map<String, Object> attributemap = new HashMap<String, Object>();
				for (Attribute a : al.asList()) {
					if (logger.isLoggable(Level.FINEST)) {
						logger.finest(table.getIdentification() + "." + a.getName() + "=" + a.getValue());
					}
					attributemap.put(a.getName(), a.getValue());
				}

				for (String attr : attrmap.keySet()) {
					attrmap.put(attr, attrmap.get(attr) + getAttr(attributemap, attr));
				}
			}
			table.storeDb(serverId, time, attrmap);

			
		} else {
			Set<String> tableallattrs = table.getColumnAttrs();
			Map<String, Long> attrmap = new HashMap<String, Long>();
			Object val = mbeanserver.getAttribute(table.getObjectName(), table.getType());// mbean的type也要是唯一的attribute名称
			if (val instanceof TabularData) {
				TabularData td = (TabularData) val;
				Collection<?> values = td.values();
				for (Object v : values) {
					Map<String, Object> attributemap = new HashMap<String, Object>();
					CompositeData cd = (CompositeData) v;
					attributemap.put("key", cd.get("key"));

					attrmap.clear();
					Object tv = cd.get("value");
					if (table.isValueNumber() && tv instanceof Number) {// 让value可以是一个单个数值，而不是组合bean
						String vattr = table.getFirstValueAttr();
						tableallattrs.remove(vattr);
						attrmap.put(vattr, ((Number) tv).longValue());
					} else {
						attributemap.put("value", tv);
					}

					for (String attr : tableallattrs)
						attrmap.put(attr, getAttr(attributemap, attr));
					table.storeDb(serverId, time, attrmap);
				}
			} else {
				logger.warning(table + " not tabular data");
			}
		}
	}

	void pull(long time, List<Table> tables) {

		try {
			connect();
		} catch (IOException e) {
			return;
		}

		try {
			for (Table table : tables) {

				try {
					pullOneTable(time, table);
				} catch (IOException ioexcept) {
					throw ioexcept;
				} catch (InstanceNotFoundException | IntrospectionException | AttributeNotFoundException | ReflectionException | MBeanException e) {
					logger.info(this + " pullErr: " + e.getMessage());
					logger.throwing("Server", "pull", e);
				} catch (Throwable t) {
					logger.info(this + " pullErr: " + t.getMessage());
					logger.throwing("Server", "pull", t);
				}
				
			}

			lastPullTime = time;

		} catch (IOException e) {
			close();
		}

	}

	private static long getAttr(Map<String, Object> map, String key) {
		String[] seps = key.split("\\.");
		if (seps.length == 0) {
			logger.warning(key + " getattr split err");
			return 0;
		}

		Object val = map.get(seps[0]);
		if (val == null) {
			logger.warning(map.keySet() + "." + key + "." + seps[0] + " getattr attr not contained");
			return 0;
		}

		for (int i = 0; i < seps.length;) {
			boolean islast = (i == seps.length - 1);
			if (islast) {
				if (val instanceof Number)
					return ((Number) val).longValue();

				logger.warning(key + " getattr not number");
				return 0;
			}

			if (!(val instanceof CompositeData)) {
				logger.warning(key + " getattr array or tabular not supported yet");
				return 0;
			}

			i++;
			val = ((CompositeData) val).get(seps[i]);
		}

		logger.warning(key + " getattr impossible here");
		return 0;
	}

}