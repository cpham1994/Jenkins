package com.revature.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.beans.Bean;

public class BeanServlet extends HttpServlet{
	private static Logger log = Logger.getLogger(BeanServlet.class);
	private static ObjectMapper om = new ObjectMapper();
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws IOException, ServletException {
		Bean b = new Bean("Richard", "Pinto", 1, 2000, false);
		String beanString = om.writeValueAsString(b);
		log.trace(beanString);
		resp.getWriter().write(beanString);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		Bean b = om.readValue(req.getInputStream(), Bean.class);
		log.trace(b);
	}
}
