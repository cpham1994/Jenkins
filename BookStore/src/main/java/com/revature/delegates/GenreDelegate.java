package com.revature.delegates;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.beans.Genre;
import com.revature.services.GenreService;
import com.revature.services.GenreServiceJDBC;
import com.revature.util.JsonParseUtil;

public class GenreDelegate implements FrontControllerDelegate {
	private Logger log = Logger.getLogger(GenreDelegate.class);
	private GenreService gs = new GenreServiceJDBC();
	private ObjectMapper om = new ObjectMapper();
	
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String path = (String) req.getAttribute("path");
		// BookStore/genre
		log.trace("Genre delegate has been called");
		if(path == null || "".equals(path)) {
			collectionOperations(req, resp);
		} else {
			genreTimes(req, resp, Integer.parseInt(path));
		}
	}
	private void collectionOperations(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// GET, POST
		switch(req.getMethod()) {
		case "GET":
			resp.getWriter().write(om.writeValueAsString(gs.getGenres()));
			break;
		case "POST":
			Genre g = null;
			
			g = JsonParseUtil.parseJsonInput(req.getInputStream(), Genre.class, resp);
			
			gs.addGenre(g);
			if(g.getId()==0) {
				resp.sendError(HttpServletResponse.SC_CONFLICT);
				return;
			}
			resp.setStatus(HttpServletResponse.SC_CREATED);
			resp.getWriter().write(om.writeValueAsString(g));
			break;
		default: resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return;
		}
	}
	private void genreTimes(HttpServletRequest req, HttpServletResponse resp, int genreId) throws IOException {
		// GET, PUT, DELETE
		Genre g = gs.getGenreById(genreId);
		switch(req.getMethod()) {
		case "GET": 
			resp.getWriter().write(om.writeValueAsString(g));
			break;
		case "PUT":
			g = JsonParseUtil.parseJsonInput(req.getInputStream(), Genre.class, resp);
			gs.updateGenre(g);
			break;
		case "DELETE": 
			gs.deleteGenre(g);
			break;
		default: resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED); return;
		}
		
	}

}
