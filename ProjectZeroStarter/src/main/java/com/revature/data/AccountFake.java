package com.revature.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.bankingapp.BankingApp;
import com.revature.beans.Account;
import com.revature.util.ConnectionUtil;
import com.revature.util.LogUtil;

public class AccountFake implements AccountDao{
	private static List<Account> accounts = new ArrayList<Account>();
	private static Logger log = Logger.getLogger(AccountFake.class);
	private static ConnectionUtil cu = ConnectionUtil.getInstance();
	public AccountFake() {
		
	}
	public Account getAccount(int id) {
		for(Account a : accounts) {
			if(a.getId()==id)
				return a;
		}
		return null;
	}
	
	public void getAllAccounts() {
		Account accc = null;
		String sql = "select * from account"; 
		
		log.trace(sql);
		 
		log.trace("trying to connect");
		
		try(Connection conn = cu.getConnection()){
			conn.commit();
			log.trace("connection successful");
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			log.trace("resultset done");
			while(rs.next()) {
				accc = new Account(); 
				accc.setBalance(rs.getDouble("balance"));
				accc.setStatus(rs.getInt("status"));
				accc.setType(rs.getString("account_type"));
				accc.setId(rs.getInt("accountid"));

				accounts.add(accc);
				log.trace("Accounts added to list");
			}
		}catch(SQLException e) {
			LogUtil.logException(e, BankingApp.class);
		}
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void saveAccount(Account a) {
		log.trace("Adding account to database");
		
		log.trace("trying connection");
		
		try(Connection conn = cu.getConnection()){
			
			int key = 0;
			log.trace("connection successful");
			conn.setAutoCommit(false);
			String sql = "insert into account(balance, account_type, status) values(?, ?, ?)"; 
			String [] keys = {"accountid"};
			 
			PreparedStatement stmt = conn.prepareStatement(sql, keys);
			stmt.setDouble(1, a.getBalance());
			stmt.setString(2, a.getType());
			stmt.setInt(3, a.getStatus());
			
			stmt.executeQuery();
			ResultSet rs = stmt.getGeneratedKeys();
			
			log.trace("resultset done");
			if(rs.next()){ //needs to be called
				
				key = rs.getInt(1); 
				a.setId(key);
				conn.commit();
				log.trace("Account created");
				//updateBridge();
			}else {
				log.trace("Account not created");
				conn.rollback();
			}
			
			conn.commit();
		}catch(SQLException e) {
			LogUtil.logException(e, AccountFake.class);
		}
		
		
	}

	private void updateBridge() {
		log.trace("Updating Bridge");
		
		log.trace("Trying Connection");
		try(Connection conn = cu.getConnection()){
			log.trace("Connection Successful");
			String sql = "";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
		}catch(SQLException e) {
			LogUtil.logException(e, AccountFake.class);
		}
	}

	public void updateAccount(Account a) {
		log.trace("Updating Account");
		log.trace("Trying Connection");
		
		try(Connection conn = cu.getConnection()){
			log.trace("Connection Successful");
			String sql = "update account set balance = ?, account_type = ?, status = ? where accountid = ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
		
			stmt.setDouble(1, a.getBalance());
			stmt.setString(2, a.getType());
			stmt.setInt(3, a.getStatus());
			stmt.setInt(4, a.getId());

			int commit = stmt.executeUpdate();
			if(commit == 1) {
				conn.commit();
				log.trace("Accounts updated");
			}else {
				log.trace("Accounts not updated");
				conn.rollback();
				
			}
			conn.commit();
			
		}catch(SQLException e) {
			LogUtil.logException(e, AccountFake.class);
		}
	}

	public void deleteAccount(Account a) {
		accounts.remove(a);
	}

}
