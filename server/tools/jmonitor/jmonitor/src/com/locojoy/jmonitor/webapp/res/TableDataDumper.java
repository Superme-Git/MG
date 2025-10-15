package com.locojoy.jmonitor.webapp.res;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.view.Viewable;
import com.locojoy.jmonitor.IServer;
import com.locojoy.jmonitor.ITable;
import com.locojoy.jmonitor.Monitor;

@Path("tabledatadumper")
public class TableDataDumper {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable datadumper() {
		List<String> ts = new ArrayList<String>();
		for (ITable t : Monitor.getManager().getTables()){
			if (t.isWorking())
				ts.add(t.getIdentification());
		}

		List<Object> ss = new ArrayList<Object>();
		for (IServer serv : Monitor.getManager().getServers()) {
			Map<String, String> t = new HashMap<String, String>();
			t.put("desc", serv.getDescription());
			t.put("id", String.valueOf(serv.getId()));
			ss.add(t);
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("tablenames", ts);
		model.put("servers", ss);

		return new Viewable("/tabledatadumper", model);
	}
}
