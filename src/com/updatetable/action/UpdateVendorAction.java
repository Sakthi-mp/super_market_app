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

import com.DBConnection.updateTable.UpdateVendorTable;
import com.opensymphony.xwork2.ActionSupport;

public class UpdateVendorAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{
	private static final long serialVersionUID = 1L;

	JSONParser parser = new JSONParser();
	
	String vendorID;
	String vendorName;
	String phoneNumber;
	String EmailID;
	
	HttpServletRequest request;
	HttpServletResponse response;
	@Override
	public void setServletResponse(HttpServletResponse Response) {
		this.response = Response;
	}

	@Override
	public void setServletRequest(HttpServletRequest Request) {
		this.request = Request;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		//get JSON string values.
		String jsonString = request.getParameter("formData");
		
		//parse JSON string to object.
		JSONObject json = (JSONObject) parser.parse(jsonString);
		vendorID = (String) json.get("ID");
		vendorName = (String) json.get("vendorName");
		phoneNumber = (String) json.get("phoneNumber");
		EmailID = (String) json.get("vendorEmailID");
		
		JSONArray jsonArr = new JSONArray();
		int check = 0;
		if(vendorName.equals("")) {
			jsonArr.add("Employee name is Empty");
		}else if(vendorName.length() < 4 || vendorName.length() > 30){
			jsonArr.add("Employee name have 4 to 30 letters");
	    }else if(!Pattern.matches("^[A-Z][a-z]{3,30}", vendorName)) {
	    	jsonArr.add("Invalid Employee name");
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
		
		if(EmailID.equals("")) {
			jsonArr.add("EmailID is empty");
		}else if(!Pattern.matches("[a-z]{4,20}[0-9]{2,5}@(gmail|mail|yahoo).(com|co|net|org)", EmailID)) {
			jsonArr.add("Invalid EmailID");
		}else {
	    	check += 1;
	    }
		
		if(check == 3) {
			UpdateVendorTable updateVendor = new UpdateVendorTable(Integer.parseInt(vendorID),vendorName, EmailID, Long.parseLong(phoneNumber));
			updateVendor.updateTable();
			jsonArr.add("success");
		}
		
		PrintWriter out = response.getWriter();
		out.println(jsonArr);
		out.flush();
		return NONE;
	}

}
