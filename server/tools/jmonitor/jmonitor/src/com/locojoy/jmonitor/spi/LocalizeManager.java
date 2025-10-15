package com.locojoy.jmonitor.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.w3c.dom.Element;

import com.locojoy.jmonitor.Localize;
import com.locojoy.jmonitor.spi.LocalizeImpl.BaseKey;
import com.locojoy.jmonitor.util.DomHelper;

public class LocalizeManager {

	private Configure configure;
	private Element element;
	
	private ConcurrentMap<String, BaseKey> basekey_map = new ConcurrentHashMap<String, BaseKey>();
	private ConcurrentMap<String, LocalizeImpl> localize_map = new ConcurrentHashMap<String, LocalizeImpl>();

	LocalizeManager(Configure config, Element ele) throws Exception {
		configure = config;
		element = ele;

		for (Element e : DomHelper.getElementsByName(ele, "basekey")) {
			BaseKey c = new BaseKey(this, e);
			if (null != basekey_map.put(c.getName(), c))
				throw new Exception(c.getName() + " duplicate basekey");
		}
		
		for (Element e : DomHelper.getElementsByName(ele, "localize")) {
			LocalizeImpl c = new LocalizeImpl(this, e);
			if (null != localize_map.put(c.getName(), c))
				throw new Exception(c.getName() + " duplicate localize");
		}
	}
	
	//系统增加
	void addBaseKeyForDynamicMBean(String beanname) throws Exception{
		BaseKey basekey = new BaseKey(this, beanname);
		if (null != basekey_map.putIfAbsent(basekey.getName(), basekey))
			throw new Exception(basekey.getName() + " already exist");

		element.appendChild(basekey.getElement());
	}
	
	//取数据过程中需要
	BaseKey getBaseKeyForDynamicMBean(String beanname){
		return basekey_map.get("RO_" + beanname);
	}
	
	Configure getConfigure(){
		return configure;
	}
	
	BaseKey getBaseKey(String name){
		return basekey_map.get(name);
	}
	
	List<Localize> getLocalizes() {
		List<Localize> locals = new ArrayList<Localize>(basekey_map.values());
		locals.addAll(localize_map.values());
		return locals;
	}
	
	//用户增加的
	void addBaseKey(BaseKey basekey) throws Exception {
		if (null != basekey_map.putIfAbsent(basekey.getName(), basekey))
			throw new Exception(basekey.getName() + " already exist");

		element.appendChild(basekey.getElement());
	}

	boolean removeBaseKey(String name) throws Exception {
		BaseKey c = basekey_map.remove(name);
		if (null != c) {
			element.removeChild(c.getElement());
			return true;
		}
		return false;
	}

	void addLocalize(LocalizeImpl local) throws Exception {
		if (null != localize_map.putIfAbsent(local.getName(), local))
			throw new Exception(local.getName() + " already exist");

		element.appendChild(local.getElement());
	}

	boolean removeLocalize(String bean) throws Exception {
		LocalizeImpl c = localize_map.remove(bean);
		if (null != c) {
			element.removeChild(c.getElement());
			return true;
		}
		return false;
	}

}
