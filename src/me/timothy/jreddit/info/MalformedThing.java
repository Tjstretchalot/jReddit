package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

/**
 * Describes a thing which is missing a "kind"
 * 
 * @author Timothy
 */
public class MalformedThing extends Thing {
	
	public MalformedThing(JSONObject object) {
		super(object);
	}

	@Override
	public String fullname() {
		if(object.containsKey("id"))
			return id();
		return null;
	}
}
