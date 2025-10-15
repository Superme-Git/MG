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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.sun.jersey.api.view.Viewable;
import com.locojoy.jmonitor.main.Main;
import com.locojoy.jmonitor.util.StringHelper;
import com.locojoy.jmonitor.webapp.AccessManager.Forbid;


@Path("access")
public class Access {
	@Context UriInfo uriInfo;
	@Context SecurityContext context;
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Viewable list( ) {
		Map<String, Object> model = new HashMap<String, Object>();	
		model.put("user", context.getUserPrincipal().getName());
		
		Map<String, Object> rf = new HashMap<String, Object>();
		for( Map.Entry<String, List<Forbid>> e : Main.getAccessManager().getRoleForbidsMap().entrySet()){
			List<Object> fs = new ArrayList<Object>();
			for(Forbid f: e.getValue()){
				Map<String, String> m = new HashMap<String, String>();
				m.put("path", f.getPath());
				m.put("methods", StringHelper.join(f.getMethods(), ","));
				fs.add(m);
			}
			rf.put( e.getKey(), fs );
		}
		model.put("roleForbids", rf);
		
		model.put("roleUsers", Main.getAccessManager().getRoleUsersMap());
		
		return new Viewable( "/access.ftl", model ); 
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response add( @FormParam("role") String role, @FormParam("user") String user){
		try {
			Main.getAccessManager().add(role, user);
			return Response.created(uriInfo.getAbsolutePath()).build();
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build());
		}
	}
	
	@DELETE
	@Path("{role}/{user}")
	public Response delete( @PathParam("role") String role, @PathParam("user") String user){
		try {
			if (Main.getAccessManager().remove(role, user))
				return Response.seeOther(uriInfo.getBaseUriBuilder().path("access").build()).build();
		} catch (Exception e) {
			throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build());
		}
		throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity("user not exist").build());
	}
}

