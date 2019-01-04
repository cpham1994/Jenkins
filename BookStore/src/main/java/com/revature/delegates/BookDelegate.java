package com.revature.delegates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.beans.Book;
import com.revature.services.BookService;
import com.revature.services.BookServiceJDBC;
import com.revature.util.JsonParseUtil;
import com.revature.util.LogUtil;

public class BookDelegate implements FrontControllerDelegate {
	private Logger log = Logger.getLogger(BookDelegate.class);
	private ObjectMapper om = new ObjectMapper();
	private BookService bs = new BookServiceJDBC();
	
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String path = (String) req.getAttribute("path");
		log.trace(path);
		if(path==null||"".equals(path)) {
			collectionOperations(req,resp);
		} else {
			bookTimes(req, resp, Integer.parseInt(path.toString()));
		}
	}

	private void collectionOperations(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		switch(req.getMethod()) {
		case "GET": 
			log.trace("Retrieve all book from database");
			Set<Book> books = bs.getBooks();
			log.trace(om.writeValueAsString(books));
			resp.getWriter().write(om.writeValueAsString(books));
			return;
		case "POST":
			log.trace("Post called with book");
			Book b = JsonParseUtil.parseJsonInput(req.getInputStream(), Book.class, resp);
			log.trace(b);
			try {
				log.trace("adding book to database");
				bs.addBook(b);
				log.trace(b);
				resp.setStatus(HttpServletResponse.SC_CREATED);
				resp.getWriter().write(om.writeValueAsString(b));
			} catch(Exception e) {
				LogUtil.logException(e, BookDelegate.class);
				resp.sendError(HttpServletResponse.SC_CONFLICT);
			}
			return;
		default:
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private void bookTimes(HttpServletRequest req, HttpServletResponse resp, int bookId) throws IOException {
		log.trace("Operating on a specific book with id: "+bookId);
		PrintWriter writer = resp.getWriter();
		Book b;
		BufferedReader bf;
		StringBuilder sb;
		switch(req.getMethod()) {
		case "GET":
			b = bs.getBookById(bookId);
			resp.getWriter().write(om.writeValueAsString(b));
			return;
		case "PUT":
			// Update the book in the database
			
			b = JsonParseUtil.parseJsonInput(req.getInputStream(), Book.class, resp);
			bs.updateBook(b);
			writer.write(om.writeValueAsString(b));
			return;
		case "DELETE":
			bf = req.getReader();
			sb = new StringBuilder();
			while(bf.ready()) {
				sb.append(bf.readLine());
			}
			b = om.readValue(sb.toString(), Book.class);
			bs.deleteBook(b);
			resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
			return;
		default:
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

}
