package com.DBConnection.updateTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class UpdateStaffTable {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	private static final String updateTable = "UPDATE staff SET employeeName=?,jobId=?,salary=?,phoneNumber=? WHERE employeeID=?";
	
	int employeeID;
	String employeeName;
	int jobId;
	int salary;
	Long phoneNumber;
		
	Connection connect = null;
	
	public UpdateStaffTable(int employeeID,String employeeName,int jobId,int salary,Long phoneNumber) {
		this.employeeID = employeeID;
		this.employeeName = employeeName;
		this.jobId = jobId;
		this.salary = salary;
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
			statement.setString(1, employeeName);
			statement.setInt(2, jobId);
			statement.setInt(3, salary);
			statement.setLong(4, phoneNumber);
			statement.setInt(5, employeeID);
			
			statement.executeUpdate();
		} catch (Exception e) {
		}
	}
}
