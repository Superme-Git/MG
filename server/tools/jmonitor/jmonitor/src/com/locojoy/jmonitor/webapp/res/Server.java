package com.locojoy.jmonitor.webapp.res;

import java.util.HashMap;
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
import com.locojoy.jmonitor.Monitor;


@Path("server")
public class Server {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable list( ) {
		boolean authcok = true;
		String authcstatus = "PortFowarder 连接正常";
		try{
			Monitor.getManager().testPortForwarderAlive();
		}catch(Exception e){
			authcok = false;
			authcstatus = "PortFowarder: " + e.getMessage();
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("serverlist", Monitor.getManager().getServers());
		model.put("authcok", authcok );
		model.put("authcstatus", authcstatus );
		
		return new Viewable( "/server.ftl", model ); 
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void add( @FormParam("serverid") int serverId,  @FormParam("url") String url, @FormParam("name") String name){
		try {
			Monitor.getManager().addServer(serverId, url, name);
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build());
		}
	}
	
	@DELETE
	@Path("/{id}")
	public void delete( @PathParam("id") int id){
		try {
			Monitor.getManager().removeServer(id);
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build());
		}
	}
	
	@POST
	@Path("/{id}")
	public void fetch( @PathParam("id") int id){
		try {
			Monitor.getManager().fetchServer(id);
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build());
		}
	}

}
