package com.locojoy.jmonitor.webapp.res;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import com.sun.jersey.api.view.Viewable;
import com.locojoy.jmonitor.IChart;
import com.locojoy.jmonitor.Monitor;


@Path("chart")
public class Chart{
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable list() {
		
		List<Object> charts = new ArrayList<Object>();

		for (IChart chart : Monitor.getManager().getCharts()) {
			Map<String, Object> t = new HashMap<String, Object>();

			t.put("chartName", chart.getChartName());
			String desc = chart.getXmlDescription();
			t.put("rows", desc.split("\n").length + 1);
			t.put("xmlDescription", desc);
			
			charts.add(t);
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("chartlist", charts );
		
		return new Viewable("/chart.ftl", model);
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void add(@FormParam("xml") String xml){
		try {
			Monitor.getManager().addChart( xml );
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build());
		}
	}
	
	@Path("{key}")
	@DELETE
	public void delete( @PathParam("key") String key){
		try {
			Monitor.getManager().removeChart( key );
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build());
		}
	}
	
}
