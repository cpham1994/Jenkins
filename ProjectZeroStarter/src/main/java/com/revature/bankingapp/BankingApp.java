package com.revature.bankingapp;


  
//Imports

import org.apache.log4j.Logger;

import com.revature.beans.Account;
import com.revature.beans.Address;
import com.revature.beans.User;
import com.revature.data.AccountDao;
import com.revature.data.AccountFake;
import com.revature.data.UserDao;
import com.revature.data.UserFake;
import com.revature.util.ConnectionUtil;
import com.revature.util.LogUtil;

import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.*;

public class BankingApp{
	private static ConnectionUtil cu = ConnectionUtil.getInstance();
	private static Logger log = Logger.getLogger(BankingApp.class);
	static UserDao userDao = new UserFake(); 
	static AccountDao acDao = new AccountFake(); 
	
	public static void main(String[] args) {		
		int selection = 0;

		userDao.getAllUsers();
		acDao.getAllAccounts();
		
		for(int i = 0; i < userDao.getUsers().size(); i++) {
			User use = userDao.getUsers().get(i);
			populateUserAccounts(userDao.getUser(use.getUsername()));
			
		}
 
		
		while(true) {	
			try{ 
				
				selection = mainMenu();
				log.trace("Going to main menu");

			}catch(Exception e ) {
				//TODO error handle
				log.trace("Could not get to main menu", e);
				mainMenu();
			}
			
			switch(selection) {
				case 1:
					log.trace("Logging In");
					logIn();
					break;
					
				case 2: 
					log.trace("Registering for Account");
					registerAccount();
					break;
				default:
					log.trace("Could not get correct input");
					System.out.println("Try again, please input 1: Log In or 2: Register Account"); 
					break;
			}
		}
	}
	
	private static int mainMenu() {
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to Phamly Bank");
		System.out.println("1.Log In\n2.Apply for an account");
		
		int selection = input.nextInt();
		return selection;
	}
	
