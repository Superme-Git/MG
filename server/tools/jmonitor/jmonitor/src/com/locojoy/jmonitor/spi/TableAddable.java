package com.locojoy.jmonitor.spi;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.locojoy.jmonitor.ITable;
import com.locojoy.jmonitor.util.DomHelper;

class TableAddable implements ITable {
	private String domain;
	private String type;
	private String name;
	
	private Map<String, String> keyattr_map;
	private Map<String, String> valattr_map;
	
	private String identification;
	
	private String xmlDescription;
	private boolean canBeDynamicMBean;
	private String xmlDescriptionAsDynamicMBean;

	boolean isFreshNew = false;

	TableAddable(Configure config, ServerBeans.Bean bean) throws Exception {
		domain = bean.domain;
		type = bean.type;
		name = bean.name;
		identification = bean.identification;
		
		keyattr_map = bean.keyattr_map;
		valattr_map = bean.valattr_map;

		Table  table = config.getTableManager().getTable(identification);
		if (null != table)
			throw new Exception("table " + identification + " already exists");
		if (valattr_map.isEmpty())
			throw new Exception("table " + identification + " value attr empty");
		
		xmlDescription = xml();
		xmlAsDynamicMBean();
	};
	

	
	private void xmlAsDynamicMBean() throws Exception {
		canBeDynamicMBean = true;
		if (!keyattr_map.isEmpty())
			canBeDynamicMBean = false;
		else{
			Set<String> vs = new HashSet<String>(valattr_map.values());
			if (vs.size() > 1 || !vs.contains("long"))
				canBeDynamicMBean = false;
		}
		if (!canBeDynamicMBean){
			xmlDescriptionAsDynamicMBean = "";
			return;
		}
		
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		Element ele = document.createElement("table");
		ele.setAttribute("domain", domain);
		ele.setAttribute("type", type);
		if (name.length() > 0)
			ele.setAttribute("name", name);

		if (identification.length() > Table.MAX_TABLENAME_LEN ) 
			ele.setAttribute("tablename", makeMaxLenName(identification, Table.MAX_TABLENAME_LEN));
				
		ele.setAttribute("fetch_offset_minute", "0");
		ele.setAttribute("fetch_period_minute", "10");
		ele.setAttribute("peak", "true");
		ele.setAttribute("sample", "1440");
		ele.setAttribute("dynamic_mbean", "true");
		
		Element e = document.createElement("keycolumn");
		e.setAttribute("attr", "name");
		e.setAttribute("type", "short");
		ele.appendChild(e);
		
		e = document.createElement("column");
		e.setAttribute("attr", "count");
		e.setAttribute("type", "long");
		ele.appendChild(e);
		
		document.appendChild(ele);
		xmlDescriptionAsDynamicMBean = DomHelper.documentToString(document);
	}

	private String xml() throws Exception {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		Element ele = document.createElement("table");
		ele.setAttribute("domain", domain);
		ele.setAttribute("type", type);
		if (name.length() > 0)
			ele.setAttribute("name", name);

		if (identification.length() > Table.MAX_TABLENAME_LEN ) 
			ele.setAttribute("tablename", makeMaxLenName(identification, Table.MAX_TABLENAME_LEN));
				
		ele.setAttribute("fetch_offset_minute", "0");
		ele.setAttribute("fetch_period_minute", "10");
		ele.setAttribute("peak", "false");
		
		for (Map.Entry<String, String> entry : keyattr_map.entrySet()) {
			Element e = document.createElement("keycolumn");
			String attr = entry.getKey();
			e.setAttribute("attr", attr);
			e.setAttribute("type", entry.getValue());
			String name = TableColumn.makeNameFromAttr(attr);
			if (name.length() > TableColumn.MAX_COLUMN_LEN){
				e.setAttribute("name", makeMaxLenName(name, TableColumn.MAX_COLUMN_LEN)); //make suggest
			}
			ele.appendChild(e);
		}

		for (Map.Entry<String, String> entry : valattr_map.entrySet()) {
			Element e = document.createElement("column");
			String attr = entry.getKey();
			e.setAttribute("attr", attr);
			e.setAttribute("type", entry.getValue());
			String name = TableColumn.makeNameFromAttr(attr);
			if (name.length() > TableColumn.MAX_COLUMN_LEN){
				e.setAttribute("name", makeMaxLenName(name, TableColumn.MAX_COLUMN_LEN)); //make suggest
			}
			ele.appendChild(e);
		}

		document.appendChild(ele);

		return DomHelper.documentToString(document);
	}
	
	@Override
	public String getIdentification() {
		return identification;
	}
	
	@Override
	public boolean isWorking() {
		return false;
	}
	
	@Override
	public String getXmlDescription() {
		return canBeDynamicMBean ? xmlDescription + "\n" + xmlDescriptionAsDynamicMBean : xmlDescription;
	}
	
	@Override
	public List<String> getKeyList() {
		return null;
	}

	@Override
	public List<String> getValueList() {
		return null;
	}
	
	static String makeMaxLenName(String ident, int maxlen) {
		while (true) {
			int len = ident.length();
			if (len <= maxlen)
				return ident;

			int i = ident.indexOf("_");
			if (-1 == i)
				return ident.substring(len - maxlen);

			ident = ident.substring(i + 1);
		}
	}

	
	
}