package com.revature.data;

import java.util.Set;

import com.revature.beans.Book;
import com.revature.beans.Genre;

public interface GenreDAO {
	// Data Access Object
	// EAch bean has an object dedicated to
	// accessing the database on its behalf.
	
	// Usually a DAO is going to have CRUD methods.
	
	// Create - Insert
	Integer addGenre(Genre g);
	// Read - Select
	Genre getGenre(Integer id);
	Genre getGenreByGenre(String genre);
	Set<Genre> getGenres();
	Set<Genre> getGenresByBook(Book b);
	// Update - Update
	void updateGenre(Genre g);
	// Delete - Delete
	void deleteGenre(Genre g);
	
}