	private static void logIn() {
		userDao.getAllUsers();
		acDao.getAllAccounts();
		System.out.println();
		User u = null;
		Address add = null;
	
		String userlogin = "", pswdlogin = ""; 
		Scanner input = new Scanner(System.in);
		System.out.println("1. Customer 2. Employee 3. Administator 4. Log out");
		
		String select;
		int selection = 0;
		try { 
			select = input.nextLine();
			if(select.matches("[1-3]")){
				selection = Integer.parseInt(select);
				System.out.print("Username: ");
				userlogin = input.nextLine();
				
				System.out.print("Password: ");
				pswdlogin = input.nextLine();
				
			}else if(select.matches("4")) {
				mainMenu(); 
			}
			else {
				logIn();
			}

		}catch(Exception e ) {
			logIn();
			log.warn("User must enter a valid input");
			return;
		}
		
		if(userDao.login(userlogin, pswdlogin) != null) {
			log.trace("Login was successful");
			System.out.println("Welcome: " + userDao.getUser(userlogin).getName());
			switch( selection ) {
			
				case 1:	//Customer
					log.trace("User logged in as customer");
					System.out.println("1. Get Balance\n2. Withdraw\n3. Deposit\n4.Transfer\n5. Apply for new account\n6. Log Out");
					String choice = input.nextLine();
					int cusSelection = Integer.parseInt(choice); 
					if(cusSelection == 1) {
						log.trace("User getting balance");
						getBalance(userDao.getUser(userlogin));
					}else if(cusSelection == 2) {
						log.trace("User withdrawing");
						Double amt = 0.0;
						System.out.println("Here are your accounts: " + userDao.getUser(userlogin).getAccounts());
						System.out.println("What account id would you like to withdraw from?" );
						String id = input.nextLine();
						int accId = Integer.parseInt(id);

						System.out.println("How much would you like to withdraw?");
						amt = input.nextDouble(); 
					
						while(amt <= 0) {
							System.out.println("You must imput a positive number. \nHow much would you like to withdraw?");
							amt = input.nextDouble(); 
						}
						
						while(amt > acDao.getAccount(accId).getBalance()) {
							System.out.println("You cannot overwithdraw. \nHow much would you like to withdraw?");
							amt = input.nextDouble(); 
						}
						
						withdraw(userDao.getUser(userlogin), amt, accId);
						
					}else if(cusSelection == 3) {
						log.trace("User depositing");
						Double amt = 0.0;
						System.out.println("Here are your accounts: " + userDao.getUser(userlogin).getAccounts());
						System.out.println("What account id would you like to deposit from?" );
						String id = input.nextLine();
						int accId = Integer.parseInt(id);
						
						System.out.println("How much would you like to Deposit?");
						amt = input.nextDouble(); 
						
						while(amt <= 0) {
							System.out.println("You must imput a positive number. \nHow much would you like to withdraw?");
							amt = input.nextDouble(); 
						}
						
						

						deposit(userDao.getUser(userlogin), amt, accId);
						
					}else if(cusSelection == 4) {
						log.trace("User trying transfer");
						
						System.out.println("Here are your accounts: " + userDao.getUser(userlogin).getAccounts());
						System.out.println("What accounts would you like to transfer?");
						String acc1 = input.nextLine();
						String acc2 = input.nextLine();
						
						int acc1ID = Integer.parseInt(acc1);
						int acc2ID = Integer.parseInt(acc2); 
						
						System.out.println("How would you like to transfer?\n1. Transfer " + acc1ID + " to " + acc2ID +"\n2. Transfer " + acc2ID + " to " + acc1ID);
						String transferSelection = input.nextLine();
						int transChoice = Integer.parseInt(transferSelection); 
						
						System.out.println("How much would you like to transfer?");
						String transferAMT = input.nextLine(); 
						Double transAMT = Double.parseDouble(transferAMT); 
						
						while(transAMT <= 0) {
							log.warn("User input a negative number");
							System.out.println("You must imput a positive number. \nHow much would you like to withdraw?");
							transferAMT = input.nextLine(); 
							transAMT = Double.parseDouble(transferAMT); 
						}
						
						while(transAMT > acDao.getAccount(acc1ID).getBalance() || transAMT > acDao.getAccount(acc2ID).getBalance()) {
							log.warn("One Account doesnt have sufficient funds");
							System.out.println("You cannot overwithdraw. \nHow much would you like to withdraw?");
							transferAMT = input.nextLine(); 
							transAMT = Double.parseDouble(transferAMT); 
						}
						
						
						transfer(userDao.getUser(userlogin), transAMT, acc1ID, acc2ID, transChoice);
					}else if(cusSelection == 5) {
						log.trace("User trying to apply for new account");
						applyAccount(userDao.getUser(userlogin));
					}else if(cusSelection == 6) {
						log.trace("User has logged out");
						System.out.println("You have been logged out");
						mainMenu();
					}
					
					break;
					
				case 2:	//Employee 
					log.trace("User logged in as an employee");
					System.out.println("Here are all accounts: ");
					showUserIDs();
					System.out.println("1. Get Account Balances\n2. Get Account Information\n3. Get User Personal Information\n4. Log out");
					String eS = input.nextLine(); 
					
					int emSelection = Integer.parseInt(eS); 
					if(emSelection == 1) {
						log.trace("Employee getting account balance");
						System.out.println("What is the account id?");
						int accID = input.nextInt(); 
						
						PLSQLAccounts(accID);
						
					}else if(emSelection == 2) {
						log.trace("Employee getting Account information");
						System.out.println("What is the user id?");
						int accID = input.nextInt(); 
						PLSQLAccounts(accID);
						
					}else if(emSelection == 3) {
						log.trace("Employee getting personal customer information");
						System.out.println("What is the customer user id?");
						int useID = input.nextInt();
						PLSQLPersonal(useID);
					}else if(emSelection == 4) {
						log.trace("Employee has logged out");
						System.out.println("You have been logged out");
						mainMenu();
					}
					break;
					
				case 3:	//Administrator
					log.trace("User logged in as Admin");
					System.out.println("Here are all accounts: ");
					showUserIDs();
					System.out.println("1. Approving Accounts\n2. Get Balance\n3. Withdraw\n4. Deposit\n5.Transfer\n6. Cancel Accounts\n7.Log Out");
					String aS = input.nextLine(); 
					int adSelection = Integer.parseInt(aS); 
					if(adSelection == 1) {
						//Approving Accounts
						log.trace("Admin approving accounts");
						System.out.println("Here are the accounts: " + acDao.getAccounts().toString());
						System.out.println("What is the account you would like to approve or deny?");
						String aID = input.nextLine();
						int accID =  Integer.parseInt(aID); 
						System.out.println("1: Approve\n2: Deny");
						String adChoice = input.nextLine(); 
						int adminChoice = Integer.parseInt(adChoice);
						if(adminChoice == 1) {
							acDao.getAccount(accID).setStatus(1);
							acDao.updateAccount(acDao.getAccount(accID));
							userDao.getAllUsers();
							acDao.getAllAccounts();
						}else if(adminChoice == 2) {
							acDao.getAccount(accID).setStatus(0);
							acDao.deleteAccount(acDao.getAccount(accID));
						}
						
						System.out.println("Here are the accounts: " + acDao.getAccounts().toString());
					}else if(adSelection == 2) {
						log.trace("Admin getting account balances");
						System.out.println("What is the account id?");
						int accID = input.nextInt(); 
						System.out.println("Balance: " + acDao.getAccount(accID).getBalance());
						
					}else if(adSelection == 3) {
						//Withdraw
						log.trace("Admin withdrawing");
						System.out.println("What is the account id?");
						String aID = input.nextLine();
						int accID =  Integer.parseInt(aID); 
						System.out.println("How much would you like to withdraw?");
						String acWithdraw = input.nextLine();
						Double accWithdraw = Double.parseDouble(acWithdraw); 
						double newBal = 0.0;
						newBal = acDao.getAccount(accID).getBalance() - accWithdraw;
						acDao.getAccount(accID).setBalance(newBal);
						acDao.updateAccount(acDao.getAccount(accID));
						userDao.getAllUsers();
						acDao.getAllAccounts();
						System.out.println("You withdrawed $" + accWithdraw + " from the account " + aID +  "\nBalance: $" + acDao.getAccount(accID).getBalance());

					}else if(adSelection == 4) {
						//Deposit
						log.trace("Admin depositing");
						System.out.println("What is the account id?");
						String aID = input.nextLine();
						int accID =  Integer.parseInt(aID); 
						System.out.println("How much would you like to deposit?");
						String acDeposit = input.nextLine();
						Double accDeposit = Double.parseDouble(acDeposit); 
						double newBal = 0.0;
						newBal = acDao.getAccount(accID).getBalance() + accDeposit;	
						acDao.getAccount(accID).setBalance(newBal);
						acDao.updateAccount(acDao.getAccount(accID));
						userDao.getAllUsers();
						acDao.getAllAccounts();
						System.out.println("You withdrawed $" + accDeposit + " from the account" + aID +  "\nBalance: $" + acDao.getAccount(accID).getBalance());

					}else if(adSelection == 5) {
						//Transfer
						log.trace("Admin transfering");
						System.out.println("What accounts would you like to transfer?");
						String acc1 = input.nextLine();
						String acc2 = input.nextLine();
						
						int acc1ID = Integer.parseInt(acc1);
						int acc2ID = Integer.parseInt(acc2); 
						
						System.out.println("How would you like to transfer?\n1. Transfer " + acc1ID + " to " + acc2ID +"\n2. Transfer " + acc2ID + " to " + acc1ID);
						String transferSelection = input.nextLine();
						int transChoice = Integer.parseInt(transferSelection); 
						if(transChoice == 1) {
							System.out.println("How much would you like to transfer?");
							String transferAMT = input.nextLine(); 
							Double transAMT = Double.parseDouble(transferAMT); 
							
							Double newBalance1 = 0.0, newBalance2 = 0.0; 
							
							newBalance1 = acDao.getAccount(acc1ID).getBalance() - transAMT; 
							acDao.getAccount(acc1ID).setBalance(newBalance1);
							
							newBalance2 = acDao.getAccount(acc2ID).getBalance() + transAMT; 
							acDao.getAccount(acc2ID).setBalance(newBalance2);
							
							acDao.getAccount(acc1ID).setBalance(newBalance1);
							acDao.getAccount(acc2ID).setBalance(newBalance2);
							
							acDao.updateAccount(acDao.getAccount(acc1ID));
							userDao.getAllUsers();
							acDao.getAllAccounts();
							
							acDao.updateAccount(acDao.getAccount(acc2ID));
							userDao.getAllUsers();
							acDao.getAllAccounts();
							
							System.out.println("You have successfully transfered funds");
						} else if(transChoice == 2) {
							System.out.println("How much would you like to transfer?");
							String transferAMT = input.nextLine(); 
							Double transAMT = Double.parseDouble(transferAMT); 
							
							Double newBalance1 = 0.0, newBalance2 = 0.0; 
							
							newBalance1 = acDao.getAccount(acc2ID).getBalance() - transAMT; 
							acDao.getAccount(acc2ID).setBalance(newBalance1);
							
							newBalance2 = acDao.getAccount(acc1ID).getBalance() + transAMT; 
							acDao.getAccount(acc1ID).setBalance(newBalance2);
							
							acDao.getAccount(acc1ID).setBalance(newBalance1);
							acDao.getAccount(acc2ID).setBalance(newBalance2);
							
							acDao.updateAccount(acDao.getAccount(acc1ID));
							userDao.getAllUsers();
							acDao.getAllAccounts();
							
							acDao.updateAccount(acDao.getAccount(acc2ID));
							userDao.getAllUsers();
							acDao.getAllAccounts();
							
							System.out.println("You have successfully transfered funds");
						}
					}else if(adSelection == 6) {
						//Cancel Account
						log.trace("Admin cancelling an account"); 
						System.out.println("What is the accound id?");
						String aID = input.nextLine();
						int accID =  Integer.parseInt(aID); 
						
						Account a = acDao.getAccount(accID);
						
						acDao.deleteAccount(a);
						System.out.println("Account has been successfully canceled");
					}else if(adSelection == 7) {
						System.out.println("You have been logged out");
						mainMenu();
					}
					
					break;
					
				case 4: //Log out
					log.trace("Admin logging out");
					mainMenu();
					
				default:
					System.out.println("Must enter: 1. Customer 2. Employee 3. Administator, Please log in again");
					logIn();
					log.warn("User must enter a valid input");
					
					return;
			}
		} else {
			log.trace("Login was not successful");
			System.out.println("Username or password is incorrect, please re-login");
			logIn();
		}
		
	}

