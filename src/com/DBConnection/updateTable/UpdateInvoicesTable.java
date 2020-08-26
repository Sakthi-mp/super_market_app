package com.DBConnection.updateTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class UpdateInvoicesTable {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
		
	int billID;
	double discount;
	String quantity;
	
	private static final String getNetAmountAndTax = "SELECT tax,netAmount FROM purchaseOrder WHERE productName IN "
        										   + "(SELECT productName FROM product WHERE productID=?) LIMIT 1";
	
	private static final String updateInvoicesTable = "UPDATE invoices SET discount=?,quantity=?,TotalAmount=? WHERE billID=?";
	private static final String getQuantityBeforeUpdate = "SELECT quantity FROM invoices WHERE billID=?";
	
	private static final String getProductQuantity = "SELECT quantity FROM product WHERE productID IN (SELECT productID FROM invoices WHERE billID=?)";
	private static final String updateProductTable = "UPDATE product SET quantity=? WHERE productID=?";
	private static final String getProductID = "SELECT productID FROM invoices WHERE billID=?";
	
	Connection connect;
	public UpdateInvoicesTable(int billID,double discount,	String quantity) {
		this.billID = billID;
		this.discount = discount;
		this.quantity = quantity;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updateData() {
		double quantityBeforeUpdate = getQuantityBeforeUpdate();
		
		double mrpAmount = getNetAmountAndTax().get("netAmount") +5*getNetAmountAndTax().get("netAmount")/100;
		double taxAmount=((getNetAmountAndTax().get("tax")/100)*mrpAmount);
		double discountAmount = ((discount/100)*mrpAmount);			
		
		double totalAmount = ((Double.parseDouble(quantity.split(" ")[0])*mrpAmount)+taxAmount) - discountAmount;
		try {
			PreparedStatement statement = connect.prepareStatement(updateInvoicesTable);
			
			statement.setDouble(1, discount);
			statement.setString(2, quantity);
			statement.setDouble(3, totalAmount);
			statement.setInt(4, billID);
			
			statement.executeUpdate();
			modifyProductQuantity(quantityBeforeUpdate);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void modifyProductQuantity(double quantityBeforeUpdate) {
		
		try {
			String quan = "";
			PreparedStatement statement1 = connect.prepareStatement(getProductQuantity);
			statement1.setInt(1, billID);
			
			ResultSet rs1 = statement1.executeQuery();
			while(rs1.next()) {
				quan = (String) rs1.getObject("quantity");
			}
			
			String quanStr = (Double.parseDouble(quan.split(" ")[0])+quantityBeforeUpdate)
							 -Double.parseDouble(quantity.split(" ")[0])+" "+quantity.split(" ")[1];
			
			PreparedStatement statement2 = connect.prepareStatement(updateProductTable);
			statement2.setString(1, quanStr);
			statement2.setInt(2, getProductID());
			
			statement2.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public double getQuantityBeforeUpdate() {
		double quanInt=0;
		String quanStr="";
		try {
			PreparedStatement statement = connect.prepareStatement(getQuantityBeforeUpdate);
			statement.setInt(1, billID);
			
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				quanStr = (String) rs.getObject("quantity");				
			}
			quanInt = Double.parseDouble(quanStr.split(" ")[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return quanInt;
	}
	
	public int getProductID() {
		int ID=0;
		try {
			PreparedStatement statement = connect.prepareStatement(getProductID);
			statement.setInt(1, billID);
			
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				ID = (int) rs.getObject("productID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ID;
	}	
	
	public HashMap<String,Double> getNetAmountAndTax() {
		HashMap<String,Double> arr = new HashMap<String,Double>();
		try {
			PreparedStatement statement = connect.prepareStatement(getNetAmountAndTax);
			statement.setInt(1, getProductID());
			
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
}
