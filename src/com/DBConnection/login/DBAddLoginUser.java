package com.DBConnection.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DBAddLoginUser {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	private static final String clearLoginUser = "DELETE FROM loginDetail";
	private static final String addLoginUser = "INSERT INTO loginDetail(userName,categary,jobName,entryDetail) "
			                         		 + " VALUES(?,?,?,?)";
	
	String userName = "none";		
	String categary = "none";
	String jobName = "none";
	
	Connection connect;	
	public DBAddLoginUser(String userName,String categary) {
		this.userName = userName;
		this.categary = categary;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public DBAddLoginUser(String userName,String categary,String jobName) {
		this.userName = userName;
		this.categary = categary;
		this.jobName = jobName;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addData() {
		try {
			Statement sta = connect.createStatement();
			sta.execute(clearLoginUser);
			PreparedStatement statement = connect.prepareStatement(addLoginUser);
			statement.setString(1, userName);
			statement.setString(2, categary);
			statement.setString(3, jobName);
			statement.setString(4, "LOGIN");
			
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
