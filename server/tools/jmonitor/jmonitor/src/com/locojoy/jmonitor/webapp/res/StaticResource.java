package com.locojoy.jmonitor.webapp.res;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.transform.stream.StreamSource;


import com.sun.jersey.api.NotFoundException;
import com.sun.jersey.api.view.Viewable;
import com.locojoy.jmonitor.main.Main;
import com.locojoy.jmonitor.webapp.Mime;


public class StaticResource {

	private static Response _response(String fn, String ext, InputStream is, boolean use_expire){
		if (null == is)
			throw new NotFoundException( fn + " NOT FOUND");
		
		ResponseBuilder rb = Response.ok().type(Mime.type(ext));
		if (use_expire && !Main.isDebug()){	
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MINUTE, 5);
			rb.expires(c.getTime());
		}
		
		return rb.entity( is ).build();
	}
	
	private static Response response(String fn){
		String ext = fn.substring(fn.lastIndexOf('.') + 1);
		InputStream is = StaticResource.class.getResourceAsStream("/WEB-INF/" + fn);
		return _response(fn, ext, is, true);
	}
	
	private static Response responseFs(String fn){
		String ext = fn.substring(fn.lastIndexOf('.') + 1);
		return responseFs(fn, ext);
	}
	
	private static Response responseFs(String fn, String ext){
		try {
			return _response(fn, ext, new FileInputStream(fn), false);
		} catch (FileNotFoundException e) {
			throw new NotFoundException( fn + " NOT FOUND");
		}
	}
	
	@Path("")
	public static class Index {
		@GET
		public Response get(@Context UriInfo uriInfo){
			return Response.temporaryRedirect( uriInfo.getAbsolutePathBuilder().path("chartchainviewer").build() ).build();
		}
	}

	
	@Path("js/{fn}")	
	public static class JsRes{
		@GET
		public Response get(@PathParam("fn") String fn){
			return response("js/"+fn);
		}
	}

	@Path("jslib/{fn}")	
	public static class JslibRes{
		@GET
		public Response get(@PathParam("fn") String fn){
			return response("jslib/"+fn);
		}
	}
	
	@Path("css/{fn}")	
	public static class CssRes{
		@GET
		public Response get(@PathParam("fn") String fn){
			return response("css/"+fn);
		}
	}

	@Path("favicon.ico")	
	public static class FaviconRes{
		@GET
		public Response get(){
			return response("favicon.ico");
		}
	}
	
	@Path("cfg")
	public static class ConfigureRes{
		
		@GET
		@Produces(MediaType.TEXT_HTML)
		public Viewable get(){
			return new Viewable("/cfg.ftl");
		}
		
		@Path("configure")
		@GET
		public Response configure(){
			return responseFs( Main.getConfigureFn() );
		}
		
		@Path("jmxport")
		@GET
		public Response jmxport(){
			return responseFs( Main.getJmxportFn() );
		}
		
		@Path("access")
		@GET
		public Response access(){
			return responseFs( Main.getAccessFn() );
		}
		
		@Path("lastfetch")
		@GET
		public Response lastfetch(){
			return responseFs( Main.getLastfetchFn() );
		}
		
		@Path("logging.properties")
		@GET
		public Response loggingproperties(){
			return responseFs( System.getProperty("java.util.logging.config.file"), "txt" );
		}
	}
	
	@Path("jslib/save.php")
	public static class ReturnPng{
		@POST
		public Response post(StreamSource src){
			return Response.ok()
					.type(Mime.type("png"))
					.header("Content-Disposition", "attachment; filename=\"chart.png\"")
					.entity(src.getInputStream())
					.build();
		}
	}
	
}
