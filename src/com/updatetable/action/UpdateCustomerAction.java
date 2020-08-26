package com.updatetable.action;

import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.DBConnection.addDataToTable.AddNewCustomer;
import com.DBConnection.updateTable.UpdateCustomerTable;
import com.opensymphony.xwork2.ActionSupport;

public class UpdateCustomerAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {
	private static final long serialVersionUID = 1L;
	JSONParser parser = new JSONParser();
	
	HttpServletRequest request;
	HttpServletResponse response;
	
	String customerID;
	String customerName;
	String phoneNumber;
	String EmailID;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		//get JSON string values.
		String jsonString = request.getParameter("formData");
		
		//parse JSON string to object.
		JSONObject json = (JSONObject) parser.parse(jsonString);
		
		customerID = (String) json.get("ID");
		customerName = (String) json.get("customerName");
		EmailID = (String) json.get("EmailID");
		phoneNumber = (String) json.get("phoneNumber");
		
		JSONArray jsonArr = new JSONArray();
		int check = 0;
		
		if(customerName.equals("")) {
			jsonArr.add("Customer name is Empty");
		}else if(customerName.length() < 4 || customerName.length() > 30){
			jsonArr.add("Customer name have 4 to 30 letters");
	    }else if(!Pattern.matches("^[A-Z][a-z]{3,30}", customerName)) {
	    	jsonArr.add("Invalid customer name");
	    }else {
	    	check += 1;
	    }
		
		if(EmailID.equals("")) {
			jsonArr.add("Must enter EmailID ");
		}else if(!Pattern.matches("[a-z]{4,20}[0-9]{2,5}@(gmail|mail|yahoo).(com|co|net|org)", EmailID)) {
			jsonArr.add("Invalid EmailID");
		}else {
	    	check += 1;
	    }
		
		if(phoneNumber.equals("")) {
			jsonArr.add("Phonenumber is Empty");
		}else if(!Pattern.matches("^[6-9][0-9]{9}", phoneNumber)) {
			jsonArr.add("Invalid Phone number");
		}else if(phoneNumber.length() != 10){
			jsonArr.add("Phone number have 10 numbers only");
		}else {
	    	check += 1;
	    }
		
		if(check == 3) {
			Long phonenumber = Long.parseLong(phoneNumber);
									
			UpdateCustomerTable updateCustomer = new UpdateCustomerTable(Integer.parseInt(customerID), customerName, phonenumber, EmailID);
			updateCustomer.updateTable();
			jsonArr.add("success");			
		}	
 
		PrintWriter out = response.getWriter();
		out.println(jsonArr);
		out.flush();
		return NONE;
	}


	@Override
	public void setServletResponse(HttpServletResponse Response) {
		this.response = Response;		
	}

	@Override
	public void setServletRequest(HttpServletRequest Request) {
		this.request = Request;		
	}
}
