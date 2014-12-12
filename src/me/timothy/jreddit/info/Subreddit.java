package me.timothy.jreddit.info;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Subreddit extends Thing {

	public Subreddit(JSONObject obj) {
		super(obj);
	}

	public int accountsActive() {
		return ((Long) data.get("accounts_active")).intValue();
	}
	
	public int commentScoreHideMins() {
		return ((Long) data.get("comment_score_hide_mins")).intValue();
	}
	
	public String description() {
		return (String) data.get("description");
	}
	
	public String descriptionHTML() {
		return (String) data.get("description_html");
	}
	
	public String displayName() {
		return (String) data.get("display_name");
	}
	
	public String headerImage() {
		return (String) data.get("header_img");
	}
	
	public boolean hasHeaderSize() {
		return data.get("header_size") != null;
	}
	
	public int headerWidth() {
		return ((Long) ((JSONArray) data.get("header_size")).get(0)).intValue(); // TODO: test
	}
	
	public int headerHeight() {
		return ((Long) ((JSONArray) data.get("header_size")).get(1)).intValue(); // TODO: test
	}
	
	public String headerTitle() {
		return (String) data.get("header_title");
	}
	
	public boolean over18() {
		return (boolean) data.get("over18");
	}
	
	public String publicDescription() {
		return (String) data.get("public_description");
	}
	
	public boolean publicTraffic() {
		return (boolean) data.get("public_traffic");
	}
	
	public long subscribers() {
		return (long) data.get("subscribers");
	}
	
	/**
	 * @return 'any', 'link', or 'self'
	 */
	public String submissionType() {
		return (String) data.get("submissionType");
	}
	
	public String submitLinkLabel() {
		return (String) data.get("submit_link_label");
	}
	
	public String submitTextLabel() {
		return (String) data.get("submit_text_label");
	}
	
	public String subredditType() {
		return (String) data.get("subreddit_type");
	}
	
	public String title() {
		return (String) data.get("title");
	}
	
	public String url() {
		return (String) data.get("url");
	}
	
	public boolean userIsBanned() {
		return (boolean) data.get("user_is_banned");
	}
	
	public boolean userIsContributor() {
		return (boolean) data.get("user_is_contributor");
	}
	
	public boolean userIsModerator() {
		return (boolean) data.get("user_is_moderator");
	}
	
	public boolean userIsSubscriber() {
		return (boolean) data.get("user_is_subscriber");
	}

	@Override
	public String fullname() {
		return "t5_" + id();
	}

	@Override
	public String toString() {
		return "Subreddit [accountsActive()=" + accountsActive()
				+ ", commentScoreHideMins()=" + commentScoreHideMins()
				+ ", description()=" + description() + ", descriptionHTML()="
				+ descriptionHTML() + ", displayName()=" + displayName()
				+ ", headerImage()=" + headerImage() + ", hasHeaderSize()="
				+ hasHeaderSize() + ", headerWidth()=" + headerWidth()
				+ ", headerHeight()=" + headerHeight() + ", headerTitle()="
				+ headerTitle() + ", over18()=" + over18()
				+ ", publicDescription()=" + publicDescription()
				+ ", publicTraffic()=" + publicTraffic() + ", subscribers()="
				+ subscribers() + ", submissionType()=" + submissionType()
				+ ", submitLinkLabel()=" + submitLinkLabel()
				+ ", submitTextLabel()=" + submitTextLabel()
				+ ", subredditType()=" + subredditType() + ", title()="
				+ title() + ", url()=" + url() + ", userIsBanned()="
				+ userIsBanned() + ", userIsContributor()="
				+ userIsContributor() + ", userIsModerator()="
				+ userIsModerator() + ", userIsSubscriber()="
				+ userIsSubscriber() + ", fullname()=" + fullname() + ", id()="
				+ id() + ", name()=" + name() + ", kind()=" + kind() + "]";
	}
}
