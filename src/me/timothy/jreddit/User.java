package me.timothy.jreddit;



/**
 * This class represents a reddit user.
 * 
 * @author Timothy
 */
public class User {
	private String username;
	private String password;
	
	private String modhash;
	private String cookie;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setModhash(String newModhash) {
		modhash = newModhash;
	}
	
	public String getModhash() {
		return modhash;
	}
	
	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
}
