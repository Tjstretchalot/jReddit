package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

public class Link extends VotableAndCreated {
	public Link(JSONObject obj) {
		super(obj);
	}
	
	public String author() {
		return getString("author", true);
	}
	
	public String authorFlairCSSClass() {
		return getString("author_flair_css_class", true);
	}
	
	public String authorFlairText() {
		return getString("author_flair_text", true);
	}
	
	public boolean clicked() {
		return getBoolean("clicked", true);
	}
	
	/**
	 * @return the top level domain that this submission links to. Self posts are self.*subreddit*
	 */
	public String domain() {
		return getString("domain", true);
	}
	
	public boolean hidden() {
		return getBoolean("hidden", true);
	}
	
	public boolean isSelf() {
		return getBoolean("is_self", true);
	}
	
	public String linkFlairCSSClass() {
		return getString("link_flair_css_class", true);
	}
	
	public String linkFlairText() {
		return getString("link_flair_text", true);
	}
	
	public JSONObject media() {
		return (JSONObject) data.get("media");
	}
	
	public JSONObject getMediaEmbed() {
		return (JSONObject) data.get("media_embed"); 
	}
	
	public int numComments() {
		return getInt("num_comments", true);
	}
	
	public boolean over18() {
		return getBoolean("over18", true);
	}
	
	public String permalink() {
		return getString("permalink", true);
	}
	
	/**
	 * @return true if the user is logged in and has saved this post, false otherwise
	 */
	public boolean saved() {
		return getBoolean("saved", true);
	}
	
	public int score() {
		return getInt("score", true);
	}
	
	public String selftext() {
		return getString("selftext", true);
	}
	
	public String selftextHTML() {
		return getString("selftext_html", true);
	}
	
	public String subreddit() {
		return getString("subreddit", true);
	}
	
	public String subredditID() {
		return getString("subreddit_id", true);
	}
	
	public String thumbnail() {
		return getString("thumnail", true);
	}
	
	public String title() {
		return getString("title", true);
	}
	
	public String url() {
		return getString("url", true);
	}
	
	public boolean edited() {
		if(data.get("edited") instanceof Boolean) {
			return (Boolean) data.get("edited"); // currently, if it is a boolean, this is always false
		}
		
		return true; // it should be a long, in this case
	}
	
	public long timeEdited() {
		if(!(data.get("edited") instanceof Number))
			return -1;
		
		return (long) data.get("edited");
	}
	
	/**
	 * null = not distinguished
	 * 
	 * For the rest, the user had the option to distinguish himself,
	 * and thus this cannot be used for determining whether the user is
	 * not a moderator/admin/other, just if (s)he is.
	 * 
	 * moderator = a moderator posted this
	 * admin = an admin posted this
	 * special = something else
	 * 
	 * @return if this post is distinguished
	 */
	public String distinguished() {
		return getString("distinguished", true);
	}
	
	public boolean stickied() {
		return getBoolean("stickied", true);
	}
	
	// here on out are not documented on the JSON page, so might not always be there
	
	public Object numReports() {
		return data.get("num_reports");
	}
	
	/**
	 * @return the moderator who banned this submission. Normally null
	 */
	public String bannedBy() {
		return getString("banned_by", true);
	}
	
	/**
	 * @return the moderator who approved this submission
	 */
	public String approvedBy() {
		return getString("approved_by", true);
	}

	@Override
	public String fullname() {
		return "t3_" + id();
	}	
}