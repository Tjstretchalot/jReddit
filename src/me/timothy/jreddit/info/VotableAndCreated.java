package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

public abstract class VotableAndCreated extends Thing implements Votable, Created {
	
	
	public VotableAndCreated(JSONObject obj) {
		super(obj);
	}

	@Override
	public int ups() {
		return ((Long) data.get("ups")).intValue();
	}

	@Override
	public int downs() {
		return ((Long) data.get("downs")).intValue();
	}

	@Override
	public Boolean likes() {
		return (Boolean) data.get("likes");
	}

	@Override
	public double created() {
		return (double) data.get("created");
	}

	@Override
	public double createdUTC() {
		return (double) data.get("created_utc");
	}
}
