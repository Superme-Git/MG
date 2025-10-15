package com.locojoy.jmonitor.webapp.res;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONValue;

import com.sun.jersey.api.view.Viewable;
import com.locojoy.jmonitor.IServer;
import com.locojoy.jmonitor.Monitor;

@Path("chartchainviewer")
public class ChartChainViewer {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable viewer() {
		Map<String, Object> chaininfo = new HashMap<String, Object>();
		for (com.locojoy.jmonitor.ChartChain cc : Monitor.getManager().getChartChains()){
			Map<String, Object> r = new HashMap<String, Object>();
			r.put("first", cc.getFirst());
			r.put("nexts", cc.getNexts());
			chaininfo.put(cc.getChartChainName(), r);
		}
		
		Map<String, Object> localinfo = new HashMap<String, Object>();
		for (com.locojoy.jmonitor.Localize local : Monitor.getManager().getLocalizes()) {
			if (!local.isBaseKey()){
				Map<String, Object> t = new HashMap<String, Object>();
				t.put("beanLocalName", local.getLocalName());
				t.put("keyLocals", local.getKeyLocals());
				t.put("valueLocals", local.getValueLocals());
				localinfo.put(local.getName(), t);
			}
		}
		
		List<Object> servers = new ArrayList<Object>();
		for (IServer serv : Monitor.getManager().getServers()) {
			Map<String, String> t = new HashMap<String, String>();
			t.put("desc", serv.getDescription());
			t.put("id", String.valueOf( serv.getId() ));
			servers.add(t);
		}
		
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("chaininfo", JSONValue.toJSONString(chaininfo));
		model.put("localinfo", JSONValue.toJSONString(localinfo));
		model.put("servers", servers);
			
		return new Viewable("/chartchainviewer.ftl", model);
	}
}
