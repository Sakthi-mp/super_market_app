package com.DBConnection.onload;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GetJobName {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	private static final String getJobName = "SELECT jobName FROM jobDivision";
	
	Connection connect = null;
	
	public GetJobName(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getData() {
		JSONArray jsonArr = new JSONArray();
		try {
			Statement statement = connect.createStatement();	
			ResultSet rs = statement.executeQuery(getJobName);
			int id = 0;
			while(rs.next()) {
				
				JSONObject jsonObject = new JSONObject();
				for(int i = 1;i <= rs.getMetaData().getColumnCount();i += 1) {
					
					String values = rs.getObject(i).toString();					
					jsonObject.put(++id, values);
				}
				
				jsonArr.add(jsonObject);		
			}
		} catch (Exception e) {
		}
		return jsonArr;
	}
}
