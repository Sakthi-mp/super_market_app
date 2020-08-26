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

public class FindPurchaseAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{
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
		String purchaseID = request.getParameter("ID");
		
		JSONArray jsonArr = new JSONArray();
		
		CheckID searchPurchase = new CheckID("purchase");
		if(purchaseID.equals("")) {
			jsonArr.add("empty");
		}else if(!Pattern.matches("[0-9]{3,}", purchaseID)) {
			jsonArr.add("Invalid purchaseID");
		}else if(!searchPurchase.CheckTableID(Integer.parseInt(purchaseID))){
			jsonArr.add("purchaseID is Not Found");	
		}else {
			FindQuerySet search = new FindQuerySet("purchase", Integer.parseInt(purchaseID));	
			jsonArr.add("success");
			jsonArr.add(search.find());			
		}
		
		PrintWriter out = response.getWriter();
		out.println(jsonArr);
		out.flush();
		return NONE;
	}
}
