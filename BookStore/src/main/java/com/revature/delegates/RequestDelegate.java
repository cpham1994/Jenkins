package com.revature.delegates;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class RequestDelegate implements FrontControllerDelegate {
	private Logger log = Logger.getLogger(RequestDelegate.class);
	private DelegateFactory df = DelegateFactory.getInstance();
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		log.trace("Calling our requestDelegate: "+req.getRequestURI());
		addCorsHeader(req.getRequestURI(), resp);
		if("OPTIONS".equals(req.getMethod())) {
			return;
		}
		// Input: "BookStore/books"
		// Input: "BookStore/books/1"
		// Input: "BookStore/books/"
		StringBuilder switchString = new StringBuilder(req.getRequestURI());
		// Remove BookStore/ from the URI
		switchString.replace(0, req.getContextPath().length()+1, "");
		/*
		 * "books"
		 * "books/1"
		 * "books/"
		 */
		// We want to get the first part of the string ending with /
		if(switchString.indexOf("/")!=-1) {
			// save our remnants for later
			req.setAttribute("path", switchString.substring(switchString.indexOf("/")+1));
			switchString.replace(switchString.indexOf("/"), switchString.length(), "");
		}
		
		// SwitchString: books	"path": null
		// SwitchString: books	"path": 1
		// SwitchString: books	"path": ""
		
		switch(switchString.toString()) {
		case "getBooks":
			req.getRequestDispatcher("/static/books.html").forward(req, resp);
			return;
		case "addBook":
			req.getRequestDispatcher("/static/addbook.html").forward(req, resp);
			return;
		case "editBook":
			req.getRequestDispatcher("/static/editbook.html").forward(req, resp);
			return;
		default: break;
		}
		
		FrontControllerDelegate delegate = df.getDelegate(switchString.toString());
		if(delegate==null) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			req.getRequestDispatcher("/static/404.html").forward(req, resp);
			return;
		}
		delegate.process(req, resp);		
	}

	private void addCorsHeader(String requestURI, HttpServletResponse response) {
		log.trace("adding headers");
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		response.addHeader("Vary", "Origin");
		//if I don't care about getting my cookie, this will work
		//response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Max-Age", "1728000");
        response.addHeader("Produces", "application/json");
	}

}
