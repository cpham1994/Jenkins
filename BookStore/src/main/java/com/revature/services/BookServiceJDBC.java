package com.revature.services;

import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.beans.Author;
import com.revature.beans.Book;
import com.revature.beans.Customer;
import com.revature.beans.Genre;
import com.revature.data.AuthorDAO;
import com.revature.data.BookAppDAOFactory;
import com.revature.data.BookDAO;
import com.revature.data.GenreDAO;

public class BookServiceJDBC implements BookService {
	private Logger log = Logger.getLogger(BookServiceJDBC.class);
	private BookAppDAOFactory bf = BookAppDAOFactory.getInstance();
	private BookDAO bd = bf.getBookDAO();
	private AuthorDAO ad = bf.getAuthorDAO();
	private GenreDAO gd = bf.getGenreDAO();

	@Override
	public Set<Book> getBooksForReadingList(Customer cust) {
		Set<Book> bookSet = bd.getReadingList(cust);
		for(Book b : bookSet) {
			b.setAuthors(ad.getAuthorsByBook(b));
			b.setGenres(gd.getGenresByBook(b));
		}
		return bookSet;
	}

	@Override
	public Book getBookById(int i) {
		Book b = bd.getBook(i);
		b.setAuthors(ad.getAuthorsByBook(b));
		b.setGenres(gd.getGenresByBook(b));
		return b;
	}

	@Override
	public void updateBook(Book b) {
		// check: are the authors in the db?
		Set<Author> authors = b.getAuthors();

		for (Author a : authors) {
			if (ad.getAuthor(a.getId()) != null) {
				ad.updateAuthor(a);
			} else {
				ad.addAuthor(a);
			}
		}

		// check: are the genres in the db?
		Set<Genre> genres = b.getGenres();

		for (Genre g : genres) {
			if (gd.getGenre(g.getId()) != null) {
				gd.updateGenre(g);
			} else {
				gd.addGenre(g);
			}
		}

		// update the book
		bd.updateBook(b);

	}

	@Override
	public void deleteBook(Book b) {
		bd.deleteBook(b);
	}

	@Override
	public void addBook(Book b) {
		// Check to see if Authors are in db
		if (b.getAuthors() != null) {
			Set<Author> authors = b.getAuthors();
			for (Author a : authors) {
				if (ad.getAuthor(a.getId()) != null)
					ad.updateAuthor(a);
				else
					ad.addAuthor(a);
			}
		}
		log.trace("authors added");
		// check to see if genres are in db.
		if (b.getGenres() != null) {
			Set<Genre> genres = b.getGenres();
			for (Genre g : genres) {
				if (gd.getGenre(g.getId()) != null) {
					gd.updateGenre(g);
				} else {
					gd.addGenre(g);
				}
			}
		}
		log.trace("genres added");
		// add the book
		bd.addBook(b);
	}

	@Override
	public Set<Book> getBooks() {
		Set<Book> bookSet = bd.getBooks();
		for(Book b : bookSet) {
			b.setAuthors(ad.getAuthorsByBook(b));
			b.setGenres(gd.getGenresByBook(b));
		}
		return bookSet;
	}
}
