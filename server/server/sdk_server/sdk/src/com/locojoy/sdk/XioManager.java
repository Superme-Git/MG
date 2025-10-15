package com.locojoy.sdk;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import mkio.Engine;
import mkio.Manager;
import mkio.Protocol;
import mkio.Mkio;

public class XioManager extends Manager {
	private static Logger logger = Logger.getLogger(XioManager.class);
	static public class XioInfo {
		private String serverid = "";
		private int plattype = XioManager.getInstance().defaultplattype;

		public String getServerid() {
			return serverid;
		}
		public int getPlattype() {
			return plattype;
		}
	}
	
	private Map<String,Mkio> serverid2xio = new HashMap<String,Mkio>();
	private Map<Mkio,XioInfo> xio2info = new HashMap<Mkio,XioInfo>();
	private Object mutex = new Object();
	private int defaultplattype = Config.getInstance().getDefaultPlatType();

	private static XioManager instance = null;
	public static XioManager getInstance() {
		return instance;
	}
	
	public XioManager() {
		instance = this;
	}

	@Override
	public void execute(Protocol p) {
		if(logger.isDebugEnabled())
			logger.debug("execute" + p);
		super.execute(p);
	}

	@Override
	protected void removeMkio(Mkio xio, Throwable e) {
		synchronized (mutex) {
			final XioInfo info = xio2info.remove(xio);
			if(null != info && !info.serverid.isEmpty())
				serverid2xio.remove(info.serverid);
		}
	}

	@Override
	protected void addMkio(Mkio xio) {
		boolean added = false;
		if (Engine.isOpen()) {
			synchronized (mutex) {
				added = (super.getMaxSize() <= 0 || xio2info.size() < super.getMaxSize()) && null == xio2info.put(xio, new XioInfo());
			}
		}
		if(!added) {
			if(logger.isInfoEnabled())
				logger.info("Close in addXio " + xio + " size=" + size() + "/" + getMaxSize());
			xio.close();
		} else {
			//FIXME:注释掉是因为目前与gdeliverd的安全机制不一致，导致gdeliverd频繁被T掉。by dc@20151010
			//SecurityFilter.setXioOutputSecurity(xio, new ARCFourSecurity(), Config.getInstance().getXioOutputSecurity());
			//SecurityFilter.setXioInputSecurity(xio, new ARCFourSecurity(), Config.getInstance().getXioInputSecurity());
		}
	}

	@Override
	protected void close() {
		super.close();
		// 对于大量连接的应用，不关闭连接更好。目前尽量干净的退出。
		Map<Mkio,XioInfo> tmp;
		synchronized (mutex) {
			tmp = xio2info;
			xio2info = new HashMap<Mkio,XioInfo>();
			serverid2xio.clear();
		}
		for(Map.Entry<Mkio, XioInfo> e: tmp.entrySet())
			e.getKey().close();
	}
	
	@Override
	public int size() {
		synchronized (mutex) {
			return xio2info.size();
		}
	}

	@Override
	public Mkio get() {
		synchronized (mutex) {
			if (xio2info.isEmpty())
				return null;
			return xio2info.keySet().iterator().next();
		}
	}
	
	public XioInfo getXioInfo(Mkio xio) {
		synchronized (mutex) {
			return xio2info.get(xio);
		}
	}
	
	public boolean sendProtocol(Protocol p, String serverid) {
		Mkio xio;
		synchronized (mutex) {
			xio = serverid2xio.get(serverid);
		}
		if(null == xio) {
			logger.error("Send proto:" + p.getType() + " to server:" + serverid + ",xio is null!");
			return false;
		}
		else {
			logger.debug("Send proto:" + p.getType() + "," + p.getClass() + " to server:" + serverid + ",ip:" + xio.getPeerInfo());
			return p.send(xio);
		}
	}
	
	public int updateGameServerIDAndPlatType(Mkio xio, String serverid, int plattype) {
		synchronized (mutex) {
			XioInfo info = xio2info.get(xio);
			if(null == info)
				return openau.AuAnyPlatTypes.unknow;
			if(openau.AuAnyPlatTypes.unknow != plattype)
				info.plattype = plattype;
			if(serverid.equals(info.getServerid()))
				logger.warn("The serverid:" + serverid + " already in auany!Will replace by current xio!Think twice,Boy!");
			info.serverid = serverid;
			serverid2xio.put(serverid, xio);
			return info.plattype;
		}
	}
	
	public void showCurrentServer() {
		synchronized (mutex) {
			StringBuilder sb = new StringBuilder();
			sb.append("ServersId:\n");
			for(Map.Entry<String, Mkio> ent : serverid2xio.entrySet()) {
				sb.append("[").append(ent.getKey()).append(ent.getValue().getPeerInfo()).append("]\n");
			}
			logger.info(sb.toString());
		}
	}
}
