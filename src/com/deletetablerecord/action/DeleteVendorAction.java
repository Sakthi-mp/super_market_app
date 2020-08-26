package com.deletetablerecord.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import com.DBConnection.deletetablerecord.DeleteVendorFromDB;
import com.opensymphony.xwork2.ActionSupport;

public class DeleteVendorAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{
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
		String vendorID = request.getParameter("ID");
		DeleteVendorFromDB deleteVentor = new DeleteVendorFromDB(Integer.parseInt(vendorID));
		deleteVentor.deleteData();
		
		return NONE;
	}
}