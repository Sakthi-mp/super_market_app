package com.DBConnection.addDataToTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewStaff {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	private static final String addStaff = "INSERT INTO staff(employeeName,joiningDate,jobId,salary,phoneNumber) VALUES(?,?,?,?,?)";
	String staffName;
	int salary;
	int jobId;
	Long phoneNumber;
	Connection connect = null;
	
	public AddNewStaff(String staffName,int salary,int jobId,Long phoneNumber) {
		this.staffName = staffName;
		this.salary = salary;
		this.phoneNumber = phoneNumber;
		this.jobId = jobId;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
		}
	}
	
	public void AddData() {
		try {
			PreparedStatement statement = connect.prepareStatement(addStaff);
			statement.setString(1, staffName);
			statement.setString(2, DateAndTime());
			statement.setInt(3, jobId);
			statement.setInt(4, salary);
			statement.setLong(5, phoneNumber);
			
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
