package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

public class Account extends Thing implements Created {
	
	public Account(JSONObject obj) {
		super(obj);
	}

	public int commentKarma() {
		return getInt("comment_karma", true);
	}

	public boolean hasMail() {
		return getBoolean("has_mail", true);
	}
	
	public boolean hasModMail() {
		return getBoolean("has_mod_mail", true);
	}
	
	public boolean hasVerifiedEmail() {
		return getBoolean("has_verified_email", true);
	}
	
	public String id() {
		return getString("id", true);
	}

	public boolean isFriend() {
		return getBoolean("is_friend", true);
	}
	
	public boolean isGold() {
		return getBoolean("is_gold", true);
	}
	
	public int linkKarma() {
		return getInt("link_karma", true);
	}
	
	public String modhash() {
		return getString("modhash", true);
	}
	
	public String name() {
		return getString("name", true);
	}
	
	public boolean over18() {
		return getBoolean("over18", true);
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
