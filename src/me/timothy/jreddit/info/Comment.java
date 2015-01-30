package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

public class Comment extends VotableAndCreated {
	public Comment(JSONObject obj) {
		super(obj);
	}

	public String approvedBy() {
		return (String) data.get("approved_by");
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
	
	public String bannedBy() {
		return (String) data.get("banned_by");
	}
	
	public String body() {
		return (String) data.get("body");
	}
	
	public String bodyHTML() {
		return (String) data.get("body_html");
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
		return ((Long) data.get("gilded")).intValue();
	}
	
	public Boolean likes() {
		return (Boolean) data.get("likes"); 
	}
	
	public String linkID() {
		return (String) data.get("link_id");
	}
	
	public String linkTitle() {
		return (String) data.get("link_title");
	}
	
	public String linkURL() {
		return (String) data.get("link_url");
	}
	
	public String linkAuthor() {
		return (String) data.get("link_author");
	}
	
	public Integer numReports() {
		return (Integer) data.get("num_reports");
	}
	
	public String parentId() {
		return (String) data.get("parent_id");
	}
	
	public boolean scoreHidden() {
		return (boolean) data.get("score_hidden");
	}
	
	public String subreddit() {
		return (String) data.get("subreddit");
	}
	
	public String subredditId() {
		return (String) data.get("subreddit_id");
	}
	
	public String distinguished() {
		return (String) data.get("distinguished");
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