	private static void registerAccount() {
		UserDao userDao = new UserFake();	
		
		AccountDao accDao = new AccountFake(); 
		
		Scanner input = new Scanner(System.in);
		User user = new User(); 
		Address address = new Address();
	

		//name email, phone, address, username, password
		//street1 street2 city state zip
		System.out.println("Please enter the following information:");
		System.out.print("Name: ");
		String name = input.nextLine();
		user.setName(name);
		
		System.out.print("Phone: ");
		String phone = input.nextLine(); 
		user.setPhone(phone);
		
		System.out.print("Email Address: ");
		String email = input.nextLine(); 
		user.setEmail(email);	
		
		System.out.print("Street 1: ");
		String street1 = input.nextLine();  
		address.setStreet1(street1);
		
		System.out.print("Street 2: ");
		String street2 = input.nextLine(); 
		address.setStreet2(street2);
		
		System.out.print("City: ");
		String city = input.nextLine(); 
		address.setCity(city);
		
		System.out.print("State: ");
		String state = input.nextLine(); 
		address.setState(state);
		
		System.out.print("Zip-Code: ");
		String zip = input.nextLine(); 
		address.setZip(zip);
		
		System.out.println("Welcome to the Phamly\nNow register your user login!");
		System.out.print("Username: ");
		String username = input.nextLine(); 
		user.setUsername(username);
		System.out.print("Password: ");
		String password = input.nextLine();  
		user.setPassword(password);
		
		address.setId(1);
		user.setAddress(address);
		
		userDao.saveUser(user);
		userDao.updateUser(user);
		System.out.println("user " + user.toString());
		System.out.println("address " + user.getAddress().toString());
		System.out.println("Now login and apply for an account!");
	}

