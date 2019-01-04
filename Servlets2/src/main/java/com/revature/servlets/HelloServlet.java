package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 5797336179647213715L;
	private Logger log = Logger.getLogger(HelloServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		log.trace("Handling a GET Request");
		PrintWriter pw = resp.getWriter();
		pw.write("<html><head><title>Hello World</title></head><body>");
		pw.write("<h1>Hello</h1><h2>World</h2>"
				+ "<a href=\"index.html\">request a color</a></body></html>");
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		log.trace("Handling a POST Request");
		String color = null;
		color = req.getParameter("color");
		log.trace("Received color: "+color);
		resp.getWriter().write("<html><head><title>Hello World</title></head>"
				+ "<body style=\"background-color:"+color+"\">"
						+ "<a href=\"index.html\">Go Back</a>"
						+ "<br><a href=\"hello\">hello</a></body></html>");
		
	}
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Calls our do methods for us.
		log.trace("HelloServlet service()");
		super.service(req, resp);
	}
	@Override
	public void destroy() {
		// Closes servlet resources and preps for end of application.

		log.trace("HelloServlet destroy()");
		super.destroy();
	}
	@Override
	public void init(ServletConfig config) throws ServletException {
		// Do not override this method unless you call super.init(config);

		log.trace("HelloServlet init(config)");
		super.init(config);
	}
	@Override
	public void init() throws ServletException {
		// This method exists so that you CAN override init

		log.trace("HelloServlet init()");
		super.init();
	}
}
