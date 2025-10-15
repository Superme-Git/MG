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
import com.locojoy.jmonitor.IChart;
import com.locojoy.jmonitor.ITable;
import com.locojoy.jmonitor.Monitor;

@Path("chartchainadder")
public class ChartChainAdder {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable adder() {
		
		List<String> cs = new ArrayList<String>();
		Map<String, Object> chartinfo = new HashMap<String, Object>();
		for (IChart chart : Monitor.getManager().getCharts()){
			Map<String, Object> sub = new HashMap<String, Object>();
			sub.put("keyList", new ArrayList<String>(chart.getContrainableKeys()));
			
			String id = chart.getChartName();
			cs.add(id);
			chartinfo.put(id, sub);
		}
		
		List<String> ts = new ArrayList<String>();
		Map<String, Object> tableinfo = new HashMap<String, Object>();
		for (ITable t : Monitor.getManager().getTables()) {
			if (t.isWorking()){
				Map<String, Object> sub = new HashMap<String, Object>();
				sub.put("keyList", t.getKeyList());
				sub.put("valueList", t.getValueList());
				
				String id = t.getIdentification();
				ts.add(id);
				tableinfo.put(id, sub);
			}
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("chartnames", cs);
		model.put("tablenames", ts);
		
		model.put("chartinfo", JSONValue.toJSONString( chartinfo));
		model.put("tableinfo", JSONValue.toJSONString( tableinfo));
		
		return new Viewable("/chartchainadder.ftl", model);
	}
}
