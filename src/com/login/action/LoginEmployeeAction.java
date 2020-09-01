package com.login.action;

import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.DBConnection.login.DBAddLoginUser;
import com.DBConnection.login.DBLoginAdmin;
import com.DBConnection.login.DBLoginEmployee;
import com.cachedata.login.RedisAddLoginUser;
import com.opensymphony.xwork2.ActionSupport;

public class LoginEmployeeAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {
	private static final long serialVersionUID = 1L;
	JSONParser parser = new JSONParser();
	
	String emloyeeID;
	String phoneNumber;
	
	HttpServletRequest request;
	HttpServletResponse response;
	
	DBLoginEmployee db = new DBLoginEmployee();
	RedisAddLoginUser addLoginUser;
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
		String jsonString = request.getParameter("formData");
		
		//parse JSON string to object.
		JSONObject json = (JSONObject) parser.parse(jsonString);
		emloyeeID = (String) json.get("employeeID");
		phoneNumber = (String) json.get("phoneNumber");
		
		JSONArray jsonArr = new JSONArray();
		int check=0;
		
		if(emloyeeID.equals("")) {
			jsonArr.add("EmployeeId is Empty");
		}else if(Pattern.matches("[0-9]{4,5}", emloyeeID)) {
			if(db.checkEmployeeID(Integer.parseInt(emloyeeID))) {
				check += 1;
			}else {
				jsonArr.add("EmployeeId does not match");
			}
		}else{
			jsonArr.add("Invalid EmployeeId is Empty");
		}
		
		if(phoneNumber.equals("")) {
			jsonArr.add("Phonenumber is empty");
		}else if(Pattern.matches("^[6-9][0-9]{9}", phoneNumber) && phoneNumber.length() == 10){
			if(db.checkPhoneNumber(Long.parseLong(phoneNumber))) {
				check += 1;
			}else {
				jsonArr.add("Phone number does not match");
			}			
		}else {
			jsonArr.add("Invalid Phone number");
	    }
		
		if(check == 2) {
			jsonArr.add("success");
			String emplyeeName = db.getEmployeeName(Integer.parseInt(emloyeeID));
			String emplyeeJob = db.getJobName(Integer.parseInt(emloyeeID));
			addLoginUser = new RedisAddLoginUser(emplyeeName, "employee", emplyeeJob);
			addLoginUser.login();
		}
		PrintWriter out = response.getWriter();
		out.println(jsonArr);
		out.flush();
		return NONE;
	}
}

