package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

/**
 * These things are returned from 
 * https://reddit.com/r/-sub-/about/log.json
 * 
 * @author Timothy
 */
public class ModAction extends Thing {

	public ModAction(JSONObject object) {
		super(object);
	}

	@Override
	public String fullname() {
		return id();
	}
	
	@SuppressWarnings("unchecked")
	public String description() {
		return (String)data.getOrDefault("description", null);
	}
	
	@SuppressWarnings("unchecked")
	public String targetBody() {
		return (String)data.getOrDefault("target_body", null);
	}

	@SuppressWarnings("unchecked")
	public String modID36() {
		return (String)data.getOrDefault("mod_id36", null);
	}

	@SuppressWarnings("unchecked")
	public double createdUTC() {
		return (double)data.getOrDefault("created_utc", 0);
	}

	@SuppressWarnings("unchecked")
	public String subreddit() {
		return (String)data.getOrDefault("subreddit", null);
	}

	/**
	 * This is null for comments
	 * @return target_title
	 */
	@SuppressWarnings("unchecked")
	public String targetTitle() {
		return (String)data.getOrDefault("target_title", null);
	}

	@SuppressWarnings("unchecked")
	public String targetPermalink() {
		return (String)data.getOrDefault("target_permalink", null);
	}
	
	/**
	 * If subreddit is fakesubreddit, this is r/fakesubreddit
	 * @return subreddit_name_prefixed
	 */
	@SuppressWarnings("unchecked")
	public String subredditNamePrefixed() {
		return (String)data.getOrDefault("subreddit_name_prefixed", null);
	}
	
	@SuppressWarnings("unchecked")
	public String details() {
		return (String)data.getOrDefault("details", null);
	}

	@SuppressWarnings("unchecked")
	public String action() {
		return (String)data.getOrDefault("action", null);
	}

	@SuppressWarnings("unchecked")
	public String targetAuthor() {
		return (String)data.getOrDefault("target_author", null);
	}

	@SuppressWarnings("unchecked")
	public String targetFullname() {
		return (String)data.getOrDefault("target_fullname", null);
	}

	@SuppressWarnings("unchecked")
	public String srID36() {
		return (String)data.getOrDefault("sr_id36", null);
	}
	
	@SuppressWarnings("unchecked")
	public String mod() {
		return (String)data.getOrDefault("mod", null);
	}
}
