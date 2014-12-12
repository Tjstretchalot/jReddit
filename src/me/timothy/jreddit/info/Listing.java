package me.timothy.jreddit.info;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Describes http://reddit.com/
 * 
 * @author Timothy
 */
public class Listing {
	protected JSONObject object;
	protected JSONObject data;
	protected JSONArray children;
	
	public Listing(JSONObject jObject) {
		object = jObject;
		data = (JSONObject) object.get("data");
		children = (JSONArray) data.get("children");
	}
	
	public String kind() {
		return (String) object.get("kind");
	}
	
	public String modhash() {
		return (String) data.get("modhash");
	}
	
	public String after() {
		return (String) data.get("after");
	}
	
	public String before() {
		return (String) data.get("before");
	}
	
	public int numChildren() {
		return children.size();
	}
	
	public Thing getChild(int n) {
		return (Thing) Thing.parse((JSONObject) children.get(n));
	}

	@Override
	public String toString() {
		return "Listing [kind()=" + kind() + ", modhash()=" + modhash()
				+ ", after()=" + after() + ", before()=" + before()
				+ ", numChildren()=" + numChildren() + "]";
	}
}
