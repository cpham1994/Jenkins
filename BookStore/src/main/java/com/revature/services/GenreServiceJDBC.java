package com.revature.services;

import java.util.Set;

import com.revature.beans.Genre;
import com.revature.data.BookAppDAOFactory;
import com.revature.data.GenreDAO;

public class GenreServiceJDBC implements GenreService {
	private BookAppDAOFactory bf = BookAppDAOFactory.getInstance();
	private GenreDAO gd = bf.getGenreDAO();
	
	@Override
	public Set<Genre> getGenres() {
		return gd.getGenres();
	}
	@Override
	public Genre getGenreById(int i) {
		return gd.getGenre(i);
	}
	@Override
	public void updateGenre(Genre g) {
		gd.updateGenre(g);
	}
	@Override
	public void deleteGenre(Genre g) {
		gd.deleteGenre(g);
	}
	@Override
	public void addGenre(Genre g) {
		gd.addGenre(g);
	}
}
