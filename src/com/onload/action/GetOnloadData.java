package com.onload.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.json.simple.JSONArray;

import com.DBConnection.onload.GetJobName;
import com.opensymphony.xwork2.ActionSupport;

public class GetOnloadData extends ActionSupport implements ServletResponseAware {
	private static final long serialVersionUID = 1L;
	
	HttpServletResponse response;
	
	
	@Override
	public String execute() throws Exception {
		JSONArray jsonArr = new JSONArray();
		GetJobName jobName = new GetJobName();
		jsonArr = jobName.getData();
		
		PrintWriter out = response.getWriter();
		out.println(jsonArr);
		out.flush();
		return NONE;
	}



	@Override
	public void setServletResponse(HttpServletResponse Response) {
		this.response = Response;
	}
}
