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
import com.locojoy.jmonitor.ITable;
import com.locojoy.jmonitor.Monitor;

@Path("table")
public class Table {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable list() {

		List<Object> tables = new ArrayList<Object>();

		for (ITable ti : Monitor.getManager().getTables()) {
			Map<String, Object> t = new HashMap<String, Object>();

			t.put("identification", ti.getIdentification());
			String desc = ti.getXmlDescription();
			int rows = desc.split("\n").length;
			t.put("xmlDescription", desc);
			t.put("rows", String.valueOf(rows + 1));

			t.put("isWorking", ti.isWorking());

			tables.add(t);
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("tablelist", tables);

		return new Viewable("/table.ftl", model);
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void add(@FormParam("xml") String xml) {
		try {
			Monitor.getManager().addTable(xml);
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build());
		}
	}

	@Path("{key}")
	@DELETE
	public void delete(@PathParam("key") String key) {
		try {
			Monitor.getManager().removeTable(key);
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build());
		}
	}





}
