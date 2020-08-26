package com.DBConnection.gettabledata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GetTable {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	String getTable = "";
	int ID=0;
	boolean check = false;
	Connection connect = null;
	
	public GetTable(String selectQuery){
		this.getTable = selectQuery;
		check = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public GetTable(String selectQuery,int ID) {
		this.getTable = selectQuery;
		this.ID = ID;
		check = true;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray TableData() {
		
		JSONArray jsonArr = new JSONArray();
		try {
			PreparedStatement statement = connect.prepareStatement(getTable);
			
			if(check) {
				statement.setInt(1, ID);
			}
			
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				
				JSONObject jsonObject = new JSONObject();
				for(int i = 1;i <= rs.getMetaData().getColumnCount();i += 1) {
					
					String values = rs.getObject(i).toString();					
					jsonObject.put(rs.getMetaData().getColumnName(i), values);
				}
				
				jsonArr.add(jsonObject);		
			}			
		} catch (Exception e) {		
			e.getMessage();
			System.err.println(e.getMessage());			
		}
		return jsonArr;
	}
}
