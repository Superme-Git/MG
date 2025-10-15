package com.locojoy.jmonitor.webapp;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ContainerResponseWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

public class MyLogFilter implements ContainerResponseFilter {

	private static final Logger LOGGER = Logger.getLogger(MyLogFilter.class.getName());
	private @Context
	SecurityContext sc;
	private ContainerRequest req;

	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
		req = request;
		response.setContainerResponseWriter(new Adapter(response.getContainerResponseWriter()));
		return response;
	}

	private final class Adapter implements ContainerResponseWriter {
		private final ContainerResponseWriter crw;

		Adapter(ContainerResponseWriter crw) {
			this.crw = crw;
		}

		public OutputStream writeStatusAndHeaders(long contentLength, ContainerResponse response) throws IOException {

			StringBuilder sb = new StringBuilder();
			try{
				sb.append(sc.getUserPrincipal().getName()).append(" ");
			}catch(UnsupportedOperationException e){
			}
			sb.append(req.getMethod()).append(" ");
			sb.append(req.getPath()).append(" ");
			sb.append("[").append(response.getStatusType().getStatusCode()).append(" ");
			sb.append(response.getStatusType().getReasonPhrase()).append("]");

			LOGGER.info(sb.toString());
			return crw.writeStatusAndHeaders(contentLength, response);
		}

		public void finish() throws IOException {
			crw.finish();
		}
	}
}