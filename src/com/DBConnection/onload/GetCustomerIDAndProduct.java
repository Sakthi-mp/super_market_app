package com.DBConnection.onload;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GetCustomerIDAndProduct {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	private static final String getCustomerID = "SELECT customerID FROM customer ORDER BY customerID";
	private static final String getProduct = "SELECT productID,productName FROM product ORDER BY productID";
	
	Connection connect = null;
	public GetCustomerIDAndProduct() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getData() {
		JSONArray jsonArr = new JSONArray();
		try {
			Statement statement1 = connect.createStatement();
			ResultSet rs1 = statement1.executeQuery(getCustomerID);
			JSONArray idArr = new JSONArray();
			while(rs1.next()) {
				idArr.add(rs1.getObject("customerID"));
			}
			jsonArr.add(idArr);
			
			Statement statement2 = connect.createStatement();
			ResultSet rs2 = statement2.executeQuery(getProduct);
			JSONArray productArr = new JSONArray();
			JSONObject jsonObject = new JSONObject();
			while(rs2.next()) {
				jsonObject.put(rs2.getObject("productID"), rs2.getObject("productName"));
			}
			productArr.add(jsonObject);
			jsonArr.add(productArr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonArr;
	}
}