	private static void saveAddress(User user, Address address) {
		
		
		log.trace("Adding address to database");
	
		log.trace("trying connection");

		try(Connection conn = cu.getConnection()){
			int key = 0; 
			log.trace("connection successful");
			conn.setAutoCommit(false);
			String sql = "insert into address (lineone, linetwo, city, state, zip) values(?, ?, ?, ?, ?)"; 
			String [] keys = {"addressid"};
			log.trace("connection successful");
			PreparedStatement stmt = conn.prepareStatement(sql, keys);
			log.trace("connection successful");
			stmt.setString(1, address.getStreet1());
			log.trace("connection successful");
			stmt.setString(2, address.getStreet2());
			log.trace("connection successful");
			stmt.setString(3, address.getCity());
			log.trace("connection successful");
			stmt.setString(4, address.getState());
			log.trace("connection successful");
			stmt.setString(5, address.getZip());

			stmt.executeUpdate();

			ResultSet rs = stmt.executeQuery();
			log.trace("resultset done");
			if(rs.next()){ //needs to be called
				log.trace("trying key");
				key = rs.getInt(1); 
				log.trace("key set");
				address.setId(key); 
				log.trace("key done");
				conn.commit();
				log.trace("address created and committed");
			}else {
				log.trace("address not created");
				conn.rollback();
			}
		}catch(SQLException e) {
			LogUtil.logException(e, BankingApp.class);
		}
		log.trace("connection successful");
		user.setAddress(address);
		
	}
	
