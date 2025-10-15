package com.locojoy.jmonitor.webapp.res;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.locojoy.jmonitor.Monitor;
import com.locojoy.jmonitor.MultiLine;
import com.locojoy.jmonitor.MultiLine.Range;


@Path("chartdata")
public class ChartData {
	
	@POST
	public String data(@FormParam("condition") String condition){
		Object obj=JSONValue.parse(condition);
		JSONObject jo = (JSONObject) obj;
		String chartName = (String)jo.get("chartName");
		Map<String, MultiLine.Range> ranges = new HashMap<String, MultiLine.Range>();
		
		Object rg = jo.get("ranges");
		if (null != rg){
			@SuppressWarnings("unchecked")
			Map<String, JSONObject> ro = (Map<String, JSONObject>)rg;
			
			for(Map.Entry<String, JSONObject> e : ro.entrySet()){
				JSONObject o = e.getValue();
				ranges.put(e.getKey(), new Range((Long)o.get("low"), (Long)o.get("up")));
			}
		}
		
		try {
			return JSONValue.toJSONString( Monitor.getManager().queryChart(chartName, ranges) );
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
		}
	}
}
