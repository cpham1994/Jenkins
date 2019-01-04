package com.revature.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlets.DefaultServlet;
import org.apache.log4j.Logger;

import com.revature.delegates.FrontControllerDelegate;
import com.revature.delegates.RequestDelegate;

public class FrontController extends DefaultServlet {
	private static final long serialVersionUID = 4807799638038773525L;

	private Logger log = Logger.getLogger(FrontController.class);
	private FrontControllerDelegate delegate = new RequestDelegate();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		log.trace(req.getRequestURI());
		if(req.getRequestURI()
				.substring(req.getContextPath().length())
				.startsWith("/static")) {
			log.trace("static content");
			super.doGet(req, resp);
		} else {
			log.trace("non-static content");
			delegate.process(req, resp);
		}
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		this.doGet(request, response);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
