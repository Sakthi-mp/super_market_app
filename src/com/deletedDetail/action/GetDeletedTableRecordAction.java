package com.deletedDetail.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.simple.JSONArray;

import com.DBConnection.deletedtableDetails.DeletedRecordQuerySet;
import com.opensymphony.xwork2.ActionSupport;

public class GetDeletedTableRecordAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{
	private static final long serialVersionUID = 1L;
	
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	
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
		String tableName = request.getParameter("tableName");
		DeletedRecordQuerySet tableConnector = new DeletedRecordQuerySet(tableName);
		JSONArray jsonArr = tableConnector.getTableData();
		PrintWriter out = response.getWriter();
		
		out.println(jsonArr);
		out.flush();
		return NONE;
	}
}
