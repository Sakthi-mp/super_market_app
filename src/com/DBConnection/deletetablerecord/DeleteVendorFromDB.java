package com.DBConnection.deletetablerecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DeleteVendorFromDB {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	private static final String deleteVendor = "UPDATE vendor set leavingReport = 'REMOVED' WHERE vendorID = ?";
	
	int vendorID;
	Connection connect = null;
	
	public DeleteVendorFromDB(int vendorID) {
		this.vendorID = vendorID;		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");			
		} catch (Exception e) {
			
		}
	}
	
	public void deleteData() {
		try {
			PreparedStatement statement = connect.prepareStatement(deleteVendor);
			statement.setInt(1, vendorID);
			
			statement.executeUpdate();
		} catch (Exception e) {
		}
	}
}
