package com.DBConnection.deletedtableDetails;

import org.json.simple.JSONArray;

import com.DBConnection.gettabledata.GetTable;

public class DeletedRecordQuerySet {
	private static final String customerTable = "SELECT * FROM customer WHERE leavingReport = 'REMOVED' ORDER BY customerID";
	private static final String staffTable = "SELECT * FROM staff WHERE leavingReport = 'REMOVED' ORDER BY employeeID";
	private static final String vendorTable = "SELECT * FROM vendor WHERE leavingReport = 'REMOVED' ORDER BY vendorID";
	
	String tableName;
	GetTable showTable = null; 
	
	public DeletedRecordQuerySet(String whichTable) {
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
		}
		
		jsonArr =  showTable.TableData();
		return jsonArr;
	}
}
