package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

/**
 * Wikipage will have no fullname or id
 * @author Timothy
 *
 */
public class Wikipage extends Thing {

	public Wikipage(JSONObject object) {
		super(object);
	}

	@Override
	public String fullname() {
		throw new RuntimeException("Wikipage has no fullname / id");
	}
	
	public String contentMarkdown() {
		return (String) data.get("content_md");
	}
	
	public String contentHTML() {
		return (String) data.get("content_html");
	}
	
	public double revisionDate() {
		return ((Number) data.get("revision_date")).doubleValue();
	}
	
	public boolean mayRevise() {
		return ((Boolean) data.get("may_revise")).booleanValue();
	}
	
	/**
	 * The account has only the following fields:
	 * 
	 * is_employee, icon_img, pref_show_snoovatar, name, is_friend, created,
	 * has_subscribed, hide_from_robots, created_utc, link_karma, comment_karma, is_gold,
	 * is_mod, verified, subreddit (always null), has_verified_email, id
	 * 
	 * @return who did the latest revision
	 */
	public Account revisionBy() {
		return new Account((JSONObject) data.get("revision_by"));
	}
}
