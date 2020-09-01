package com.logout.action;

import com.cachedata.login.RedisAddLoginUser;
import com.opensymphony.xwork2.ActionSupport;

public class LogoutAction extends ActionSupport {
	private static final long serialVersionUID = 1L;

	@Override
	public String execute() throws Exception {
		RedisAddLoginUser logOut = new RedisAddLoginUser();
		logOut.logout();
		return NONE;
	}
}
