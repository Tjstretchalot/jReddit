package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

public class Link extends VotableAndCreated {
	public Link(JSONObject obj) {
		super(obj);
	}
	
	public String author() {
		return (String) data.get("author");
	}
	
	public String authorFlairCSSClass() {
		return (String) data.get("author_flair_css_class");
	}
	
	public String authorFlairText() {
		return (String) data.get("author_flair_text");
	}
	
	public boolean clicked() {
		return (Boolean) data.get("clicked");
	}
	
	/**
	 * @return the top level domain that this submission links to. Self posts are self.*subreddit*
	 */
	public String domain() {
		return (String) data.get("domain");
	}
	
	public boolean hidden() {
		return (Boolean) data.get("hidden");
	}
	
	public boolean isSelf() {
		return (Boolean) data.get("is_self");
	}
	
	public String linkFlairCSSClass() {
		return (String) data.get("link_flair_css_class");
	}
	
	public String linkFlairText() {
		return (String) data.get("link_flair_text");
	}
	
	public JSONObject media() {
		return (JSONObject) data.get("media");
	}
	
	public JSONObject getMediaEmbed() {
		return (JSONObject) data.get("media_embed"); 
	}
	
	public int numComments() {
		return ((Long) data.get("num_comments")).intValue();
	}
	
	public boolean over18() {
		return (Boolean) data.get("over18");
	}
	
	public String permalink() {
		return (String) data.get("permalink");
	}
	
	/**
	 * @return true if the user is logged in and has saved this post, false otherwise
	 */
	public boolean saved() {
		return (Boolean) data.get("saved");
	}
	
	public int score() {
		return ((Long) data.get("score")).intValue();
	}
	
	public String selftext() {
		return (String) data.get("selftext");
	}
	
	public String selftextHTML() {
		return (String) data.get("selftext_html");
	}
	
	public String subreddit() {
		return (String) data.get("subreddit");
	}
	
	public String subredditID() {
		return (String) data.get("subreddit_id");
	}
	
	public String thumbnail() {
		return (String) data.get("thumbnail");
	}
	
	public String title() {
		return (String) data.get("title");
	}
	
	public String url() {
		return (String) data.get("url");
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
		return (String) data.get("distinguished");
	}
	
	public boolean stickied() {
		return (Boolean) data.get("stickied");
	}
	
	// here on out are not documented on the JSON page, so might not always be there
	
	public Object numReports() {
		return data.get("num_reports");
	}
	
	/**
	 * @return the moderator who banned this submission. Normally null
	 */
	public String bannedBy() {
		return (String) data.get("banned_by");
	}
	
	/**
	 * @return the moderator who approved this submission
	 */
	public String approvedBy() {
		return (String) data.get("approved_by");
	}

	@Override
	public String fullname() {
		return "t3_" + id();
	}	
}