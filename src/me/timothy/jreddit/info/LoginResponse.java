package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

public class LoginResponse implements IAuthInfo {
	protected String accessToken;
	protected long expiresIn;
	protected String scope;
	protected String tokenType;
	protected long acquiredAt;
	
	public LoginResponse(JSONObject obj) {
		if(obj.containsKey("access_token")) {
			accessToken = (String) obj.get("access_token");
		}
		if(obj.containsKey("expires_in")) {
			expiresIn = ((Number) obj.get("expires_in")).longValue();
		}
		if(obj.containsKey("scope")) {
			scope = (String) obj.get("scope");
		}
		if(obj.containsKey("token_type")) {
			tokenType = (String) obj.get("token_type");
		}
		
		acquiredAt = System.currentTimeMillis();
	}
	
	public LoginResponse(String accessToken, long expiresIn, String scope, String tokenType, long acquiredAt) {
		this.accessToken = accessToken;
		this.expiresIn = expiresIn; // seconds since acquired at
		this.scope = scope;
		this.tokenType = tokenType;
		this.acquiredAt = acquiredAt; // ms since epoch
	}
	
	public String accessToken() {
		return accessToken;
	}
	
	public long expiresIn() {
		return expiresIn;
	}
	
	public String scope() {
		return scope;
	}
	
	public String tokenType() {
		return tokenType;
	}
	
	public long acquiredAt() {
		return acquiredAt;
	}

	@Override
	public String toString() {
		return "LoginResponse [accessToken=" + accessToken + ", expiresIn=" + expiresIn + ", scope=" + scope
				+ ", tokenType=" + tokenType + ", acquiredAt=" + acquiredAt + "]";
	}
}
