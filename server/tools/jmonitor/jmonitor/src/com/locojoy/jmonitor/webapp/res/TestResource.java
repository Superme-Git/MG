package com.locojoy.jmonitor.webapp.res;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.locojoy.jmonitor.spi.ServerBeans;
import com.locojoy.jmonitor.util.JMXTimeoutConnect;

@Path("test")
public class TestResource {

	@Path("mbeaninfo")
	@POST
	@Produces(MediaType.TEXT_HTML)
	public Response mbeaninfo(@FormParam("url") String serverurl) {
		try {
			MBeanServerConnection mbserver;
			JMXConnector connector = null;
			if (serverurl.isEmpty()) {
				mbserver = ManagementFactory.getPlatformMBeanServer();
			} else {
				JMXServiceURL url = new JMXServiceURL(serverurl);
				connector = JMXTimeoutConnect.connectWithTimeout(url, 5, TimeUnit.SECONDS);
				mbserver = connector.getMBeanServerConnection();
			}

			ServerBeans sbs = new ServerBeans();

			sbs.fetch(mbserver);

			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, ServerBeans.Bean> e : sbs.getBeans().entrySet())
				e.getValue().dump(sb);
			
			if (null != connector)
				connector.close();
			
			return Response.ok().type(MediaType.TEXT_PLAIN).entity(sb.toString()).build();
		} catch (Exception e) {
			throw new WebApplicationException( Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build() );
		}
	}

}
