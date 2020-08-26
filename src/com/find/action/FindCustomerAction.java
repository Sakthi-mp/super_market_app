package com.find.action;

import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.simple.JSONArray;

import com.DBConnection.addDataToTable.AddNewInvoices;
import com.DBConnection.find.CheckID;
import com.DBConnection.find.FindQuerySet;
import com.opensymphony.xwork2.ActionSupport;

public class FindCustomerAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{
	
	private static final long serialVersionUID = 1L;

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
		String customerID = request.getParameter("ID");
		
		JSONArray jsonArr = new JSONArray();
		
		CheckID searchCustomer = new CheckID("customer");
		if(customerID.equals("")) {
			jsonArr.add("empty");
		}else if(!Pattern.matches("[0-9]{3,}", customerID)) {
			jsonArr.add("Invalid customerID");
		}else if(!searchCustomer.CheckTableID(Integer.parseInt(customerID))){
			jsonArr.add("customerID is Not Found");	
		}else {
			FindQuerySet search = new FindQuerySet("customer", Integer.parseInt(customerID));	
			jsonArr.add("success");
			jsonArr.add(search.find());			
		}
		
		PrintWriter out = response.getWriter();
		out.println(jsonArr);
		out.flush();
		return NONE;
	}
	
}
