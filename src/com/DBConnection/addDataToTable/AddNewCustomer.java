package com.DBConnection.addDataToTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewCustomer {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	private static final String NewCustomer = "INSERT INTO customer(customerName,entryTime,phoneNumber,EmailID) VALUES(?,?,?,?)";
	
	private String customerName;
	private String EmailID;
	private Long phoneNumber;
	
	Connection connect = null;
	
	public AddNewCustomer(String customername,String Emailid,Long phonenumber) {
		this.customerName = customername;
		this.EmailID = Emailid;
		this.phoneNumber = phonenumber;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//get the current date and time.	
	static String DateAndTime() {
		Date date = new Date();
		String dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date); 
		return dateTime;
	}
		
	public void addData() {
		try {
			PreparedStatement statement = connect.prepareStatement(NewCustomer);
			statement.setString(1, customerName);
			statement.setString(2, DateAndTime());
			statement.setLong(3, phoneNumber);
			statement.setString(4, EmailID);
			
			statement.executeUpdate();
		}catch(Exception e){}
	}
}
