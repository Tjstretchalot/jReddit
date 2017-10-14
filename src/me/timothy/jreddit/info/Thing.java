package me.timothy.jreddit.info;

import org.json.simple.JSONObject;

public abstract class Thing {
	protected JSONObject object;
	protected JSONObject data;
	
	public Thing(JSONObject object) {
		this.object = object;
		
		if(object.containsKey("data"))
			data = (JSONObject) object.get("data");
		else
			data = object;
	}
	
	public String id() {
		return (String) data.get("id");
	}
	
	public String name() {
		return (String) data.get("name");
	}
	
	public String kind() {
		return (String) object.get("kind"); // this always appears to be in the right spot
	}

	public abstract String fullname();
	
	public static Object parse(JSONObject obj) {
		String kind = (String) obj.get("kind");
		
		switch(kind) {
		case "Listing":
			return new Listing(obj);
		case "more":
			return new More(obj);
		case "modaction":
			return new ModAction(obj);
		case "t1":
			return new Comment(obj);
		case "t2":
			return new Account(obj);
		case "t3":
			return new Link(obj);
		case "t4":
			return new Message(obj);
		case "t5":
			return new Subreddit(obj);
		case "t6":
			return new Award(obj);
		case "t8":
			return new PromoCampaign(obj);
		}
		return null;
	}

	public JSONObject getJsonObject() {
		return object;
	}
	
	@Override
	public String toString() {
		return "Thing [id()=" + id() + ", name()=" + name() + ", kind()="
				+ kind() + ", fullname()=" + fullname() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id().hashCode();
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Comment))
				return false;
		Comment com = (Comment) o;
		
		return com.id().equals(id());
	}
}
