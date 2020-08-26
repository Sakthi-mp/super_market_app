package com.DBConnection.onload;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GetLoginDetail {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	private static final String getLoginDetail = "SELECT userName,categary,jobName,entryDetail FROM loginDetail";
	
	Connection connect;	
	public GetLoginDetail() {
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
			Statement statement = connect.createStatement();
			ResultSet rs = statement.executeQuery(getLoginDetail);
			JSONObject jsonObj = new JSONObject();
			while(rs.next()) {
				jsonObj.put("userName", rs.getObject("userName"));
				jsonObj.put("categary", rs.getObject("categary"));
				jsonObj.put("jobName", rs.getObject("jobName"));
				jsonObj.put("entryDetail", rs.getObject("entryDetail"));
				jsonArr.add(jsonObj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonArr;
	}
}
