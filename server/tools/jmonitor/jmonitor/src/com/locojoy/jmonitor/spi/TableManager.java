package com.locojoy.jmonitor.spi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.w3c.dom.Element;

import com.locojoy.jmonitor.util.DomHelper;

class TableManager {

	private ConcurrentMap<String, Table> table_map = new ConcurrentHashMap<String, Table>();
	private Element element;
	private Configure configure;

	public TableManager(Configure cfg, Element ele) throws Exception {
		configure = cfg;
		element = ele;
		for (Element e : DomHelper.getElementsByName(ele, "table")) {
			Table t = new Table(this, e);
			if (null != table_map.put(t.getIdentification(), t))
				throw new Exception(t + " duplicate table");
		}

		verifyTablename(null);
	}

	private void verifyTablename(String newTablename) throws Exception {
		Set<String> ts = new HashSet<String>();
		for (Table t : table_map.values())
			if (!ts.add(t.getTableName().toLowerCase()))
				throw new Exception(t + " duplicate tablename");

		if (null != newTablename && ts.contains(newTablename))
			throw new Exception(" tablenamebase " + newTablename + " already exist");

	}

	Configure getConfigure() {
		return configure;
	}

	void prepareDb() throws Exception {
		for (Table t : table_map.values())
			t.prepareDb(false);
	}

	List<Table> getTableList() {
		return new ArrayList<Table>(table_map.values());
	}

	Table getTable(String identification) {
		return table_map.get(identification);
	}

	synchronized void addTable(Table nt) throws Exception {
		if (table_map.containsKey(nt.getIdentification()))
			throw new Exception(nt + " already exist");

		verifyTablename(nt.getTableName());

		nt.prepareDb(true);
		table_map.put(nt.getIdentification(), nt);
		element.appendChild(nt.getElement());
		
		if (nt.isDynamicMBean())
			configure.getLocalizeManager().addBaseKeyForDynamicMBean(nt.getIdentification());
	}

	synchronized boolean removeTable(String identification) {
		Table t = table_map.remove(identification);
		if (null != t) {
			element.removeChild(t.getElement());
			return true;
		}
		return false;
	}
	
	void repairDb_DAY(long from) throws Exception{
		for (Table t : table_map.values())
			t.repairDb_DAY(from);
	}
	
	void unshardingDb() throws Exception{
		for (Table t : table_map.values())
			t.unshardingDb();
	}

}
