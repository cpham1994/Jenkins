package com.revature.test;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.rules.ExpectedException;

import com.revature.bankingapp.BankingApp;
import com.revature.beans.Account;
import com.revature.beans.Address;
import com.revature.beans.User;

import com.revature.data.AccountDao;
import com.revature.data.AccountFake;
import com.revature.data.UserDao;
import com.revature.data.UserFake;

public class BankingAppTest {
	private static final BankingApp myBank = new BankingApp(); 
	private static Account account = new Account(); 
	private static User user = new User(); 
	private static UserDao userDao = new UserFake(); 
	private static AccountDao accDao = new AccountFake(); 
	private static Address a = new Address();
	private static Logger Log = Logger.getLogger(BankingAppTest.class);
	private  AccountFake fake = new AccountFake();
	
	private static int id = 0; 

	
	@Test
	public void setup() {
		Log.trace("Setting up tests");
		user.setUsername("john");
		user.setPassword("smith");
		a.setId(1);
		account.setId(1);
		account.setBalance(500.00);
		account.setType("checking");
		account.setStatus(1);
		user.getAccounts().add(account);
		user.setAddress(a);
		id = account.getId(); 
	}
	
	
	@Test
	public void withdrawTest() {
		setup();
		Double expected = 450.00; 
		assertEquals(expected, BankingApp.withdraw(user, 50.00, id));
	}
	
	@Test
	public void depositTest() {
		setup(); 
		Double expected = 550.00; 
		assertEquals(expected, BankingApp.deposit(user, 50.00, id));

	}
	
	@Test
	public void saveAccountTest() {

		account.setBalance(50.00);
		account.setStatus(1);
		account.setType("checking");
		accDao.saveAccount(account);
		accDao.updateAccount(account);
		accDao.getAllAccounts();
		int id = account.getId();
		
		Double expectedBal = 50.00; 
		String expectedT = "checking";
		int expectedStat = 1;
		
		assertEquals(expectedBal, accDao.getAccount(id).getBalance());
		assertEquals(expectedT, accDao.getAccount(id).getType());
		assertEquals(expectedStat, accDao.getAccount(id).getStatus());

	}
	
	@Test
	public void saveUserTest() {
		Address a = new Address();
		a.setId(1);
		user.setName("kel");
		user.setPhone("4129492939");
		user.setAddress(a);
		user.setEmail("james@domain.com");
		user.setUsername("sfa");
		user.setPassword("asdf");
		
		
		userDao.saveUser(user);
		userDao.updateUser(user);
		userDao.getAllUsers();
		
		String expectedName = "kel";
		String expectedPhone = "4129492939";
		String expectedEmail = "james@domain.com";
		String expectedUser = "sfa";
		String expectedPW = "asdf";
		
		assertEquals(expectedName, userDao.getUser(user.getUsername()).getName());
		assertEquals(expectedPhone, userDao.getUser(user.getUsername()).getPhone());
		assertEquals(expectedEmail, userDao.getUser(user.getUsername()).getEmail());
		assertEquals(expectedUser, userDao.getUser(user.getUsername()).getUsername());
		assertEquals(expectedPW, userDao.getUser(user.getUsername()).getPassword());
	}
}
