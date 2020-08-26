
package com.DBConnection.addDataToTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.DBConnection.updateTable.UpdatePurchaseTable;

import java.sql.Statement;

public class AddNewPurchase {
	private static final String url = "jdbc:mysql://localhost:3306/market_db";
	private static final String User = "root";
	
	int vendorID;
	String productName;
	double netAmount;
	String quantity;
	
	private static final String getVendorProduct = "SELECT vendorProduct FROM vendor WHERE vendorID=?";	
	private static final String getVendorID = "SELECT vendorID FROM vendor";
	
	private static final String addPurchaseOrder = "INSERT INTO purchaseOrder(vendorID,productName,netAmount,quantity,totalAmount,tax,receivedDate)"
												 + " VALUES(?,?,?,?,?,?,?)";
	private static final String updateAllNetAmount = "UPDATE purchaseOrder SET netAmount=? WHERE vendorID=?";
	
	private static final String getTax = "SELECT tax FROM purchaseOrder WHERE vendorID=? LIMIT 1";
	private static final String getProductListInPurchase = "SELECT productName FROM purchaseOrder";

	
	private static final String addProduct = "INSERT INTO product(productName,quantity) VALUES(?,?)";
	private static final String getProductQuantity = "SELECT quantity FROM product WHERE productName=?";
	private static final String updateProductQuantity = "UPDATE product SET quantity=? WHERE productName=?";
	private static final String getProductName = "SELECT productName FROM product";
	
	private static final String getBillQuantity = "SELECT quantity FROM invoices WHERE productID IN "
								                + "(SELECT productID FROM product WHERE productName=?)";
	static Connection connect;
	
	public AddNewPurchase() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public AddNewPurchase(int vendorID,	String productName,	double netAmount,String quantity) {
		this.vendorID = vendorID;
		this.productName = productName;
		this.netAmount = netAmount;
		this.quantity = quantity;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url, User,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String DateAndTime() {
		Date date = new Date();
		String dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date); 
		return dateTime;
	}
	
	public boolean getVendorID(int vendorID) {
		boolean check = false;
		
		try {
			 Statement statement = connect.createStatement();
			ResultSet rs = statement.executeQuery(getVendorID);
			while(rs.next()) {
				int venID = (int) rs.getObject("vendorID");
				if(vendorID == venID) {
					check = true;
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return check;
	}
	
	public boolean checkProduct(String productName,int vendorID) {
		boolean check = false;
		String product_name = "";
		try {
			PreparedStatement statement = connect.prepareStatement(getVendorProduct);
			statement.setInt(1, vendorID);
			
			java.sql.ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				product_name = (String) rs.getObject("vendorProduct");
			}
			if(productName.equals(product_name)) {check = true;}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return check;
	}
	public void addData() {
		
		double quan = Double.parseDouble(quantity.split(" ")[0]);
		Random ran = new Random();
		int tax = (ran.nextInt(5)+1);
		
		try {
			PreparedStatement statement = connect.prepareStatement(addPurchaseOrder);
			statement.setInt(1, vendorID);
			statement.setString(2, productName);
			statement.setDouble(3, netAmount);
			statement.setString(4, quantity);
			statement.setDouble(5, (netAmount*quan));
			if(containProductInPurchase()) {
				statement.setInt(6, getTax());
			}else {
				statement.setInt(6, tax);
			}		
			statement.setString(7, DateAndTime());
			
			statement.executeUpdate();
			
			updateAllNetAmount();
			checkProductFromProductTable(quan);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void updateAllNetAmount() {
		try {
			PreparedStatement statement = connect.prepareStatement(updateAllNetAmount);
			statement.setDouble(1, netAmount);
			statement.setInt(2, vendorID);			
			statement.executeUpdate();
			UpdatePurchaseTable updatePur = new UpdatePurchaseTable(netAmount);
			updatePur.updateAlltotalAmount(vendorID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//this method used to insert productname and their quantity when data added to the purchaseOrder table
	public void insertIntoProduct() {
		try {
			PreparedStatement statement = connect.prepareStatement(addProduct);
			statement.setString(1, productName);
			statement.setString(2, quantity);
			
			statement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//this method used to update the quantity from the product when same productname entered in the purchasetable
	public void updateProductQuantity(double quantity) {
		
		try {
			String quan = "";
			PreparedStatement state1 = connect.prepareStatement(getProductQuantity);
			state1.setString(1, productName);
			
			ResultSet rs1 = state1.executeQuery();
			while(rs1.next()) {
				quan = (String) rs1.getObject("quantity");
			}
			quantity += Double.parseDouble(quan.split(" ")[0]);
			quantity -= getQuantityFromBill();
			PreparedStatement state2 = connect.prepareStatement(updateProductQuantity);
			state2.setString(1, quantity+" "+quan.split(" ")[1]);
			state2.setString(2, productName);
			
			state2.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void checkProductFromProductTable(double quantity) {
		ArrayList<String> ProductArr = new ArrayList<String>();
		try {
			Statement statement = connect.createStatement();
			
			ResultSet rs = statement.executeQuery(getProductName);			
			while(rs.next()) {				
				String product_name = (String) rs.getObject("productName");
				ProductArr.add(product_name);
			}
			
			if(ProductArr.contains(productName)) {
				updateProductQuantity(quantity);
				
			}else {
				insertIntoProduct();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public int getTax() {
		int tax=0;
		try {
			PreparedStatement statement = connect.prepareStatement(getTax);
			statement.setInt(1, vendorID);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				tax = (int) rs.getObject("tax");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return tax;
	}
	
	
	public boolean containProductInPurchase() {
		boolean check = false;
		ArrayList<String> ProductArr = new ArrayList<String>();
		try {
			Statement statement = connect.createStatement();			
			ResultSet rs = statement.executeQuery(getProductListInPurchase);
			while(rs.next()) {
				ProductArr.add((String) rs.getObject("productName"));
			}
			if(ProductArr.contains(productName)) {
				check = true;
			}else {
				check = false;
			}
			
		} catch (Exception e) {
		}
		return check;
	}
	
	public double getQuantityFromBill() {
		double quantity=0;
		try {
			PreparedStatement statement = connect.prepareStatement(getBillQuantity);
			statement.setString(1, productName);
			
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

