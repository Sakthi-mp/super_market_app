package com.find.action;

import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.simple.JSONArray;

import com.DBConnection.find.CheckID;
import com.DBConnection.find.FindQuerySet;
import com.opensymphony.xwork2.ActionSupport;

public class FindSalesAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{
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
		String salesID = request.getParameter("ID");
		
		JSONArray jsonArr = new JSONArray();
		
		CheckID searchSales = new CheckID("sales");
		if(salesID.equals("")) {
			jsonArr.add("empty");
		}else if(!Pattern.matches("[0-9]{1,}", salesID)) {
			jsonArr.add("Invalid salesID");
		}else if(!searchSales.CheckTableID(Integer.parseInt(salesID))){
			jsonArr.add("salesID is Not Found");	
		}else {
			FindQuerySet search = new FindQuerySet("sales", Integer.parseInt(salesID));	
			jsonArr.add("success");
			jsonArr.add(search.find());			
		}
		
		PrintWriter out = response.getWriter();
		out.println(jsonArr);
		out.flush();
		return NONE;
	}
}
