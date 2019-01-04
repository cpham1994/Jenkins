package com.revature.services;

import java.util.Set;

import org.apache.log4j.Logger;

import com.revature.beans.Customer;
import com.revature.beans.User;
import com.revature.data.AddressDAO;
import com.revature.data.BookAppDAOFactory;
import com.revature.data.CustomerDAO;
import com.revature.data.UserDAO;

public class CustomerServiceJDBC implements CustomerService {
	private Logger log = Logger.getLogger(CustomerServiceJDBC.class);
	private BookAppDAOFactory bf = BookAppDAOFactory.getInstance();
	private UserDAO ud = bf.getUserDAO();
	private CustomerDAO cd = bf.getCustomerDAO();
	private AddressDAO addrdao = bf.getAddressDAO();
	private BookService bs = new BookServiceJDBC();
	
	@Override
	public Customer getCustomer(String username, String password) {
		Customer cust = new Customer(0, username, password);
		cust = (Customer) ud.getUser(cust);
		cust = cd.getCustomer(cust);
		if(cust.getId()==0) {
			log.warn("No customer found");
			return null;
		}
		cust.setAddress(addrdao.getAddress(cust.getAddress().getId()));
		cust.setReadingList(bs.getBooksForReadingList(cust));
		return cust;
	}

	@Override
	public Customer getCustomerById(int i) {
		Customer cust = new Customer(i);
		cust = (Customer) ud.getUser(cust);
		if(cust.getId()==0) {
			log.trace("No customer found");
			return null;
		}
		cust.setAddress(addrdao.getAddress(cust.getAddress().getId()));
		cust.setReadingList(bs.getBooksForReadingList(cust));
		return cust;
	}

	@Override
	public Set<Customer> getCustomers() {
		Set<Customer> custList = cd.getCustomers();
		for(Customer c : custList) {
			ud.getUserById(c);
			c.setAddress(addrdao.getAddress(c.getAddress().getId()));
			c.setReadingList(bs.getBooksForReadingList(c));
		}
		return custList;
	}

	@Override
	public void deleteCustomer(Customer cust) {
		cd.deleteCustomer(cust);
	}

	@Override
	public void updateCustomer(Customer cust) {
		ud.updateUser(cust);
		addrdao.updateAddress(cust.getAddress());
		cd.updateCustomer(cust);
	}

	@Override
	public void addCustomer(Customer cust) {
		User u = ud.getUser(cust.getUsername(), cust.getPassword());
		if(u == null) {
			ud.addUser(cust);
		}
		cust.getAddress().setId(addrdao.addAddress(cust.getAddress()));
		cd.addCustomer(cust);
	}

}
