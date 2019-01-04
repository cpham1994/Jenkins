package com.revature.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionUtil {
	private static ConnectionUtil cu = null;
	private static Properties prop;
	
	private ConnectionUtil() {
		prop = new Properties();
		try {
			InputStream dbProps = ConnectionUtil.class.getClassLoader().getResourceAsStream("database.properties");
			prop.load(dbProps);
		} catch(Exception e) {
			LogUtil.logException(e, ConnectionUtil.class);
		}
	}
	
	public static synchronized ConnectionUtil getInstance() {
		if (cu==null) {
			cu = new ConnectionUtil();
		}
		return cu;
	}

	public Connection getConnection() {
		Connection conn = null;
		
		try {
			// We have to register our driver class.
			Class.forName(prop.getProperty("driver"));
			conn = DriverManager.getConnection(prop.getProperty("url"),
					prop.getProperty("bob"), prop.getProperty("psw"));
		} catch (Exception e) {
			LogUtil.logException(e, ConnectionUtil.class);
		}
		
		return conn;
	}
}
