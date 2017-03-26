package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

/**
 * Describes information recieved from /about/banned
 * 
 * @author Timothy
 */
public class BannedUserInfo extends Thing {
	public BannedUserInfo(JSONObject jsonObject) {
		super(jsonObject);
	}
	
	/**
	 * This is seconds since epoch.
	 * 
	 * @return the date the user was banned
	 */
	public long date() {
		return ((Number) object.get("date")).longValue();
	}
	
	/**
	 * The note that was specified with the ban, visible only
	 * to moderators
	 * 
	 * @return the note
	 */
	public String note() {
		return (String) object.get("note");
	}
	
	/**
	 * The name of the user that is banned
	 * 
	 * @return the banned username
	 */
	public String name() {
		return (String) object.get("name");
	}
	
	/**
	 * This is the sent as the fullname, but we will return the
	 * actual id of the user (no t2_) for the sake of some
	 * consistency
	 * 
	 * @return the user id
	 */
	@Override
	public String id() {
		return ((String) object.get("id")).substring(3);
	}

	/**
	 * The fullname of the user, prefixed with t2_
	 */
	@Override
	public String fullname() {
		return (String) object.get("id");
	}
}
