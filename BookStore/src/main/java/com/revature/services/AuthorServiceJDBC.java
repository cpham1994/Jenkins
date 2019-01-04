package com.revature.services;

import java.util.Set;

import com.revature.beans.Author;
import com.revature.data.AuthorDAO;
import com.revature.data.BookAppDAOFactory;

public class AuthorServiceJDBC implements AuthorService {
	private BookAppDAOFactory bf = BookAppDAOFactory.getInstance();
	private AuthorDAO ad = bf.getAuthorDAO();
	
	@Override
	public Set<Author> getAuthors() {
		return ad.getAuthors();
	}

	@Override
	public Author getAuthorById(int i) {
		return ad.getAuthor(i);
	}

	@Override
	public void updateAuthor(Author a) {
		ad.updateAuthor(a);
	}

	@Override
	public void deleteAuthor(Author a) {
		ad.deleteAuthor(a);
	}

	@Override
	public int addAuthor(Author a) {
		return ad.addAuthor(a);
	}

}
