package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

public class Account extends Thing implements Created {
	
	public Account(JSONObject obj) {
		super(obj);
	}

	public int commentKarma() {
		return ((Long) data.get("comment_karma")).intValue();
	}

	public boolean hasMail() {
		return (boolean) data.get("has_mail");
	}
	
	public boolean hasModMail() {
		return (boolean) data.get("has_mod_mail");
	}
	
	public boolean hasVerifiedEmail() {
		return (boolean) data.get("has_verified_email");
	}
	
	public String id() {
		return (String) data.get("id");
	}

	public boolean isFriend() {
		return (boolean) data.get("is_friend");
	}
	
	public boolean isGold() {
		return (boolean) data.get("is_gold");
	}
	
	public int linkKarma() {
		return ((Long) data.get("link_karma")).intValue();
	}
	
	public String modhash() {
		return (String) data.get("modhash");
	}
	
	public String name() {
		return (String) data.get("name");
	}
	
	public boolean over18() {
		return (boolean) data.get("over18");
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
		return "t2_" + id();
	}

	@Override
	public String toString() {
		return "Account [commentKarma()=" + commentKarma() + ", hasMail()="
				+ hasMail() + ", hasModMail()=" + hasModMail()
				+ ", hasVerifiedEmail()=" + hasVerifiedEmail() + ", id()="
				+ id() + ", isFriend()=" + isFriend() + ", isGold()="
				+ isGold() + ", linkKarma()=" + linkKarma() + ", modhash()="
				+ modhash() + ", name()=" + name() + ", over18()=" + over18()
				+ ", created()=" + created() + ", createdUTC()=" + createdUTC()
				+ ", fullname()=" + fullname() + ", kind()=" + kind() + "]";
	}
	
	public String toPrettyString() {
		StringBuilder result = new StringBuilder();
		result.append(name()).append("\n");
		result.append("Comment Karma: ").append(commentKarma()).append("\n");
		result.append("Link Karma   : ").append(linkKarma()).append("\n");
		result.append("Has Mail     : ").append(hasMail()).append("\n");
		result.append("Has Mod Mail : ").append(hasModMail()).append("\n");
		result.append("Has Gold     : ").append(isGold()).append("\n");
		result.append("Verified Mail: ").append(hasVerifiedEmail()).append("\n");
		return result.toString();
	}
}
