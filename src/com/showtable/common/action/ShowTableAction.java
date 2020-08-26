package com.showtable.common.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.simple.JSONArray;

import com.DBConnection.gettabledata.CommonShowTableConnector;
import com.opensymphony.xwork2.ActionSupport;

public class ShowTableAction extends ActionSupport implements ServletRequestAware,
													          ServletResponseAware{
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
		CommonShowTableConnector tableConnector = new CommonShowTableConnector(tableName);
		JSONArray jsonArr = tableConnector.getTableData();
		
		PrintWriter out = response.getWriter();
		
		out.println(jsonArr);
		out.flush();
		return NONE;
	}

}
