package com.revature.cc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class ConfigContext extends HttpServlet{
	Logger log = Logger.getLogger(ConfigContext.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// The Servlet Config belongs to the servlet
		String config = this.getServletConfig().getInitParameter("cheese");
		// The Servlet Context belongs to all servlets
		String context = this.getServletContext().getInitParameter("cheese");
		
		log.trace("Config: "+config);
		log.trace("Context: "+context);
		log.trace("context has: "+this.getServletContext().getAttribute("cheese"));
		
		resp.getWriter().write("<div>Config: "+config+"<br>Context: "+context+""
				+ "<br>Context2: "+this.getServletContext().getAttribute("cheese")+"</div>");


		this.getServletContext().setAttribute("cheese", "Brie");
	}
	
	
}
