package com.DBConnection.updateTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class UpdatePaymentTable {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
		
	int paymentID;
	String paymentDetail;
	
	private static final String updatePaymentTable = "UPDATE payment SET paymentDetail=? WHERE paymentID=?";
	Connection connect;
	public UpdatePaymentTable(int paymentID,String paymentDetail) {
		this.paymentID = paymentID;
		this.paymentDetail = paymentDetail;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updateData() {
		try {
			PreparedStatement statement = connect.prepareStatement(updatePaymentTable);			
			statement.setString(1, paymentDetail);
			statement.setInt(2, paymentID);
			
			statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
