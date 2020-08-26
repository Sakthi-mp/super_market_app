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

public class FindPaymentAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {
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
		String paymentID = request.getParameter("ID");
		
		JSONArray jsonArr = new JSONArray();
		
		CheckID searchPayment = new CheckID("payment");
		if(paymentID.equals("")) {
			jsonArr.add("empty");
		}else if(!Pattern.matches("[0-9]{4,}", paymentID)) {
			jsonArr.add("Invalid paymentID");
		}else if(!searchPayment.CheckTableID(Integer.parseInt(paymentID))){
			jsonArr.add("paymentID is Not Found");	
		}else {
			FindQuerySet search = new FindQuerySet("payment", Integer.parseInt(paymentID));	
			jsonArr.add("success");
			jsonArr.add(search.find());			
		}
		
		PrintWriter out = response.getWriter();
		out.println(jsonArr);
		out.flush();
		return NONE;
	}
}
