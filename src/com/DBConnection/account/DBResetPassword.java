package com.DBConnection.account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.DBConnection.login.EncryptAndDecrypt;

public class DBResetPassword {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	private static final String passwordKey = "k";
	private static final String resetPassword = "UPDATE admin SET adminPassword=?";
	
	String newPassword;
	
	Connection connect;
	EncryptAndDecrypt ED = new EncryptAndDecrypt();
	
	public DBResetPassword(String newPassWord) {
		this.newPassword = ED.encrypt(newPassWord, passwordKey);
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Happy Coding
	
	public void reset() {
		try {
			PreparedStatement statement = connect.prepareStatement(resetPassword);
			statement.setString(1, newPassword);
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
