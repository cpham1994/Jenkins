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
import com.revature.beans.Address;
import com.revature.beans.User;
import com.revature.util.ConnectionUtil;
import com.revature.util.LogUtil;

public class UserFake implements UserDao{
	private static List<User> users = new ArrayList<User>();
	private static Logger log = Logger.getLogger(UserFake.class);
	private static ConnectionUtil cu = ConnectionUtil.getInstance();
	public UserFake()
	{
		
	}
	public User login(String username, String password) {
		for(User u : users) {
			if(u.getUsername().equals(username)&&u.getPassword().equals(password))
				return u;
		}
		return null;
	}

	public User getUser(String username) {
		for(User u : users) {
			if(u.getUsername().equals(username))
				return u;
		}
		return null;
	}

	public void getAllUsers() {
		Address add = null;
		User u = null;
		String sql = "select * from users"; 
		
		log.trace(sql);
		
		log.trace("trying connection");
		
		try(Connection conn = cu.getConnection()){
			log.trace("connection successful");
			PreparedStatement stmt = conn.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();
			log.trace("resultset done");
			while(rs.next()){ //needs to be called
				u = new User();
				add = new Address();
				u.setName(rs.getString("name"));
				u.setId(rs.getInt("userid"));
				add.setId(rs.getInt("address_id"));
				u.setAddress(add);
				u.setPhone(rs.getString("phone_number"));
				u.setEmail(rs.getString("email"));
				u.setUsername(rs.getString("username"));
				u.setPassword(rs.getString("password"));
				
				users.add(u);
			}
		}catch(SQLException e) {
			LogUtil.logException(e, BankingApp.class);
		}
	}
	
	public List<User> getUsers() {
		return users;
	}


	public void saveUser(User u) {
		users = new ArrayList<User>();
		log.trace("Adding user to database");
		log.trace("trying connection");
		 
		try(Connection conn = cu.getConnection()){
			
			int key = 0;
			log.trace("connection successful");
			conn.setAutoCommit(false);
			String sql = "insert into users(name, address_id, phone_number, email, username, password) values(?, ?, ?, ?, ?, ?)"; 
			String [] keys = {"userid"};
			
			PreparedStatement stmt = conn.prepareStatement(sql, keys);
			stmt.setString(1, u.getName());
			stmt.setInt(2, u.getAddress().getId());
			stmt.setString(3, u.getPhone());
			stmt.setString(4, u.getEmail());
			stmt.setString(5, u.getUsername());
			stmt.setString(6, u.getPassword());
			stmt.executeQuery();
			ResultSet rs = stmt.getGeneratedKeys();
			log.trace("resultset done");
			if(rs.next()){ //needs to be called
				log.trace("User created");
				key = rs.getInt(1); 
				u.setId(key);
				conn.commit();
			}else {
				log.trace("User not created");
				conn.rollback();
			}
		}catch(SQLException e) {
			LogUtil.logException(e, UserFake.class);
		}
		
		
	}

	public void updateUser(User u) {
		log.trace("Updating User");
		log.trace("Trying Connection");
		
		try(Connection conn = cu.getConnection()){
			String sql = "update users set name = ?, phone_number = ?, email = ?, username = ?, password = ? where userid = ?";
			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, u.getName());
			stmt.setString(2, u.getPhone());
			stmt.setString(3, u.getEmail());
			stmt.setString(4, u.getUsername());
			stmt.setString(5, u.getPassword());
			stmt.setInt(6, u.getId());

			int commit = stmt.executeUpdate();
			
			if(commit == 1) {
				conn.commit();
			}else {
				log.trace("User not updated");
				conn.rollback();
				
			}
			
		}catch(SQLException e) {
			LogUtil.logException(e, AccountFake.class);
		}
	}

	public void deleteUser(User u) {
		users.remove(u);
	}

}
