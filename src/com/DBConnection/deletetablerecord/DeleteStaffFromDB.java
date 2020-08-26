package com.DBConnection.deletetablerecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DeleteStaffFromDB {
	
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	private static final String deleteCustomer = "UPDATE staff set leavingReport = 'REMOVED' WHERE employeeID = ?";
	
	int employeeID;
	Connection connect = null;
	
	public DeleteStaffFromDB(int employeeID) {
		this.employeeID = employeeID;		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");			
		} catch (Exception e) {
			
		}
	}
	
	public void deleteData() {
		try {
			PreparedStatement statement = connect.prepareStatement(deleteCustomer);
			statement.setInt(1, employeeID);
			
			statement.executeUpdate();
		} catch (Exception e) {
		}
	}
}
