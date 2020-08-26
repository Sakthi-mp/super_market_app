package com.DBConnection.addDataToTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AddNewInvoices {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	int customerID;
	int productID;
	String quantity;
	double discount;
	
	private static final String getCustomerID = "SELECT customerID from customer";
	private static final String getNetAmountAndTax = "SELECT tax,netAmount FROM purchaseOrder WHERE productName IN "
			                                + "(SELECT productName FROM product WHERE productID=?) LIMIT 1";
	
	private static final String getProductID = "SELECT productID from product";
	private static final String getQuantityFromProductT = "SELECT quantity FROM product WHERE productID=?";
	private static final String modifyProductQuantity = "UPDATE product SET quantity=? WHERE productID=?";
	
	private static final String addNewInvoices = "INSERT INTO invoices(customerID,productID,quantity,netAmount,MRP,tax,discount,TotalAmount,entryTime)"
			                                  + " VALUES(?,?,?,?,?,?,?,?,?)";
	
	
	
	Connection connect;
	public AddNewInvoices(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public AddNewInvoices(int customerID,int productID,String quantity,double discount){
		this.customerID = customerID;
		this.productID = productID;
		this.quantity = quantity;
		this.discount = discount;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	static String DateAndTime() {
		Date date = new Date();
		String dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date); 
		return dateTime;
	}
	
	public boolean checkCustomerID(int customerID){
		ArrayList<Integer> idArr = new ArrayList<Integer>();
		try {
			Statement statement = connect.createStatement();
			
			ResultSet rs = statement.executeQuery(getCustomerID);
			while(rs.next()) {
				idArr.add((int)rs.getObject("customerID"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return containsID(customerID,idArr);
	}
	
	public boolean checkProductID(int productID){
		ArrayList<Integer> idArr = new ArrayList<Integer>();
		try {
			Statement statement = connect.createStatement();
			
			ResultSet rs = statement.executeQuery(getProductID);
			while(rs.next()) {
				idArr.add((int)rs.getObject("productID"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return containsID(productID,idArr);
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
	
	
	public HashMap<String,Double> getNetAmountAndTax() {
		HashMap<String,Double> arr = new HashMap<String,Double>();
		try {
			PreparedStatement statement = connect.prepareStatement(getNetAmountAndTax);
			statement.setInt(1, productID);
			
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				arr.put("netAmount", rs.getDouble("netAmount"));
				arr.put("tax", rs.getDouble("tax"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return arr;
	}
	
	
	public void addData() {
		double mrpAmount = getNetAmountAndTax().get("netAmount") +5*getNetAmountAndTax().get("netAmount")/100;
		double taxAmount=((getNetAmountAndTax().get("tax")/100)*mrpAmount);
		double discountAmount = ((discount/100)*mrpAmount);			
		
		double totalAmount = ((Double.parseDouble(quantity.split(" ")[0])*mrpAmount)+taxAmount) - discountAmount;
		
		try {
			PreparedStatement statement = connect.prepareStatement(addNewInvoices);
			statement.setInt(1, customerID);
			statement.setInt(2, productID);
			statement.setString(3, quantity);
			statement.setDouble(4, getNetAmountAndTax().get("netAmount"));			
			statement.setDouble(5, mrpAmount);
			statement.setDouble(6, getNetAmountAndTax().get("tax"));
			statement.setDouble(7, discount);
			statement.setDouble(8, totalAmount);			
			statement.setString(9, DateAndTime());
			
			statement.executeUpdate();
				
			modifyProductQuantity();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void modifyProductQuantity() {
		String quantityStr = "";
		double quanInt=0;
		try {
			PreparedStatement statement1 = connect.prepareStatement(getQuantityFromProductT);
			statement1.setInt(1, productID);
			
			ResultSet rs = statement1.executeQuery();
			while(rs.next()) {
				quantityStr = (String) rs.getObject("quantity");
			}
			quanInt = Double.parseDouble(quantityStr.split(" ")[0]) - Double.parseDouble(quantity.split(" ")[0]);
			quantityStr = quanInt+ " "+quantityStr.split(" ")[1];
			
			PreparedStatement statement2 = connect.prepareStatement(modifyProductQuantity);
			statement2.setString(1, quantityStr);
			statement2.setInt(2, productID);
			
			statement2.executeUpdate();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
//	public static void main(String args[]) {
//		AddNewInvoices demo = new AddNewInvoices(101,101,"10.5 kg",5);
//		demo.addData();
//	}
}
