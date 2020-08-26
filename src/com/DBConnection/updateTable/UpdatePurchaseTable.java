package com.DBConnection.updateTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UpdatePurchaseTable {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	int purchaseID;
	double netAmount;
	String quantity;
	
	private static final String updatePurchaseTable = "UPDATE purchaseOrder SET netAmount=?,quantity=?,totalAmount=? WHERE purchaseID=?";
	private static final String getNewQuantities = "SELECT quantity FROM purchaseOrder WHERE productName IN"
												 + "(SELECT productName FROM purchaseOrder WHERE purchaseID=?)";	
	private static final String updateAllNetAmount = "UPDATE purchaseOrder SET netAmount=? WHERE vendorID=?";
	private static final String updateTotalAmount = "UPDATE purchaseOrder SET totalAmount=? WHERE purchaseID=?";	
	private static final String getVendorID = "SELECT vendorID FROM purchaseOrder WHERE purchaseID=?";
	private static final String getAllPurchaseIDAndQuantity = "SELECT quantity,purchaseID FROM purchaseOrder WHERE vendorID=?";
	
	
	private static final String updateQuantityOnProductTable = "UPDATE product SET quantity=? WHERE productName IN "
															  +"(SELECT productName FROM purchaseOrder WHERE purchaseID=?)";
	
	private static final String getBillQuantity = "SELECT quantity FROM invoices WHERE productID IN "
												+ "(SELECT productID FROM product WHERE productName IN "
												+ "(SELECT productName FROM purchaseOrder WHERE purchaseID=?))";
	Connection connect;
	
	public UpdatePurchaseTable(double netAmount) {
		this.netAmount = netAmount;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public UpdatePurchaseTable(int purchaseID,double netAmount,String quantity) {
		this.purchaseID = purchaseID;
		this.netAmount = netAmount;
		this.quantity = quantity;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void updateData() {
		try {
			PreparedStatement statement = connect.prepareStatement(updatePurchaseTable);
			statement.setDouble(1, netAmount);
			statement.setString(2, quantity);
			statement.setDouble(3, (netAmount*Double.parseDouble(quantity.split(" ")[0])));
			statement.setInt(4, purchaseID);
			
			statement.executeUpdate();
						
			updateNewQuantityOnProduct();
			updateAllNetAmount();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void updateAllNetAmount() {
		int vendorID=0;
		try {
			PreparedStatement statement2 = connect.prepareStatement(getVendorID);
			statement2.setInt(1, purchaseID);
			ResultSet rs = statement2.executeQuery();
			while(rs.next()) {
				vendorID = (int) rs.getObject("vendorID");
			}
			
			PreparedStatement statement = connect.prepareStatement(updateAllNetAmount);
			statement.setDouble(1, netAmount);
			statement.setInt(2, vendorID);
			
			statement.executeUpdate();
			updateAlltotalAmount(vendorID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateAlltotalAmount(int vendorID) {
		try {
			PreparedStatement statement1 = connect.prepareStatement(getAllPurchaseIDAndQuantity);
			statement1.setInt(1,vendorID);
			ResultSet rs = statement1.executeQuery();
			while(rs.next()) {
				int purchaseID = (int) rs.getObject("purchaseID");
				String quantity = (String) rs.getObject("quantity");
				double totalAmount = netAmount * Integer.parseInt(quantity.split(" ")[0]);
				
				
				PreparedStatement statement2 = connect.prepareStatement(updateTotalAmount);
				statement2.setDouble(1, totalAmount);
				statement2.setInt(2, purchaseID);
				
				statement2.executeUpdate();	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public String getNewQuantity() {
		double quan = 0;
		String quanStr="";
		try {			
			PreparedStatement statement = connect.prepareStatement(getNewQuantities);
			statement.setInt(1, purchaseID);
			
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				quanStr = (String) rs.getObject("quantity");
				quan += Double.parseDouble(quanStr.split(" ")[0]);
			}
			quan -= getQuantityFromBill();
			quanStr = quan+" "+quanStr.split(" ")[1];
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return quanStr;
	}
	
	public void updateNewQuantityOnProduct() {
		try {
			PreparedStatement statement = connect.prepareStatement(updateQuantityOnProductTable);
			statement.setString(1, getNewQuantity());
			statement.setInt(2, purchaseID);
			
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public double getQuantityFromBill() {
		double quantity=0;
		try {
			PreparedStatement statement = connect.prepareStatement(getBillQuantity);
			statement.setInt(1, purchaseID);
			
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				String quantityStr = (String) rs.getObject("quantity");
				quantity += Double.parseDouble(quantityStr.split(" ")[0]);
			}
		} catch (Exception e) {
		}
		return quantity;
	}
}