	public static void applyAccount(User user) {
		Scanner input = new Scanner(System.in);
		Account account = new Account(); 

		System.out.println("What type of account would you like?\n1. Checking\n2. Savings\n3. Joint");
		String acc = input.nextLine();
		int accTypeSelection = Integer.parseInt(acc);
		int accId = 0; 
		
		switch (accTypeSelection) {
			case 1: 
				account.setType("checking");
				account.setBalance(0.0);
				acDao.saveAccount(account);
				break;
			case 2: 
				account.setType("savings");
				account.setBalance(0.0);
				acDao.saveAccount(account);
				break;
			case 3: 
				System.out.println("Here are the accounts: " + acDao.getAccounts().toString());
				account.setType("joint");
				System.out.println("Account to join with: ");
				Integer id = input.nextInt();
				acDao.getAccount(id);
				userDao.getUsers().add(user);
				break;
			default:
				log.warn("User must type either checking, savings, joint");
				System.out.println("Please Refill form");
				applyAccount(user);
		}
		
		List<Account> accounts = user.getAccounts(); 
		
		if(accounts == null) {
			accounts = new ArrayList<Account>();
		}
		
		accounts.add(account);
		user.setAccounts(accounts);
		userDao.saveUser(user);
		userDao.updateUser(user);
		acDao.saveAccount(account);
		acDao.updateAccount(account);
		System.out.println("Here are your accounts: " + user.getAccounts().toString());
		System.out.println("Please have admin log in to approve your account");
	}
	
	public static void getBalance(User user) {
		System.out.println("Accounts: "+ user.getAccounts().toString());
		for(int i = 0; i < user.getAccounts().size(); i++) {
			Account account = user.getAccounts().get(i);
			System.out.println("Account ID: " + account.getId() + "\nAccount Type: " + account.getType() + " Balance: $" + account.getBalance());
		}
	}
	
	public static Double withdraw(User user, Double amt, int id) {
		Scanner input = new Scanner(System.in);
		Double  newBalance = 0.0;  
		
		
		
		for(int i = 0; i< user.getAccounts().size(); i++) {
			Account account = user.getAccounts().get(i);

			if(account.getId() == id) {
				newBalance = account.getBalance() - amt; 
				account.setBalance(newBalance);
				System.out.println(account.getBalance() + " " + account.getId() + " " + account.getStatus()+ " " + account.getType());
				acDao.updateAccount(account);
				userDao.getAllUsers();
				acDao.getAllAccounts();
				System.out.println("You withdrawed $" + amt + " from your account" + "\nBalance: $" + account.getBalance());
			}
		}
		
		return newBalance; 
	}
	
	public static Double deposit(User user, Double amt, int id) {
		Scanner input = new Scanner(System.in);
		Double newBalance = 0.0; 
		
		for(int i = 0; i< user.getAccounts().size(); i++) {
			Account account = user.getAccounts().get(i);
			if(account.getId() == id) {
				newBalance = account.getBalance() + amt; 
				account.setBalance(newBalance);
				acDao.updateAccount(account);
				userDao.getAllUsers();
				acDao.getAllAccounts();
				System.out.println("You deposited $" + amt + " from your account" + "\nBalance: $" + account.getBalance());
			}
		}
		return newBalance; 
	}
	
