package me.timothy.jreddit.info;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Subreddit extends Thing {

	public Subreddit(JSONObject obj) {
		super(obj);
	}

	public int accountsActive() {
		return getInt("accounts_active", true);
	}
	
	public int commentScoreHideMins() {
		return getInt("comment_score_hide_mins", true);
	}
	
	public String description() {
		return getString("description", true);
	}
	
	public String descriptionHTML() {
		return getString("description_html", true);
	}
	
	public String displayName() {
		return getString("display_name", true);
	}
	
	public String headerImage() {
		return getString("header_img", true);
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
		return getString("header_title", true);
	}
	
	public boolean over18() {
		return getBoolean("over18", true);
	}
	
	public String publicDescription() {
		return getString("public_description", true);
	}
	
	public boolean publicTraffic() {
		return getBoolean("public_traffic", true);
	}
	
	public long subscribers() {
		return getLong("subscribers", true);
	}
	
	/**
	 * @return 'any', 'link', or 'self'
	 */
	public String submissionType() {
		return getString("submissionType", true);
	}
	
	public String submitLinkLabel() {
		return getString("submit_link_label", true);
	}
	
	public String submitTextLabel() {
		return getString("submit_text_label", true);
	}
	
	public String subredditType() {
		return getString("subreddit_type", true);
	}
	
	public String title() {
		return getString("title", true);
	}
	
	public String url() {
		return getString("url", true);
	}
	
	public boolean userIsBanned() {
		return getBoolean("user_is_banned", true);
	}
	
	public boolean userIsContributor() {
		return getBoolean("user_is_contributor", true);
	}
	
	public boolean userIsModerator() {
		return getBoolean("user_is_moderator", true);
	}
	
	public boolean userIsSubscriber() {
		return getBoolean("user_is_subscriber", true);
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
