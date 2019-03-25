package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

public class Comment extends VotableAndCreated {
	public Comment(JSONObject obj) {
		super(obj);
	}

	public String approvedBy() {
		return getString("approved_by", true);
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
	
	public String bannedBy() {
		return getString("banned_by", true);
	}
	
	public String body() {
		return getString("body", true).replaceAll("[\\s\\u0085\\p{Z}]", " ");
	}
	
	public String bodyHTML() {
		return getString("body_html", true);
	}
	
	public boolean edited() {
		return (data.get("edited") instanceof Long); 
	}
	
	public long editedTime() {
		return (data.get("edited") instanceof Long ? (long) data.get("edited") : -1);
	}
	
	/**
	 * @return number of times this has been gifted gold
	 */
	public int gilded() {
		return getInt("gilded", true);
	}
	
	public Boolean likes() {
		return getBoolean("likes", true); 
	}
	
	public String linkID() {
		return getString("link_id", true);
	}
	
	public String linkTitle() {
		return getString("link_title", true);
	}
	
	public String linkURL() {
		return getString("link_url", true);
	}
	
	@SuppressWarnings("unchecked")
	public void linkURL(String str) {
		data.put("link_url", str);
	}
	
	public String linkAuthor() {
		return getString("link_author", true);
	}
	
	@SuppressWarnings("unchecked")
	public void linkAuthor(String str) {
		data.put("link_author", str);
	}
	
	public Integer numReports() {
		return (Integer) data.get("num_reports");
	}
	
	public String parentId() {
		return getString("parent_id", true);
	}
	
	public boolean scoreHidden() {
		return getBoolean("score_hidden", true);
	}
	
	public String subreddit() {
		return getString("subreddit", true);
	}
	
	public String permalink() {
		return getString("permalink", true);
	}
	
	public String subredditId() {
		return getString("subreddit_id", true);
	}
	
	public String distinguished() {
		return getString("distinguished", true);
	}

	public Object getReplies() {
		if(data.get("replies") == null)
			return null;
		if(data.get("replies") instanceof String) {
			// empty
			return null;
		}
		return Thing.parse((JSONObject) data.get("replies"));
	}

	@Override
	public String fullname() {
		if(id().startsWith("t1_"))
			return id();
		return "t1_" + id();
	}

	@Override
	public String toString() {
		return "Comment [approvedBy()=" + approvedBy() + ", author()="
				+ author() + ", authorFlairCSSClass()=" + authorFlairCSSClass()
				+ ", authorFlairText()=" + authorFlairText() + ", bannedBy()="
				+ bannedBy() + ", body()=" + body() + ", bodyHTML()="
				+ bodyHTML() + ", edited()=" + edited() + ", editedTime()="
				+ editedTime() + ", gilded()=" + gilded() + ", likes()="
				+ likes() + ", linkID()=" + linkID() + ", linkTitle()="
				+ linkTitle() + ", numReports()=" + numReports()
				+ ", parentId()=" + parentId() + ", scoreHidden()="
				+ scoreHidden() + ", subreddit()=" + subreddit()
				+ ", subredditId()=" + subredditId() + ", distinguished()="
				+ distinguished() + ", fullname()=" + fullname() + ", ups()="
				+ ups() + ", downs()=" + downs() + ", created()=" + created()
				+ ", createdUTC()=" + createdUTC() + ", id()=" + id()
				+ ", name()=" + name() + ", kind()=" + kind() + "]";
	}
}
