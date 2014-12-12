package me.timothy.jreddit.info;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class More extends Thing {
	private List<String> children;
	
	public More(JSONObject obj) {
		super(obj);
	}
	
	public List<String> children() { 
		if(children != null)
			return new ArrayList<>(children);
		children = new ArrayList<>();
		
		JSONArray childrenArr = (JSONArray) data.get("children");
		for(int i = 0; i < childrenArr.size(); i++) {
			children.add((String) childrenArr.get(i));
		}
		
		return children;
	}
	
	public String childrenCSV() {
		if(children == null)
			children();
		
		if(children.isEmpty())
			return "";
		
		StringBuilder result = new StringBuilder(children.get(0));
		for(int i = 1; i < children.size(); i++) {
			result.append(",").append(children.get(i));
		}
		
		return result.toString();
	}

	@Override
	public String fullname() {
		return null;
	}

	@Override
	public String toString() {
		return "More [children=" + children + "]";
	}
}
