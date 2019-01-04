package com.revature.data;

public class BookAppDAOFactory {
	private static BookAppDAOFactory bf = new BookAppDAOFactory();
	private static final String TYPE = "Oracle";
	private BookAppDAOFactory() {
		super();
	}
	public static synchronized BookAppDAOFactory getInstance() {
		if(bf==null) {
			bf = new BookAppDAOFactory();
		}
		return bf;
	}
	public AuthorDAO getAuthorDAO() {
		switch(TYPE) {
		case "Oracle": return new AuthorOracle();
		default: throw new RuntimeException("Could not determine DAO type");
		}
	}
	public GenreDAO getGenreDAO() {
		switch(TYPE) {
		case "Oracle": return new GenreOracle();
		default: throw new RuntimeException("Could not determine DAO type");
		}
	}
	public BookDAO getBookDAO() {
		switch(TYPE) {
		case "Oracle": return new BookOracle();
		default: throw new RuntimeException("Could not determine DAO type");
		}
	}
	public AddressDAO getAddressDAO() {
		switch(TYPE) {
		case "Oracle": return new AddressOracle();
		default: throw new RuntimeException("Could not determine DAO type");
		}
	}
	public UserDAO getUserDAO() {
		switch(TYPE) {
		case "Oracle": return new UserOracle();
		default: throw new RuntimeException("Could not determine DAO type");
		}
	}
	public EmployeeDAO getEmployeeDAO() {
		switch(TYPE) {
		case "Oracle": return new EmployeeOracle();
		default: throw new RuntimeException("Could not determine DAO type");
		}
	}
	public CustomerDAO getCustomerDAO() {
		switch(TYPE) {
		case "Oracle": return new CustomerOracle();
		default: throw new RuntimeException("Could not determine DAO type");
		}
	}
	public PurchaseDAO getPurchaseDAO() {
		switch(TYPE) {
		case "Oracle": return new PurchaseOracle();
		default: throw new RuntimeException("Could not determine DAO type");
		}
	}
}
