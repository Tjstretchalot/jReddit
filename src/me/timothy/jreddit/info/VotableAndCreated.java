package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

public abstract class VotableAndCreated extends Thing implements Votable, Created {
	
	
	public VotableAndCreated(JSONObject obj) {
		super(obj);
	}

	@Override
	public int ups() {
		return getInt("ups", true);
	}

	@Override
	public int downs() {
		return getInt("downs", true);
	}

	@Override
	public Boolean likes() {
		return (Boolean) data.get("likes");
	}

	@Override
	public double created() {
		return getDouble("created", true);
	}

	@Override
	public double createdUTC() {
		return getDouble("created_utc", true);
	}
}
