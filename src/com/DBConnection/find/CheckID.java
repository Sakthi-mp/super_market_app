package com.DBConnection.find;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class CheckID {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	String getID;
	String whichTable;
	String querySelect;
	String idName;
	
	Connection connect;
	public CheckID(String tableName) {
		this.whichTable = tableName;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean CheckTableID(int ID) {
		if(whichTable.equals("customer")) {
			querySelect = "SELECT customerID FROM customer WHERE leavingReport='NOT REMOVED'";
			idName = "customerID";
		}else if(whichTable.equals("staff")) {
			querySelect = "SELECT employeeID FROM staff WHERE leavingReport='NOT REMOVED'";
			idName = "employeeID";
		}else if(whichTable.equals("vendor")) {
			querySelect = "SELECT vendorID FROM vendor WHERE leavingReport='NOT REMOVED'";
			idName = "vendorID";
		}else if(whichTable.equals("sales")) {
			querySelect = "SELECT salesID FROM salesOrder";
			idName = "salesID";
		}else if(whichTable.equals("purchase")) {
			querySelect = "SELECT purchaseID FROM purchaseOrder";
			idName = "purchaseID";
		}else if(whichTable.equals("product")){
			querySelect = "SELECT productID FROM product";
			idName = "productID";		
		}else if(whichTable.equals("invoices")){
			querySelect = "SELECT billID FROM invoices";
			idName = "billID";
		}else {
			querySelect = "SELECT paymentID FROM payment";
			idName = "paymentID";
		}
		
		ArrayList<Integer> idArr = new ArrayList<Integer>();
		try {
			Statement statement = connect.createStatement();
			
			ResultSet rs = statement.executeQuery(querySelect);
			while(rs.next()) {
				idArr.add((int)rs.getObject(idName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return containsID(ID,idArr);
	}
	
	public boolean containsID(int id,ArrayList<Integer> IDArray) {
		boolean check = false;
		if(IDArray.contains(id)) {
			check = true;
		}else {
			check = false;
		}
		return check;
	}
}
