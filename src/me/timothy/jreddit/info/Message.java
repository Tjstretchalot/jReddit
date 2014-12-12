package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

public class Message extends Thing implements Created {

	public Message(JSONObject obj) {
		super(obj);
	}
	
	public String author() {
		return (String) data.get("author");
	}
	
	public String body() {
		return (String) data.get("body");
	}
	
	public String bodyHTML() {
		return (String) data.get("body_html");
	}
	
	public String context() {
		return (String) data.get("context");
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
		return (String) data.get("link_title");
	}
	
	public String name() {
		return (String) data.get("name");
	}
	
	public boolean isNew() {
		return (boolean) data.get("new");
	}
	
	public String parentId() {
		return (String) data.get("parent_id");
	}
	
	public Object replies() { // TODO figure out how this works
		return data.get("replies");
	}
	
	public String subject() {
		return (String) data.get("subject");
	}
	
	public String subreddit() {
		return (String) data.get("subreddit");
	}
	
	public boolean wasComment() {
		return (boolean) data.get("was_comment");
	}

	@Override
	public double created() {
		return (double) data.get("created");
	}

	@Override
	public double createdUTC() {
		return (double) data.get("created_utc");
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