	public static void transfer(User user, Double transAMT, int acc1ID, int acc2ID, int transChoice) {
		Scanner input = new Scanner(System.in);
		

		if(transChoice == 1) {
			Double newBalance1 = 0.0, newBalance2 = 0.0; 
			for(int i = 0; i< user.getAccounts().size(); i++) {
				Account account = user.getAccounts().get(i);
				if(account.getId() == acc1ID) {
					newBalance1 = account.getBalance() + transAMT; 
					account.setBalance(newBalance1);
					acDao.updateAccount(account);
					userDao.getAllUsers();
					acDao.getAllAccounts();
				}else if(account.getId() == acc2ID) {
					newBalance2 = account.getBalance() - transAMT; 
					account.setBalance(newBalance2);
					acDao.updateAccount(account);
					userDao.getAllUsers();
					acDao.getAllAccounts();
				}
			}
			System.out.println("You have successfully transfered funds");
			System.out.println("Balances: " + user.getAccounts().toString());
		} else if(transChoice == 2) {
			Double newBalance1 = 0.0, newBalance2 = 0.0; 
			
			for(int i = 0; i< user.getAccounts().size(); i++) {
				Account account = user.getAccounts().get(i);
				if(account.getId() == acc1ID) {
					newBalance1 = account.getBalance() - transAMT; 
					account.setBalance(newBalance1);
					acDao.updateAccount(account);
					userDao.getAllUsers();
					acDao.getAllAccounts();
				}else if(account.getId() == acc2ID) {
					newBalance2 = account.getBalance() + transAMT; 
					account.setBalance(newBalance2);
					acDao.updateAccount(account);
					userDao.getAllUsers();
					acDao.getAllAccounts();
				}
			}
			System.out.println("You have successfully transfered funds");
			System.out.println("Balances: " + user.getAccounts().toString());
		}
		
	}
	
	public static void populateUserAccounts(User user) {
		log.trace("Populating user accounts");
		log.trace("trying connection");
		try(Connection conn = cu.getConnection()){
			log.trace("Connection Successful");
			String sql = "select balance, account_type, status, accountid from account join user_account on account.accountid = user_account.account_id where user_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, user.getId());
			ResultSet rs = stmt.executeQuery();
			log.trace("ResultSet done");
			
			log.trace("Creating new account to populate");
			Account account = null; 
			
			log.trace("Populating Users");
			while (rs.next()) {
				
				account = new Account();
				account.setId(rs.getInt("accountid"));
				account.setBalance(rs.getDouble("balance"));
				account.setType(rs.getString("account_type"));
				account.setStatus(rs.getInt("status"));
				List<Account> accounts = user.getAccounts();
				if (accounts == null) {
					accounts = new ArrayList<Account>();
				}
				accounts.add(account);
				user.setAccounts(accounts);
			}
			
		}catch(SQLException e) {
			LogUtil.logException(e, BankingApp.class);
		}
	}
	
	public static void showUserIDs() {
		log.trace("Getting User IDs");
		try(Connection conn = cu.getConnection()){
			log.trace("Connected");
			String sql = "select * from user_account";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			log.trace("result set done");
			
			while(rs.next()) {
				System.out.println("User ID: " + rs.getInt("user_id") + " Account ID: " + rs.getInt("account_id"));
			}
			
		}catch(SQLException e) {
			LogUtil.logException(e, BankingApp.class);
		}
	}
	
	public static void PLSQLAccounts(int id) {
		log.trace("Getting Account Balance");
		
		log.trace("trying connection");
		try(Connection conn = cu.getConnection()){
			log.trace("connection successful");
			CallableStatement cstmt = conn.prepareCall("call view_user_account(?,?)");
			
			log.trace("connection successful");
			cstmt.setInt(1, id);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.executeUpdate();
			log.trace("executed sql");
			//retrieving cursor
			ResultSet rs = (ResultSet) cstmt.getObject(2);
			
			Account account = new Account();
			if(rs.next()) {
				account.setBalance(rs.getDouble("balance"));
				account.setId(rs.getInt("accountid"));
				account.setType(rs.getString("account_type"));
				log.trace("result set done");
			}
			if(rs.next()) {
				log.trace("printing");
				System.out.println("Account: " + account.toString());
			}
			
		}catch(SQLException e) {
			LogUtil.logException(e, BankingApp.class);
		}
	}
	
	private static void PLSQLPersonal(int userID) {
		log.trace("Getting Personal information");
		
		log.trace("trying connection");
		try(Connection conn = cu.getConnection()){
			log.trace("connection successful");
			CallableStatement cstmt = conn.prepareCall("call view_user_personal(?,?)");
			
			log.trace("connection successful");
			cstmt.setInt(1, userID);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.executeUpdate();
			log.trace("executed sql");
			//retrieving cursor
			ResultSet rs = (ResultSet) cstmt.getObject(2);
			
			User user = new User();
			Address address = new Address(); 
			
			if(rs.next()) {
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("Phone"));
				
				log.trace("result set done");
			}
			if(rs.next()) {
				log.trace("printing");
				System.out.println("User: " + user.toString());
			}
			
		}catch(SQLException e) {
			LogUtil.logException(e, BankingApp.class);
		}
		
	}
}
