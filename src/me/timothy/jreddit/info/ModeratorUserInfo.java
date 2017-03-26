package me.timothy.jreddit.info;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Describes an item of the result of /about/moderators, which is a kind UserList.
 * 
 * @author Timothy
 */
public class ModeratorUserInfo extends Thing {

	public ModeratorUserInfo(JSONObject object) {
		super(object);
	}

	/**
	 * Gets the users flair class, which is often null
	 * @return the users flair class
	 */
	public String authorFlairCSSClass() {
		if(!object.containsKey("author_flair_css_class"))
			return null;
		
		return (String) object.get("author_flair_css_class");
	}
	
	/**
	 * The text that the users flair text, which is often null
	 * @return the users flair text
	 */
	public String authorFlairText() {
		if(!object.containsKey("author_flair_text"))
			return null;
		
		return (String) object.get("author_flair_text");
	}
	
	/**
	 * Gets what permissions as a moderator this has. Can be all
	 * or any combination of 
	 * 
	 * <ul>
	 *   <li>access</li>
	 *   <li>config</li>
	 *   <li>flair</li>
	 *   <li>mail</li>
	 *   <li>posts</li>
	 *   <li>wiki</li>
	 * </ul>
	 * @return moderator permissions
	 */
	public List<String> modPermissions() {
		List<String> result = new ArrayList<String>();
		
		JSONArray permissionsArr = (JSONArray) object.get("mod_permissions");
		for(int i = 0; i < permissionsArr.size(); i++) {
			result.add((String) permissionsArr.get(i));
		}
		
		return result;
	}
	
	/**
	 * Get time this moderator was added in seconds since epoch.
	 * 
	 * @return time moderator was added
	 */
	public long date() {
		return ((Number) object.get("date")).longValue();
	}
	
	/**
	 * Get the name of the moderator
	 * @return moderators name
	 */
	public String name() {
		return (String) object.get("name");
	}
	
	/**
	 * Gets the id of this user, without the t2_ prefix
	 */
	@Override
	public String id() {
		return ((String) object.get("id")).substring(3);
	}
	
	/**
	 * Gets the fullname of this user, including the t2_ prefix
	 */
	@Override
	public String fullname() {
		return (String) object.get("id");
	}

}
