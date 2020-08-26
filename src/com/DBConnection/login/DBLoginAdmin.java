package com.DBConnection.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

public class DBLoginAdmin {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	
	private static final String getTable = "SELECT adminName,adminPassword FROM admin";
	private static final String passwordKey = "k";
		
	Connection connect;
	EncryptAndDecrypt ED = new EncryptAndDecrypt();
	
	public DBLoginAdmin() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public boolean checkPassword(String passWord) {
		boolean check = false;
		String password = ED.decrypt(checkAdmin().get("adminPassword"),passwordKey);
		if(passWord.equals(password)) {
			check = true;
		}
		return check;
	}
	
	public boolean checkAdminName(String name) {
		boolean check = false;
		if(name.equals(checkAdmin().get("adminName"))) {
			check = true;
		}
		return check;
	}
		
	public HashMap<String, String> checkAdmin() {
		HashMap<String, String> adminDetail = new HashMap<String, String>();
		String name = "";
		String password = "";
		try {
			Statement statement = connect.createStatement();
			ResultSet rs = statement.executeQuery(getTable);
			while(rs.next()) {
				name = (String) rs.getObject("adminName");
				password = (String) rs.getObject("adminPassword");
			}
			adminDetail.put("adminName", name);
			adminDetail.put("adminPassword", password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adminDetail;
	}
}
