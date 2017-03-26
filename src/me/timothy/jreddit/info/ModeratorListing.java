package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

/**
 * Describes the result of /about/moderators, which is a UserList
 * but looks exactly like a listing and is only used here, as far
 * as I can tell
 * 
 * @author Timothy
 */
public class ModeratorListing extends Listing {
	public ModeratorListing(JSONObject jObject) {
		super(jObject);
	}

	@Override
	public Thing getChild(int index) {
		return getModerator(index);
	}
	
	public ModeratorUserInfo getModerator(int index) {
		return new ModeratorUserInfo((JSONObject) children.get(index));
	}
}
