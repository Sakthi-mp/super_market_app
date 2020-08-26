package com.DBConnection.addDataToTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewVendor {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	private static final String addVendor = "INSERT INTO vendor(vendorName,vendorProduct,phoneNumber,vendorEmailID,entryDate) VALUES(?,?,?,?,?)";
	String vendorName;
	String productName;
	String emailID;
	Long phoneNumber;
	
	Connection connect = null;
	
	public AddNewVendor(String vendorName,String productName,String emailID,Long phoneNumber) {
		this.vendorName = vendorName;
		this.productName = productName;
		this.phoneNumber = phoneNumber;
		this.emailID = emailID;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
		}
	}
	
	public void AddData() {
		try {
			PreparedStatement statement = connect.prepareStatement(addVendor);
			statement.setString(1, vendorName);
			statement.setString(2,productName);
			statement.setLong(3, phoneNumber);
			statement.setString(4, emailID);
			statement.setString(5, DateAndTime());
			
			statement.executeUpdate();
		} catch (Exception e) {
		}
	}
	
	
	//get the current date and time.	
	static String DateAndTime() {
		Date date = new Date();
		String dateTime = new SimpleDateFormat("yyyy-MM-dd").format(date); 
		return dateTime;
	}
}
