package com.cachedata.login;

import java.sql.Connection;

import redis.clients.jedis.Jedis;

public class RedisAddLoginUser {
	
	String userName = "none";		
	String categary = "none";
	String jobName = "none";
	
	Connection connect;
	
	public RedisAddLoginUser() {}
	
	public RedisAddLoginUser(String userName,String categary) {
		this.userName = userName;
		this.categary = categary;
	}
	
	public RedisAddLoginUser(String userName,String categary,String jobName) {
		this.userName = userName;
		this.categary = categary;
		this.jobName = jobName;
	}
	
	

	public void login() {
		Jedis jedis = new Jedis("localhost");
		jedis.set("userName", userName);
		jedis.set("categary", categary);
		jedis.set("jobName", jobName);
		jedis.set("entryDetail", "LOGIN");
	}
	
	public void logout() {
		Jedis jedis = new Jedis("localhost");
		jedis.set("entryDetail", "LOGOUT");
	}
}
