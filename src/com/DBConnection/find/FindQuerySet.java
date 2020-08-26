package com.DBConnection.find;

import org.json.simple.JSONArray;

import com.DBConnection.gettabledata.GetTable;

public class FindQuerySet {
	private static final String findCustomer = "SELECT * FROM customer WHERE customerID=? AND leavingReport='NOT REMOVED'";
	private static final String findStaff = "SELECT * FROM staff WHERE employeeID=? AND leavingReport='NOT REMOVED'";
	private static final String findVendor = "SELECT * FROM vendor WHERE vendorID=? AND leavingReport='NOT REMOVED'";
	private static final String findPurchaseOrder = "SELECT * FROM purchaseOrder WHERE purchaseID=?";
	private static final String findSalesOrder = "SELECT * FROM salesOrder WHERE salesID=?";
	private static final String findProduct = "SELECT * FROM product WHERE productID=?";
	private static final String findInvoices = "SELECT * FROM invoices WHERE billID=?";
	private static final String findPayment = "SELECT * FROM payment WHERE paymentID=?";
	
	String whichTable;
	int ID;
	GetTable showTable = null;
	
	public FindQuerySet(String tableName,int ID){
		this.whichTable = tableName;
		this.ID = ID;
	}
	public JSONArray find() {
		JSONArray jsonArr = null;
		if(whichTable.equals("customer")) {
			
			showTable = new GetTable(findCustomer,ID);
		}else if(whichTable.equals("staff")) {
			
			showTable = new GetTable(findStaff,ID);
		}else if(whichTable.equals("vendor")) {
			
			showTable = new GetTable(findVendor,ID);
		}else if(whichTable.equals("sales")) {
			
			showTable = new GetTable(findSalesOrder,ID);
		}else if(whichTable.equals("purchase")) {
			
			showTable = new GetTable(findPurchaseOrder,ID);
		}else if(whichTable.equals("product")){
			
			showTable = new GetTable(findProduct,ID);			
		}else if(whichTable.equals("invoices")){
			
			showTable = new GetTable(findInvoices,ID);
		}else {
			showTable = new GetTable(findPayment,ID);
		}
		
		jsonArr =  showTable.TableData();
		return jsonArr;
	}
}
