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
import com.opensymphony.xwork2.ActionSupport;

public class LoginAdminAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {
	private static final long serialVersionUID = 1L;
	JSONParser parser = new JSONParser();
	
	String adminName;
	String adminPassWord;
	
	HttpServletRequest request;
	HttpServletResponse response;
	
	
	DBLoginAdmin db = new DBLoginAdmin();
	DBAddLoginUser addLoginUser;
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
		adminName = (String) json.get("adminName");
		adminPassWord = (String) json.get("adminPassWord");
		
		JSONArray jsonArr = new JSONArray();
		int check=0;
		
		if(adminName.equals("")) {
			jsonArr.add("Admin name is Empty");
		}else if(adminName.length() < 4 || adminName.length() > 30){
			jsonArr.add("Admin name have 4 to 30 letters");
	    }else if(Pattern.matches("^[A-Z][a-z]{3,30}.?[A-Z]{0,1}", adminName)) {
	    	if(db.checkAdminName(adminName)) {
	    		check += 1;
	    	}else {
	    		jsonArr.add("Admin name does not match");
	    	}
	    }else{
	    	jsonArr.add("Invaild AdminName");
	    }
		
		if(adminPassWord.equals("")) {
			jsonArr.add("Admin password is Empty");
		}else if(Pattern.matches("^[A-Z][a-z]{3,30}@[0-9]{1,5}", adminPassWord)){
			if(db.checkPassword(adminPassWord)) {
				check += 1;
			}else {
				jsonArr.add("Password does not match");
			}
	    }else {
	    	jsonArr.add("Invaild Password");
	    }
		
		if(check == 2) {
			jsonArr.add("success");
			addLoginUser = new DBAddLoginUser(adminName, "admin");
			addLoginUser.addData();
		}
		
		PrintWriter out = response.getWriter();
		out.println(jsonArr);
		out.flush();
		return NONE;
	}
}
