package com.locojoy.jmonitor.webapp.res;

import java.util.List;
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
import com.locojoy.jmonitor.MultiLine.SepKey;


@Path("chartchaindata")
public class ChartChainData {
	
	@POST
	public String data(@FormParam("condition") String condition){
		Object obj=JSONValue.parse(condition);
		JSONObject jo = (JSONObject) obj;
		String bean = (String)jo.get("bean");
		@SuppressWarnings("unchecked")
		List<String> values = (List<String>)jo.get("values");
		String key = (String)jo.get("key");
		Boolean dayline = (Boolean)jo.get("dayline");
		Object rg = jo.get("ranges");
		Object sk = jo.get("sepkey");
		
		SepKey sepkey = null;
		if (null != sk){
			JSONObject so = (JSONObject)sk;
			sepkey = new SepKey((String)so.get("key"), ((Number)so.get("min")).intValue(), 
					((Number)so.get("max")).intValue(), ((Number)so.get("sep")).intValue());
		}
		
		MultiLine ml = new MultiLine(bean, values, key, dayline, sepkey);
		if (null != rg){
			@SuppressWarnings("unchecked")
			Map<String, JSONObject> ro = (Map<String, JSONObject>)rg;
			
			for(Map.Entry<String, JSONObject> e : ro.entrySet()){
				JSONObject o = e.getValue();
				Object low = o.get("low");
				Object up = o.get("up");
				ml.getRanges().put(e.getKey(), new Range(low == null ? null : ((Number)low).longValue(), up == null ? null : ((Number)up).longValue()));
			}
		}
		
		try {
			return JSONValue.toJSONString( Monitor.getManager().queryBean( ml ) );
		} catch (Exception e) {
			throw new WebApplicationException( Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build() );
		}
	}
}
