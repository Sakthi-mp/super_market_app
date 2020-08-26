package com.DBConnection.logout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBLogout {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	private static final String updateLoginDetail = "UPDATE loginDetail SET entryDetail='LOGOUT'";
	
	Connection connect;	
	public DBLogout() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void logout() {
		try {
			Statement statement = connect.createStatement();
			statement.executeUpdate(updateLoginDetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
