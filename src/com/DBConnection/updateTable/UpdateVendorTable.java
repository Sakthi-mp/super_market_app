package com.DBConnection.updateTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class UpdateVendorTable {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	private static final String updateTable = "UPDATE vendor SET vendorName=?,phoneNumber=?,vendorEmailID=? WHERE vendorID=?";
	
	int vendorID;
	String vendorName;	String EmailID;
	Long phoneNumber;
		
	Connection connect = null;
	
	public UpdateVendorTable(int vendorID,String vendorName,String EmailID,Long phoneNumber) {
		this.vendorID = vendorID;
		this.vendorName = vendorName;
		this.EmailID = EmailID;
		this.phoneNumber = phoneNumber;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
		}
	}
	
	public void updateTable() {
		try {
			PreparedStatement statement = connect.prepareStatement(updateTable);					
			statement.setString(1, vendorName);
			statement.setLong(2, phoneNumber);
			statement.setString(3, EmailID);
			statement.setInt(4, vendorID);			
			
			statement.executeUpdate();
		} catch (Exception e) {
		}
	}
}
