package me.timothy.jreddit.info;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CommentResponse implements Errorable {
	private JSONObject object;
	private JSONObject json;

	public CommentResponse(JSONObject jObject) {
		this.object = jObject;
		this.json = (JSONObject) jObject.get("json");
	}
	
	/**
	 * This is not anything close to a complete comment, but it calls it a comment (kind=t1), so
	 * I return it as such. For anything meaningful, grab the id and refetch the comment
	 * 
	 * @return the comment that was just made
	 */
	public Comment getComment() {
		JSONObject json_data = (JSONObject) json.get("data");
		JSONArray data_things = (JSONArray) json_data.get("things");
		
		return new Comment((JSONObject) data_things.get(0));
	}

	@Override
	public List<?> getErrors() {
		if(object.containsKey("success")) {
			Boolean success = (Boolean) object.get("success");
			
			if(success == Boolean.TRUE) {
				return new ArrayList<>();
			}
			
			return null;
		}else if(json != null) {
			return (List<?>) json.get("errors");
		}else {
			return new ArrayList<>();
		}
	}
	
	@Override
	public String toString() {
		return "CommentResponse [object=" + object + ", json=" + json + "]";
	}
}
