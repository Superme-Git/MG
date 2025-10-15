
package com.world2.auth;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AuthFilter implements Filter
{
	protected String ignore = "";

	public void init(FilterConfig filterConfig) throws ServletException	{
		ignore = filterConfig.getInitParameter("ignore");
	}

	
	public void destroy()	{
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
				throws IOException, ServletException{
		HttpServletRequest req = (HttpServletRequest)request;
		HttpSession session = req.getSession();
		
		String uri = req.getRequestURI();
		String[] ignores = ignore.split(";");
		for (String ig : ignores){
			if (uri.contains(ig)){
				chain.doFilter(request, response);
				return;
			}
		}
		
		if (session.getAttribute("loguser") == null){
			req.getRequestDispatcher("login.jsp").forward(request, response);
		}else
			chain.doFilter(request, response);
	}
}

