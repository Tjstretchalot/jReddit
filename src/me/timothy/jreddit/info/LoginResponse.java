package me.timothy.jreddit.info;

import java.util.List;

import me.timothy.jreddit.requests.Utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class LoginResponse implements Errorable {
	protected JSONObject object;
	protected JSONObject jsonObj;
	
	public LoginResponse(JSONObject obj) {
		object = obj;
		jsonObj = (JSONObject) object.get("json");
	}
	
	public String cookie() {
		JSONObject data = (JSONObject) jsonObj.get("data");
		return (String) data.get("cookie");
	}
	
	public String modhash() {
		JSONObject data = (JSONObject) jsonObj.get("data");
		return (String) data.get("modhash");
	}
	
	@Override
	public List<?> getErrors() {
		return (JSONArray) jsonObj.get("errors");
	}
	
	@Override
	public String toString() {
		return Utils.getJSONDebugString(object);
	}
}
