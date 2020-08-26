package com.DBConnection.updateTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class UpdateCustomerTable {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	private String customerName;
	private String EmailID;
	private int customerID;
	private Long phoneNumber;
	
	private static final String updateTable = "UPDATE customer SET customerName=?,phoneNumber=?,EmailID=? WHERE customerID=?";
	Connection connect = null;
	
	public UpdateCustomerTable(int customerID,String customerName,Long phonenumber,String EmailID) {
		this.customerID = customerID;
		this.customerName = customerName;
		this.phoneNumber = phonenumber;
		this.EmailID = EmailID;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateTable() {
		try {
			PreparedStatement statement = connect.prepareStatement(updateTable);
			statement.setString(1, customerName);
			statement.setLong(2, phoneNumber);
			statement.setString(3, EmailID);
			statement.setInt(4, customerID);
			
			statement.executeUpdate();
		} catch (Exception e) {
			e.getMessage();
			System.err.println(e.getMessage());	
		}
	}
}
