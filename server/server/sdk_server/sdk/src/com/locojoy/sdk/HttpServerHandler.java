package com.locojoy.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HttpServerHandler implements HttpHandler {
	
	private void onError(HttpExchange exchange) throws IOException {
		exchange.sendResponseHeaders( HttpURLConnection.HTTP_BAD_REQUEST, 0);
		exchange.getResponseBody().close();
	}
	
	static String getQueryString( HttpExchange exchange) throws IOException {
		if( 0 == exchange.getRequestMethod().compareToIgnoreCase( "GET"))
			return exchange.getRequestURI().getQuery();
		final BufferedReader bufferreader = new BufferedReader( new InputStreamReader( exchange.getRequestBody(), "UTF-8"));
		final StringBuilder resultDataBuilder = new StringBuilder();;
		while( true) {
			final String line = bufferreader.readLine();
			if( null == line)
				break;
			resultDataBuilder.append( line);
		}
		return resultDataBuilder.toString();
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		final URI uri = exchange.getRequestURI();
		final String path = uri.getPath();
		final String query = getQueryString( exchange);
		
		if( mkdb.Trace.isDebugEnabled())
			mkdb.Trace.debug( "HttpServerHandler url = " + path + " args = " + query);
		
		final byte[] result = PlatManager.onHttpServerCallBack( path, query);
		if( null == result) {
			onError( exchange);
			return;
		}
		final Headers responseHeaders = exchange.getResponseHeaders();
		responseHeaders.set("Content-Type", "text/plain");
		exchange.sendResponseHeaders( HttpURLConnection.HTTP_OK, 0);
		final OutputStream os = exchange.getResponseBody();
		try {
			os.write( result);
		} finally {
			os.close();
		}
	}
}
