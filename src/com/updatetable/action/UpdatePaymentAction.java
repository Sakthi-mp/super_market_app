package com.updatetable.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.DBConnection.addDataToTable.AddNewInvoices;
import com.DBConnection.updateTable.UpdatePaymentTable;
import com.opensymphony.xwork2.ActionSupport;

public class UpdatePaymentAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{
	private static final long serialVersionUID = 1L;
	JSONParser parser = new JSONParser();
	
	HttpServletRequest request;
	HttpServletResponse response;

	String paymentID;
	String paymentDetail;
	
	AddNewInvoices newInvoices = new AddNewInvoices();
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
	public String execute() throws Exception {String jsonString = request.getParameter("formData");
	
	//parse JSON string to object.
		JSONObject json = (JSONObject) parser.parse(jsonString);
		paymentID = (String) json.get("ID");
		paymentDetail = (String) json.get("paymentDetail");
		
		JSONArray jsonArr = new JSONArray();
		if(paymentDetail.equals("")) {
			jsonArr.add("Payment Detail is Empty");
		}else if(paymentDetail.toUpperCase().equals("NOT PAID") || paymentDetail.toUpperCase().equals("PAID")) {
			UpdatePaymentTable updatePayment = new UpdatePaymentTable(Integer.parseInt(paymentID),paymentDetail.toUpperCase());
			updatePayment.updateData();
			jsonArr.add("success");
		}else {
			jsonArr.add("Invaild Payment Detail");
		}
		PrintWriter out = response.getWriter();
		out.println(jsonArr);
		out.flush();
		return NONE;
	}

}
