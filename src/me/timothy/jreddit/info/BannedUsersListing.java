package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

public class BannedUsersListing extends Listing {

	public BannedUsersListing(JSONObject jObject) {
		super(jObject);
	}

	@Override
	public Thing getChild(int index) {
		return getBannedUserInfo(index);
	}
	
	public BannedUserInfo getBannedUserInfo(int index)
	{
		return new BannedUserInfo((JSONObject)children.get(index));
	}
}
