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
		if(!obj.containsKey("kind"))
			return new MalformedThing(obj);
		
		String kind = (String) obj.get("kind");
		
		switch(kind) {
		case "Listing":
			return new Listing(obj);
		case "more":
			return new More(obj);
		case "modaction":
			return new ModAction(obj);
		case "wikipage":
			return new Wikipage(obj);
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
	
	/**
	 * Gets the string value associated with the given key. Raises an error 
	 * if missing.
	 * 
	 * @param key the key
	 * @param data if false, uses the object itself. if true, uses object['data']
	 * @return the associated string value
	 */
	public String getString(String key, boolean data) {
		if(data) { return (String) this.data.get(key); }
		
		return (String)object.get(key);
	}
	
	/**
	 * @see #getString(String, boolean)
	 */
	public String getString(String key) { return getString(key, false); }
	
	/**
	 * Gets the double value associated with the given key. Raises an error
	 * if missing.
	 * 
	 * @param key the key
	 * @param data if false, uses the object itself. if true, uses object['data']
	 * @return the associated double value
	 */
	public double getDouble(String key, boolean data) {
		if(data) { return ((Number)this.data.get(key)).doubleValue(); }
		
		return ((Number)object.get(key)).doubleValue();
	}
	
	/**
	 * @see #getDouble(String, boolean)
	 */
	public double getDouble(String key) { return getDouble(key, false); }
	
	/**
	 * Gets the long value associated with the given key. Raises an error if 
	 * missing.
	 * 
	 * @param key the key
	 * @param data if false, uses the object itself. if true, uses object['data']
	 * @return the associated long value
	 */
	public long getLong(String key, boolean data) {
		if(data) { return ((Number)this.data.get(key)).longValue(); }
		return ((Number)object.get(key)).longValue();
	}
	
	/**
	 * @see #getLong(String, boolean)
	 */
	public long getLong(String key) { return getLong(key, false); }
	
	/**
	 * Gets the int value associated with the given key. Raises an error if 
	 * missing.
	 * 
	 * @param key the key
	 * @param data if false, uses the object itself. If true, uses object['data']
	 * @return the associated int value
	 */
	public int getInt(String key, boolean data) {
		if(data) { return ((Number)this.data.get(key)).intValue(); }
		return ((Number)object.get(key)).intValue();
	}
	
	/**
	 * @see #getInt(String, boolean)
	 */
	public int getInt(String key) { return getInt(key, false); }
	
	/**
	 * Gets the boolean value associated with the given key. Raises an error if
	 * missing.
	 * 
	 * @param key the key
	 * @param data if false, uses the object itself. if true, uses object['data']
	 * @return the associated boolean value
	 */
	public boolean getBoolean(String key, boolean data) {
		if(data) { return ((Boolean)this.data.get(key)).booleanValue(); } 
		return ((Boolean)object.get(key)).booleanValue();
	}
	
	/**
	 * @see #getBoolean(String, boolean)
	 */
	public boolean getBoolean(String key) { return getBoolean(key, false); }
	
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
