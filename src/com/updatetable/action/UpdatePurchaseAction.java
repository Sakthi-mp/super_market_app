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

import com.DBConnection.updateTable.UpdatePurchaseTable;
import com.opensymphony.xwork2.ActionSupport;

public class UpdatePurchaseAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {
	private static final long serialVersionUID = 1L;

	HttpServletRequest request;
	HttpServletResponse response;
	
	String purchaseID;
	String netAmount;
	String quantity;
	
	JSONParser parser = new JSONParser();
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
		//get JSON string values.
		String jsonString = request.getParameter("formData");
		
		//parse JSON string to object.
		JSONObject json = (JSONObject) parser.parse(jsonString);
		purchaseID = (String) json.get("ID");
		netAmount = (String) json.get("netAmount");
		quantity = (String) json.get("quantity");
		
		JSONArray jsonArr = new JSONArray();
		int check = 0;
		
		if(netAmount.equals("")) {
			jsonArr.add("Net Amount is Empty");
		}else if(!Pattern.matches("^(\\d*\\.)?\\d+$", netAmount)) {
			jsonArr.add("Enter Valid net amount");
		}else {
			check += 1;
		}
		
		if(quantity.equals("")) {
			jsonArr.add("Quantity is Empty");
		}else if(quantity.split(" ").length != 2){
			jsonArr.add("Something is missing in quantity");
		}else if(Pattern.matches("\\d*.?\\d*", quantity.split(" ")[0])&&(quantity.split(" ")[1].equals("kg"))|| (quantity.split(" ")[1].equals("litre")) ||(quantity.split(" ")[1].equals("pieces"))) {
			check += 1;
	    }else {
	    	jsonArr.add("Invaild product quantity value and unit");
	    }
		
		if(check == 2) {
			UpdatePurchaseTable updatePurchase = new UpdatePurchaseTable(Integer.parseInt(purchaseID), Double.parseDouble(netAmount),quantity);
			updatePurchase.updateData();
			jsonArr.add("success");
		}
		PrintWriter out = response.getWriter();
		out.println(jsonArr);
		out.flush();
		return NONE;
	}
}
