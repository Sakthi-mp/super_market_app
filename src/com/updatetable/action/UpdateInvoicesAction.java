package com.updatetable.action;

import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.DBConnection.addDataToTable.AddNewInvoices;
import com.DBConnection.updateTable.UpdateInvoicesTable;
import com.opensymphony.xwork2.ActionSupport;

public class UpdateInvoicesAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {
	private static final long serialVersionUID = 1L;
	JSONParser parser = new JSONParser();
	
	HttpServletRequest request;
	HttpServletResponse response;

	String billID;
	String quantity;
	String discount;
	
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
	public String execute() throws Exception {
		String jsonString = request.getParameter("formData");
		
		//parse JSON string to object.
		JSONObject json = (JSONObject) parser.parse(jsonString);
		billID = (String) json.get("ID");
		quantity = (String) json.get("quantity");
		discount = (String) json.get("discount");
		
		JSONArray jsonArr = new JSONArray();
		int check = 0;
		
		if(quantity.equals("")) {
			jsonArr.add("Quantity is Empty");
		}else if(quantity.split(" ").length != 2){
			jsonArr.add("Something is missing in quantity");
		}else if(Pattern.matches("\\d*.?\\d*", quantity.split(" ")[0])&&(quantity.split(" ")[1].equals("kg"))|| (quantity.split(" ")[1].equals("litre")) ||(quantity.split(" ")[1].equals("pieces"))) {
			check += 1;
		}else {
	    	jsonArr.add("Invaild product quantity value and unit");
		}
		 
		if(discount.equals("")) {
			 jsonArr.add("Discount is Empty");
		}else if(discount.length() > 2 && Pattern.matches("[0-9]{1,}", discount)) {
			jsonArr.add("Enter Discount below 100(dicount<100)");
		}else if(!Pattern.matches("[0-9]{1,2}", discount)){
			jsonArr.add("Invalid Discount");
		}else {
			check += 1; 
		}
		if(check == 2) {
			UpdateInvoicesTable updateInvoices = new UpdateInvoicesTable(Integer.parseInt(billID), Double.parseDouble(discount), quantity);
			updateInvoices.updateData();
			jsonArr.add("success");
		}
		PrintWriter out = response.getWriter();
		out.println(jsonArr);
		out.flush();
		return NONE;
	}
}
