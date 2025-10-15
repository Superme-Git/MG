package com.locojoy.jmonitor.webapp.res;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONValue;

import com.sun.jersey.api.view.Viewable;
import com.locojoy.jmonitor.ITable;
import com.locojoy.jmonitor.Monitor;


@Path("chartadder")
public class ChartAdder {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable adder() {
		Map<String, Object> tableinfo = new HashMap<String, Object>();
		for (ITable table : Monitor.getManager().getTables()) {
			if (table.isWorking()){
				Map<String, Object> sub = new HashMap<String, Object>();
				sub.put("keyList", table.getKeyList());
				sub.put("valueList", table.getValueList());
				String id = table.getIdentification();
				tableinfo.put(id, sub);
			}
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("tableinfo", JSONValue.toJSONString(tableinfo));
		
		return new Viewable("/chartadder.ftl", model);
	}

}
