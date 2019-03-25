package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

public class Message extends Thing implements Created {

	public Message(JSONObject obj) {
		super(obj);
	}
	
	public String author() {
		return getString("author", true);
	}
	
	public String body() {
		return getString("body", true);
	}
	
	public String bodyHTML() {
		return getString("body_html", true);
	}
	
	public String context() {
		return getString("context", true);
	}
	
	/**
	 * @return no idea, you deal with it
	 */
	public Object firstMessage() {
		return data.get("first_message");
	}
	
	public Boolean likes() {
		return (Boolean) data.get("likes");
	}
	
	public String linkTitle() {
		return getString("link_title", true);
	}
	
	public String name() {
		return getString("name", true);
	}
	
	public boolean isNew() {
		return getBoolean("new", true);
	}
	
	public String parentId() {
		return getString("parent_id", true);
	}
	
	public Object replies() { // TODO figure out how this works
		return data.get("replies");
	}
	
	public String subject() {
		return getString("subject", true);
	}
	
	public String subreddit() {
		return getString("subreddit", true);
	}
	
	public boolean wasComment() {
		return getBoolean("was_comment", true);
	}

	@Override
	public double created() {
		return getDouble("created", true);
	}

	@Override
	public double createdUTC() {
		return getDouble("created_utc", true);
	}

	@Override
	public String fullname() {
		return "t4_" + id();
	}

	@Override
	public String toString() {
		return "Message [author()=" + author() + ", body()=" + body()
				+ ", bodyHTML()=" + bodyHTML() + ", context()=" + context()
				+ ", firstMessage()=" + firstMessage() + ", likes()=" + likes()
				+ ", linkTitle()=" + linkTitle() + ", name()=" + name()
				+ ", isNew()=" + isNew() + ", parentId()=" + parentId()
				+ ", replies()=" + replies() + ", subject()=" + subject()
				+ ", subreddit()=" + subreddit() + ", wasComment()="
				+ wasComment() + ", created()=" + created() + ", createdUTC()="
				+ createdUTC() + ", fullname()=" + fullname() + ", id()="
				+ id() + ", kind()=" + kind() + "]";
	}
}
