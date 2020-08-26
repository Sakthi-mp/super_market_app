package com.DBConnection.gettabledata;

import org.json.simple.JSONArray;

public class CommonShowTableConnector {
	
	private static final String customerTable = "SELECT * FROM customer WHERE leavingReport = 'NOT REMOVED' ORDER BY customerID";
	private static final String staffTable = "SELECT * FROM staff WHERE leavingReport = 'NOT REMOVED' ORDER BY employeeID";
	private static final String vendorTable = "SELECT * FROM vendor WHERE leavingReport = 'NOT REMOVED' ORDER BY vendorID";
	private static final String salesTable = "SELECT * FROM salesOrder ORDER BY salesID";
	private static final String purchaseTable = "SELECT * FROM purchaseOrder ORDER BY purchaseID";
	private static final String productTable = "SELECT * FROM product ORDER BY productID";
	private static final String invoicesTable = "SELECT * FROM invoices ORDER BY billID";
	private static final String paymentTable = "SELECT * FROM payment ORDER BY paymentID";
	
	String tableName;
	GetTable showTable = null; 
	
	public CommonShowTableConnector(String whichTable) {
		this.tableName = whichTable;		
	}
	
	public JSONArray getTableData() {
		
		JSONArray jsonArr = null;
		if(tableName.equals("customer")) {
			
			showTable = new GetTable(customerTable);
		}else if(tableName.equals("staff")) {
			
			showTable = new GetTable(staffTable);
		}else if(tableName.equals("vendor")) {
			
			showTable = new GetTable(vendorTable);
		}else if(tableName.equals("sales")) {
			
			showTable = new GetTable(salesTable);
		}else if(tableName.equals("purchase")) {
			
			showTable = new GetTable(purchaseTable);
		}else if(tableName.equals("product")){
			
			showTable = new GetTable(productTable);			
		}else if(tableName.equals("invoices")){
			
			showTable = new GetTable(invoicesTable);
		}else {
			showTable = new GetTable(paymentTable);
		}
		
		jsonArr =  showTable.TableData();
		return jsonArr;
	}
}
