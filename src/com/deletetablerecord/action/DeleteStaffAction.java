package com.deletetablerecord.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.DBConnection.deletetablerecord.DeleteStaffFromDB;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteStaffAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {
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

	@Override
	public String execute() throws Exception {
		String customerID = request.getParameter("ID");
		DeleteStaffFromDB deleteCustomer = new DeleteStaffFromDB(Integer.parseInt(customerID));
		deleteCustomer.deleteData();
		
		PrintWriter out = response.getWriter();
		out.println("success");
		out.flush();
		return NONE;
	}

}
