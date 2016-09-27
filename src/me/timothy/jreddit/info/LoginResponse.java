package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

import me.timothy.jreddit.requests.Utils;

public class LoginResponse implements IAuthInfo {
	protected JSONObject object;
	protected long acquiredAt;
	
	public LoginResponse(JSONObject obj) {
		object = obj;
		acquiredAt = System.currentTimeMillis();
	}
	
	public String accessToken() {
		return (String) object.get("access_token");
	}
	
	public long expiresIn() {
		return ((Number) object.get("expires_in")).longValue();
	}
	
	public String scope() {
		return (String) object.get("scope");
	}
	
	public String tokenType() {
		return (String) object.get("token_type");
	}
	
	public long acquiredAt() {
		return acquiredAt;
	}
	
	@Override
	public String toString() {
		return Utils.getJSONDebugString(object);
	}
}
