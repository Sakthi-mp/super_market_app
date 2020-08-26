package com.addtotable.action;

import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.DBConnection.addDataToTable.AddNewStaff;
import com.opensymphony.xwork2.ActionSupport;

public class AddStaffAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {
	private static final long serialVersionUID = 1L;
	JSONParser parser = new JSONParser();
	
	String staffName;
	String jobID;
	String phoneNumber;
	String salary;
	
	HttpServletRequest request;
	HttpServletResponse response;
	
	@Override
	public void setServletRequest(HttpServletRequest Request) {
		this.request = Request;
	}

	@Override
	public void setServletResponse(HttpServletResponse Response) {
		this.response = Response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		//get JSON string values.
		String jsonString = request.getParameter("formData");
		
		//parse JSON string to object.
		JSONObject json = (JSONObject) parser.parse(jsonString);
		staffName = (String) json.get("employeeName");
		jobID = (String) json.get("jobID");
		phoneNumber = (String) json.get("phoneNumber");
		salary = (String) json.get("salary");
		
		JSONArray jsonArr = new JSONArray();
		int check = 0;
		
		
		if(staffName.equals("")) {
			jsonArr.add("Employee name is Empty");
		}else if(staffName.length() < 4 || staffName.length() > 30){
			jsonArr.add("Employee name have 4 to 30 letters");
	    }else if(!Pattern.matches("^[A-Z][a-z]{3,30}", staffName)) {
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
		
		if(salary.equals("")) {
			jsonArr.add("Salary is empty");
		}else if(Pattern.matches("[0-9]", salary) && salary.length() > 6){
			jsonArr.add("Salary amount is too much for employee");
		}else if(!Pattern.matches("[0-9]{4,6}", salary)) {
			jsonArr.add("Invaild value in salary");
		}else {
			check += 1;
		}
		
		
		if(check == 3) {
			
			AddNewStaff newStaff = new AddNewStaff(staffName, Integer.parseInt(salary),Integer.parseInt(jobID), Long.parseLong(phoneNumber));
			newStaff.AddData();
			jsonArr.add("success");
		}
		PrintWriter out = response.getWriter();
		out.println(jsonArr);
		out.flush();
		
		return NONE;
	}

}
