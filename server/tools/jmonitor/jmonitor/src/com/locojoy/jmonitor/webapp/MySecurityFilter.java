package com.locojoy.jmonitor.webapp;

import com.sun.jersey.core.util.Base64;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.locojoy.jmonitor.main.Main;

import javax.naming.directory.InitialDirContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.security.Principal;
import java.util.Properties;

public class MySecurityFilter implements ContainerRequestFilter {

	private @Context UriInfo uriInfo;
	private static final String REALM = "Locojoy authentication";

	public ContainerRequest filter(ContainerRequest request) {
		User user = authenticate(request);
		request.setSecurityContext(new Authorizer(user));
		return request;
	}

	private void retryAuthIf(boolean c) {
		if (c)
			throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED)
					.header("WWW-Authenticate", (new StringBuilder()).append("Basic realm=\"").append(REALM).append("\"").toString()).build());
	}

	private User authenticate(ContainerRequest request) {
		String authentication = request.getHeaderValue(ContainerRequest.AUTHORIZATION);
		retryAuthIf(authentication == null || !authentication.startsWith("Basic "));

		authentication = authentication.substring("Basic ".length());
		String[] values = new String(Base64.base64Decode(authentication)).split(":");
		String username = values[0];
		String password = values.length >= 2 ? values[1] : null;
		retryAuthIf((username == null) || !checkCredentials(username, password));

		String role = Main.getAccessManager().getRole(username);
		if (null == role)
			throw new WebApplicationException(Response.Status.FORBIDDEN.getStatusCode());

		if (!Main.getAccessManager().checkRolePermission(role, request.getPath(), request.getMethod()))
			throw new WebApplicationException(Response.Status.FORBIDDEN.getStatusCode());

		return new User(username, role);
	}

	private boolean checkCredentials(String username, String password) {

		if (null == password)
			return false;
		String baseTemplateDN = "uid=?,dc=person,dc=locojoy,dc=com";
		Properties env = new Properties();
		env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(javax.naming.Context.PROVIDER_URL, "ldap://ldap.locojoy.com");

		env.put(javax.naming.Context.SECURITY_PRINCIPAL, baseTemplateDN.replaceFirst("[?]", username));
		env.put(javax.naming.Context.SECURITY_CREDENTIALS, password);
		InitialDirContext ctx = null;
		try {
			ctx = new InitialDirContext(env);
		} catch (Exception e) {
			return false;
		} finally {
			try {
				ctx.close();
			} catch (Exception e) {
			}
		}
		return true;

	}

	public class Authorizer implements SecurityContext {

		private User user;
		private Principal principal;

		public Authorizer(final User user) {
			this.user = user;
			this.principal = new Principal() {

				public String getName() {
					return user.username;
				}
			};
		}

		public Principal getUserPrincipal() {
			return this.principal;
		}

		public boolean isUserInRole(String role) {
			return (role.equals(user.role));
		}

		public boolean isSecure() {
			return "https".equals(uriInfo.getRequestUri().getScheme());
		}

		public String getAuthenticationScheme() {
			return SecurityContext.BASIC_AUTH;
		}
	}

	public class User {

		public String username;
		public String role;

		public User(String username, String role) {
			this.username = username;
			this.role = role;
		}
	}
}