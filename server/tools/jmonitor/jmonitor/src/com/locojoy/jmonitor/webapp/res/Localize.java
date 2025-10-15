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

import org.json.simple.JSONValue;

import com.sun.jersey.api.view.Viewable;
import com.locojoy.jmonitor.ITable;
import com.locojoy.jmonitor.Monitor;


@Path("localize")
public class Localize {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable list() {
		
		List<Object> locals = new ArrayList<Object>();
		for (com.locojoy.jmonitor.Localize local : Monitor.getManager().getLocalizes()) {
			Map<String, Object> loc = new HashMap<String, Object>();

			loc.put("beanName", local.getName());
			String desc = local.getXmlDescription();
			loc.put("rows", desc.split("\n").length + 1);
			loc.put("xmlDescription", desc);
			loc.put("type", local.isBaseKey() ? "basekey" : "bean");
			loc.put("readOnly", local.isReadOnly());
			locals.add(loc);
		}
		
		Map<String, Object> beaninfo = new HashMap<String, Object>();
		for (ITable t : Monitor.getManager().getTables()) {
			if (t.isWorking()){
				Map<String, Object> b = new HashMap<String, Object>();
				b.put("keyList", t.getKeyList());
				b.put("valueList", t.getValueList());
				beaninfo.put(t.getIdentification(), b);
			}
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("locals", locals );
		model.put("beaninfo", JSONValue.toJSONString(beaninfo));
		
		return new Viewable("/localize", model);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void add(@FormParam("xml") String xml){
		try {
			Monitor.getManager().addLocalize( xml );
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build());
		}
	}
	
	@DELETE
	@Path("/{type}/{key}")
	public void delete( @PathParam("type") String type, @PathParam("key") String key){
		try {
			Monitor.getManager().removeLocalize( key, type.equals("basekey") );
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build());
		}
	}
	
}
