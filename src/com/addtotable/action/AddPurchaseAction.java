package com.addtotable.action;

import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.DBConnection.addDataToTable.AddNewPurchase;
import com.opensymphony.xwork2.ActionSupport;

public class AddPurchaseAction extends ActionSupport implements ServletRequestAware,ServletResponseAware {
	private static final long serialVersionUID = 1L;
	JSONParser parser = new JSONParser();
	
	
	String vendorID;
	String productName;
	String netAmount;
	String quantity;
	
	AddNewPurchase newPurchase = new AddNewPurchase();
	
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
		String jsonString = request.getParameter("formData");
		
		//parse JSON string to object.
		JSONObject json = (JSONObject) parser.parse(jsonString);
		vendorID = (String) json.get("vendorID");
		productName = (String) json.get("productName");
		netAmount = (String) json.get("netAmount");
		quantity = (String) json.get("quantity");
		
		JSONArray jsonArr = new JSONArray();
		int check = 0;
		
		if(vendorID.equals("")) {
			jsonArr.add("Vendor ID is Empty");
		}else if(!Pattern.matches("[0-9]{3,5}", vendorID)) {
	    	jsonArr.add("Invaild VendorID");
	    }else if(!newPurchase.getVendorID(Integer.parseInt(vendorID))){
	    	jsonArr.add("Vendor ID Not Found");
	    }else {
	    	check += 1;
	    }
		
		if(productName.equals("")) {
			jsonArr.add("Product Field is empty");
		}else if(!Pattern.matches("^[a-z][a-z\\s]*", productName.toLowerCase())){
			jsonArr.add("Invalid Product name");
		}else if(!newPurchase.checkProduct(productName, Integer.parseInt(vendorID))) {
			jsonArr.add("Enter correct product name for the given vendor id");
		}else {
			check += 1;
		}
		
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
		
		if(check == 4) {
			AddNewPurchase	newPurchase = new AddNewPurchase(Integer.parseInt(vendorID), productName, Double.parseDouble(netAmount), quantity);
			newPurchase.addData();
			jsonArr.add("success");
		}
		
		PrintWriter out = response.getWriter();
		out.println(jsonArr);
		out.flush();
		return NONE;
	}

}
