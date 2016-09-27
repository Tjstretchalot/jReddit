package me.timothy.jreddit;

import me.timothy.jreddit.info.LoginResponse;

/**
 * This class represents a reddit user.
 * 
 * @author Timothy
 */
public class User {
	private String username;
	private String password;
	private String appClientID;
	private String appClientSecret;
	
	private LoginResponse loginResponse;

	public User(String username, String password, String appClientID, String appClientSecret) {
		this.username = username;
		this.password = password;
		this.appClientID = appClientID;
		this.appClientSecret = appClientSecret;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getAppClientID() {
		return appClientID;
	}
	
	public String getAppClientSecret() {
		return appClientSecret;
	}

	public LoginResponse getLoginResponse() {
		return loginResponse;
	}

	public void setLoginResponse(LoginResponse loginResponse) {
		this.loginResponse = loginResponse;
	}
}
