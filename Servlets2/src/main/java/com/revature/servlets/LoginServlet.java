package com.revature.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;
import org.apache.log4j.Logger;

import com.revature.beans.User;
import com.revature.data.UserDAO;
import com.revature.data.UserOracle;

public class LoginServlet extends HttpServlet{
	private static Logger log = Logger.getLogger(LoginServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//resp.sendRedirect("login.html");
		HttpSession s = req.getSession();
		if(s.getAttribute("user")==null)
			req.getRequestDispatcher("login.html").forward(req, resp);
		else
			resp.sendRedirect("home");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String username = req.getParameter("user");
		String password = req.getParameter("pass");
		UserDAO ud = new UserOracle();
		
		log.trace("user attempting to login with "
				+ "username: "+username+" and password: "+password);
		User u = ud.getUser(username, password);
		log.trace("logged in as user: "+u);
		if(u==null) {
			resp.sendRedirect("login");
		} else {
			HttpSession s = req.getSession();
			s.setAttribute("user", u);
			resp.sendRedirect("home");
		}
	}
}
