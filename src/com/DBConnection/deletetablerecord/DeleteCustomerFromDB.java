package com.DBConnection.deletetablerecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DeleteCustomerFromDB {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	private static final String deleteCustomer = "UPDATE customer set leavingReport = 'REMOVED' WHERE customerID = ?";
	
	int customerID;
	Connection connect = null;
	
	public DeleteCustomerFromDB(int customerID) {
		this.customerID = customerID;
		
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");			
		} catch (Exception e) {
			
		}
	}
	
	public void deleteData() {
		try {
			PreparedStatement statement = connect.prepareStatement(deleteCustomer);
			statement.setInt(1, customerID);
			
			statement.executeUpdate();
		} catch (Exception e) {
		}
	}
}
