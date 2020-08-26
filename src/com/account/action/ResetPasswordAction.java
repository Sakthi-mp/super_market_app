package com.account.action;

import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.DBConnection.account.DBResetPassword;
import com.DBConnection.login.DBLoginAdmin;
import com.opensymphony.xwork2.ActionSupport;

public class ResetPasswordAction extends ActionSupport implements ServletResponseAware,ServletRequestAware{
	private static final long serialVersionUID = 1L;
	JSONParser parser = new JSONParser();
	
	String oldPassword;
	String newPassword;
	String confirmPassword;
	
	HttpServletResponse response;
	HttpServletRequest request;
	
	DBLoginAdmin db = new DBLoginAdmin();
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
		
		oldPassword = (String) json.get("oldPassword");
		newPassword = (String) json.get("newPassword");
		confirmPassword = (String) json.get("confirmPassword");
		JSONArray jsonArr = new JSONArray();
		int check = 0;
		
		if(oldPassword.equals("")) {
			jsonArr.add("Old Password is Empty");
		}else if(Pattern.matches("^[A-Z][a-z]{3,30}@[0-9]{1,5}", oldPassword)){
			if(db.checkPassword(oldPassword)) {
				check += 1;
			}else {
				jsonArr.add("Password does not match");
			}
	    }else {
	    	jsonArr.add("Invaild Old Password");
	    }
		
		if(newPassword.equals("")) {
			jsonArr.add("New Password is Empty");
		}else if(Pattern.matches("^[A-Z][a-z]{3,30}@[0-9]{1,5}", newPassword)){
			check += 1;
	    }else {
	    	jsonArr.add("Invaild New Password");
	    }
		
		if(confirmPassword.equals("")) {
			jsonArr.add("Confirm Password is Empty");
		}else if(Pattern.matches("^[A-Z][a-z]{3,30}@[0-9]{1,5}", confirmPassword)){
			if(confirmPassword.equals(newPassword)) {
				check += 1;
			}else {
				jsonArr.add("New PassWord and Confirm password are does not match");
			}
			
	    }else {
	    	jsonArr.add("Invaild Confirm Password");
	    }
		
		
		if(check == 3) {
			DBResetPassword resetPassword = new DBResetPassword(newPassword);
			resetPassword.reset();
			jsonArr.add("success");			
		}	
 
		PrintWriter out = response.getWriter();
		out.println(jsonArr);
		out.flush();
		return NONE;
	}
}
