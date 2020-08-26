package com.DBConnection.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.opensymphony.xwork2.Result;

public class DBLoginEmployee {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	private static final String getEmployeeID = "SELECT employeeID FROM staff";
	private static final String getStaffPhoneNumber = "SELECT phoneNumber FROM staff";
	private static final String getEmployeeName = "SELECT employeeName FROM staff WHERE employeeID=?";
	private static final String getEmployeeJob = "SELECT jobName FROM jobDivision WHERE jobId IN "
							                   +"(SELECT jobId FROM staff WHERE employeeID=?)";
	
	
	Connection connect;
	public DBLoginEmployee() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkEmployeeID(int employeeID) {
		ArrayList<Integer> idArr = new ArrayList<Integer>();
		try {
			Statement statement = connect.createStatement();
			ResultSet rs = statement.executeQuery(getEmployeeID);
			while(rs.next()) {
				idArr.add((Integer) rs.getObject("employeeID"));
 			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(idArr.contains(employeeID)) {
			return true;
		}
		return false;
	}
	
	public boolean checkPhoneNumber(Long phoneNumber) {
		ArrayList<Long> phoneNumberArr = new ArrayList<Long>();
		try {
			Statement statement = connect.createStatement();
			ResultSet rs = statement.executeQuery(getStaffPhoneNumber);
			while(rs.next()) {
				phoneNumberArr.add((Long) rs.getObject("phoneNumber"));
 			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(phoneNumberArr.contains(phoneNumber)) {
			return true;
		}
		return false;
	}
	
	public String getEmployeeName(int employeeID) {
		String name="";
		try {
			PreparedStatement statement = connect.prepareStatement(getEmployeeName);
			statement.setInt(1, employeeID);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				name = (String) rs.getObject("employeeName");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public String getJobName(int EmployeeID) {
		String job="";
		try {
			PreparedStatement statement = connect.prepareStatement(getEmployeeJob);
			statement.setInt(1, EmployeeID);
			
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				job = (String) rs.getObject("jobName");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return job;
	}
}
