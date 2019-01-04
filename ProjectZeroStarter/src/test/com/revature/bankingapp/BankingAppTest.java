package com.revature.bankingapp;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revature.beans.Account;
import com.revature.beans.User;

public class BankingAppTest {
	private static Logger log = null;
	AccountDao accDao = null; 
	serDao userDao = null;
	User user = null;
	Account account = null;
	
	//before the tests
	@BeforeClass
	public static void setup2() {
		log = Logger.getLogger(BankingAppTest.class);
		log.trace("before tests run");
	}
	//before each test
	@Before
	public void setup() {
		log.trace("Setting up tests");
		accDao = new AccountFake();
		userDao = new UserFake();
		user = new User();
		account = new Account();
		
		user.setUsername("john");
		user.setPassword("smith");
		
		account.setStatus(true);
		
		account.setType("checking");
		accId = rand.nextInt(100) + 1; 
		account.setId(accId);
		accDao.saveAccount(account);
		
		List<Account> accounts = user.getAccounts(); 
		
		if(accounts == null) {
			accounts = new ArrayList<Account>();
		}
		accounts.add(account);
		user.setAccounts(accounts);
		
		accountDao.getAccount(accID).setStatus(true));
	}
	//after each test
	@After
	public void teardown() {
		log.trace("tearing down the test (closing resources and such)");
	}
	@AfterClass
	public static void tearAll() {
		log.trace("tearing everything down");
	}
	@Test
	public void testWithdraw() {
		log.trace("Testing over withdraw");
		assertEquals();
		
	}
	@Test
	public void testDeposit() {

	}
}
