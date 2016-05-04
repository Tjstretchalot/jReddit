package me.timothy.jreddit.info;

import org.json.simple.JSONObject;


/**
 * Describes the contributors listing you get from /r/&lt;subreddit&gt;/about/contributors.json
 * 
 * @author Timothy
 */
public class ContributorsListing extends Listing {

	public ContributorsListing(JSONObject jObject) {
		super(jObject);
	}

	@Override
	public Thing getChild(int n) {
		return getContributor(n);
	}

	/**
	 * The account only has name and id
	 * @param n the index
	 * @return the account
	 */
	@SuppressWarnings("unchecked")
	public Account getContributor(int n) {
		JSONObject jObjReal = (JSONObject) children.get(n);
		JSONObject jObjImproved = new JSONObject();
		jObjImproved.put("data", jObjReal);
		return new Account(jObjImproved);
	}
}
