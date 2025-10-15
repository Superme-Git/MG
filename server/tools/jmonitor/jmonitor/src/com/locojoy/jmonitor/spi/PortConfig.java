package com.locojoy.jmonitor.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.locojoy.jmonitor.util.DomHelper;

public class PortConfig {

	static class Port {
		int id;
		String name;
		String serverurl;
		Map<String, String> extra = new TreeMap<String, String>();
		
		Element element;

		public Port(Element e) {
			element = e;
			NamedNodeMap nnmap = e.getAttributes();
			for(int i=0; i<nnmap.getLength(); i++){
				Node n = nnmap.item(i);
				extra.put(n.getNodeName(), n.getNodeValue());
			}
			id = Integer.parseInt(extra.remove("zoneid"));
			name = extra.remove("name");
			serverurl = extra.remove("serverurl");
			
			if (serverurl == null || serverurl.isEmpty()){
				int rmiport = Integer.parseInt(extra.remove("port1"));
				int serverport = Integer.parseInt(extra.remove("port2"));
				String server = extra.remove("server");
				serverurl = String.format("service:jmx:rmi://%s:%d/jndi/rmi://%s:%d/jmxrmi", server, serverport, server, rmiport);
			}
		}
	}

	
	String filename;
	Document document;
	Element element;
	private Map<Integer, Port> map = new HashMap<Integer, Port>();

	PortConfig(String portxml) throws Exception {
		filename = portxml;
		document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
		element = document.getDocumentElement();
		
		for (Element e : DomHelper.getElementsByName(element, "server")){
			Port p = new Port(e);
			if (null != map.put(p.id, p))
				throw new Exception(filename + " duplicate server id " + p.id);
		}
	}
	
	synchronized void addPort(int id, String url, String name) throws Exception {
		Element e = document.createElement("server");
		e.setAttribute("zoneid", String.valueOf(id) );
		e.setAttribute("name", name);
		e.setAttribute("serverurl", url);
		
		Port np = new Port(e);
		if (map.containsKey(id))
			throw new Exception(id + " already exist");
		
		map.put(id, np);
		element.appendChild(e);
	}
	
	synchronized boolean removePort(int id) {
		Port p = map.remove(id);
		if (null != p){
			element.removeChild(p.element);
			return true;
		}
		return false;
		
	}
	
	void saveConfig() throws Exception {
		DomHelper.prettySave(document, filename);
	}
	
	Collection<Port> getPorts() {
		return new ArrayList<Port>( map.values() );
	}
	
	

}
