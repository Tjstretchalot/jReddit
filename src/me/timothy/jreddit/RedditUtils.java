package me.timothy.jreddit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import me.timothy.jreddit.info.Account;
import me.timothy.jreddit.info.BannedUsersListing;
import me.timothy.jreddit.info.CommentResponse;
import me.timothy.jreddit.info.ContributorsListing;
import me.timothy.jreddit.info.Errorable;
import me.timothy.jreddit.info.Listing;
import me.timothy.jreddit.info.LoginResponse;
import me.timothy.jreddit.info.ModeratorListing;
import me.timothy.jreddit.info.More;
import me.timothy.jreddit.info.Thing;
import me.timothy.jreddit.info.Wikipage;
import me.timothy.jreddit.requests.Request;
import me.timothy.jreddit.requests.RequestHandler;
import me.timothy.jreddit.requests.RequestShell;
import me.timothy.jreddit.requests.Utils;

public class RedditUtils {
	public static final RequestHandler requestHandler;
	
	static {
		File dir = new File("data");
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		File requestsIni = new File(dir, "requests.ini");
		if(!requestsIni.exists()) {
//			try {
//				HttpURLConnection con = (HttpURLConnection) (new URL("http://umad-barnyard.com/requests.ini")).openConnection();
//				InputStream inStream = con.getInputStream();
//				Files.copy(inStream, requestsIni.toPath());
//				inStream.close();
//				con.disconnect();
//			} catch (IOException e) {
//				e.printStackTrace();
//				System.exit(1);
//			}
			
			throw new RuntimeException("Missing requests.ini for RedditUtils (me.timothy.jreddit.RedditUtils)"
					+ " Look at data/requests.ini on the GitHub page!");
		}
		requestHandler = new RequestHandler(new FileManager("data").loadProperties("requests.ini"));
	}
	
	public static void loginUser(User user) throws IOException, ParseException {
		Request req = requestHandler.getShell("login").createRequest(null,
						"grant_type=password",
						"username=" + user.getUsername(), 
						"password=" + URLEncoder.encode(user.getPassword(), "UTF-8")
					);
		
		JSONObject resp = (JSONObject) req.doRequest(user);
		LoginResponse account = new LoginResponse(resp);
		
		String errorMess = (String) resp.get("error");
		if(errorMess != null)
			throw new RuntimeException("Reddit returned error: " + errorMess + "\n" + account);
		
		user.setLoginResponse(account);
	}
	
	public static Account getAccount(User user) throws IOException, ParseException {
		Request req = requestHandler.getShell("me").createRequest(user.getLoginResponse());
		
		Account account = new Account((JSONObject) req.doRequest(user));
		
		return account;
	}
	
	public static Account getAccountFor(User user, String username) throws IOException, ParseException {
		Request req = requestHandler.getShell("about_user").createRequest(user.getLoginResponse());
		
		JSONObject obj;
		try {
			obj = (JSONObject) req.doRequest(user, "user=" + username);
		}catch(FileNotFoundException fnf) {
			return null;
		}
		
		if(obj.containsKey("error")) {
			int errorCode = ((Long) obj.get("error")).intValue();
			if(errorCode == 404)
				return null;
		}
		
		Account account = new Account(obj);
		
		return account;
	}
	
	/**
	 * Submits a self-post to the given subreddit.
	 * 
	 * @param user the user who is posting
	 * @param sub the subreddit to post on
	 * @param title the title of the submission
	 * @param text the self teext
	 * @return NULL if this request is bad, true if this request succeeded, false if this request failed but only for temporary reasons
	 * @throws IOException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static Boolean submitSelf(User user, String sub, String title, String text) throws IOException, ParseException {
		Request req = requestHandler.getShell("submit").createRequest(
				user.getLoginResponse(),
				"extension=json",
				"kind=self",
				"title=" + URLEncoder.encode(title, "UTF-8"),
				"text=" + URLEncoder.encode(text, "UTF-8"),
				"sr=" + URLEncoder.encode(sub, "UTF-8")
		);
		
		JSONObject obj = (JSONObject) req.doRequest(user);
		
		boolean success = ((Boolean)obj.getOrDefault("success", false)).booleanValue();
		if(success)
			return true;
		
		if(!obj.containsKey("jquery"))
			return false; // temporary issue
		
		JSONArray jquery = (JSONArray) obj.get("jquery");
		if(jquery == null)
			return false; // most likely some temporary issue from a bad reddit push
		
		for(Object jqObj : jquery) {
			if(!(jqObj instanceof JSONArray))
				continue;
			JSONArray jqThing = (JSONArray) jqObj;
			if(jqThing.size() < 4)
				continue;
			
			Object actionObj = jqThing.get(2);
			if(actionObj == null || !(actionObj instanceof String))
				continue;
			
			String action = (String) actionObj;
			if(action.equals("call")) {
				Object paramsObj = jqThing.get(3);
				if(paramsObj == null || !(paramsObj instanceof JSONArray))
					continue;
				
				JSONArray params = (JSONArray)paramsObj;
				if(params.isEmpty())
					continue;
				
				Object errObj = params.get(0);
				if(errObj == null || !(errObj instanceof String))
					continue;
				
				String err = (String) errObj;
				if(!err.startsWith(".error."))
					continue;
				
				int startSubstrInd = ".error.".length();
				if(startSubstrInd == err.length())
					continue;
				
				StringBuilder errConstB = new StringBuilder();
				for(int ind = startSubstrInd; ind < err.length() && err.charAt(ind) != '.'; ind++) {
					errConstB.append(err.charAt(ind));
				}
				
				String errConst = errConstB.toString();
				if(errConst.equals("SUBREDDIT_NOTALLOWED"))
					return null;
				
				System.err.println("Unexpected error constant from reddit submit self: " + errConst);
			}
		}
		return false;
	}
	
	public static void report(User user, String thingFullname, String otherReason) throws IOException, ParseException
	{
		Request req = requestHandler.getShell("report").createRequest(
				user.getLoginResponse(),
				"api_type=json",
				"thing_id=" + URLEncoder.encode(thingFullname, "UTF-8"),
				"reason=other",
				"other_reason=" + URLEncoder.encode(otherReason, "UTF-8")
				);
		
		req.doRequest(user);
	}
	
	public static CommentResponse comment(User user, String parentFullname, String text) throws IOException, ParseException {
		Request req = requestHandler.getShell("comment").createRequest(
						user.getLoginResponse(),
						"text=" + URLEncoder.encode(text, "UTF-8"),
						"thing_id=" + parentFullname
				);
		
		return new CommentResponse((JSONObject) req.doRequest(user));
	}
	
	public static Errorable sendPersonalMessage(User user, String to, String title, String message, String from) throws IOException, ParseException {
		List<String> params = new ArrayList<>();
		params.add("subject="+URLEncoder.encode(title, "UTF-8"));
		params.add("text="+URLEncoder.encode(message, "UTF-8"));
		params.add("to="+URLEncoder.encode(to, "UTF-8"));
		if(from != null) {
			params.add("from_sr=" + URLEncoder.encode(from, "UTF-8"));
		}
		Request req = requestHandler.getShell("compose").createRequest(
				user.getLoginResponse(),
				params.toArray(new String[0])
				);
		
		final JSONObject result = (JSONObject) req.doRequest(user);
		
		// this endpoint is a nightmare
		return new Errorable() {

			@Override
			public List<?> getErrors() {
				JSONObject json = (JSONObject) result.get("json");
				if(json == null) {
					if(result.containsKey("success")) {
						if(((Boolean)result.get("success")).booleanValue()) {
							return null;
						}
						
						List<?> jqueryErrors = Utils.parseJqueryErrors(result);
						if(jqueryErrors != null)
							return jqueryErrors;
						return Arrays.asList("success is false!", json);
					}
				}
				
				return (JSONArray) json.get("errors");
			}
			
		};
	}

	public static Errorable sendPersonalMessage(User user, String to, String title, String message) throws IOException, ParseException {
		return sendPersonalMessage(user, to, title, message, null);
	}
	
	public static Errorable edit(User user, String thingFullname, String text) throws IOException, ParseException {
		Request req = requestHandler.getShell("editusertext").createRequest(
						user.getLoginResponse(),
						"text=" + text,
						"thing_id=" + thingFullname
				);
		
		final JSONObject obj = (JSONObject) req.doRequest(user);
		return new Errorable() {

			@Override
			public List<?> getErrors() {
				JSONObject data = (JSONObject) obj.get("data");
				
				return (JSONArray) data.get("errors");
			}
			
		};
	}
	
	public static Listing moreChildren(User user, String linkId, More more) throws IOException, ParseException {
		Request req = requestHandler.getShell("morechildren").createRequest(
						user.getLoginResponse(),
						"children=" + more.childrenCSV(),
						"link_id=" + linkId
				);
		
		Listing res = new Listing((JSONObject) req.doRequest(user));
		return res;
	}
	
	public static Listing getRecentComments(User user, String sub) throws IOException, ParseException {
		Request req = requestHandler.getShell("sub_comments").createRequest(
						user.getLoginResponse()
					);
		
		Listing res = new Listing((JSONObject) req.doRequest(user, "sub=" + sub));
		return res;
	}
	
	public static Listing getLinkReplies(User user, String linkId) throws IOException, ParseException {
		Request req = requestHandler.getShell("comments").createRequest(
				user.getLoginResponse()
			);
		Listing res = new Listing((JSONObject) ((JSONArray) req.doRequest(user, "link_id=" + linkId)).get(1));
		return res;
	}
	
	public static Listing getSubmissions(User user, String sub, SortType sType) throws IOException, ParseException {
		Request req = requestHandler.getShell("links_listing").createRequest(
				user.getLoginResponse(),
				"sort=" + sType.name().toLowerCase()
			);
		Listing res = new Listing((JSONObject) req.doRequest(user, "sub=" + sub));
		return res;
	}

	public static Listing getUnreadMessages(User user) throws IOException, ParseException {
		Request req = requestHandler.getShell("message_unread").createRequest(user.getLoginResponse());
		Listing res = new Listing((JSONObject) req.doRequest(user));
		return res;
	}

	public static void markAsRead(User user, String ids) throws IOException, ParseException {
		Request req = requestHandler.getShell("read_message").createRequest(user.getLoginResponse(),
				"id="+ids);
		
		req.doRequest(user);
	}
	
	/**
	 * Gets the list of flair options for the specified link as the specified
	 * user.
	 * 
	 * Example result:
	 * <pre>
{
  "current":  {
    "flair_css_class": null,
    "flair_template_id": null,
    "flair_text": null,
    "flair_position": "left"
  },
  "choices":  [
     {
      "flair_css_class": "nolongerneeded",
      "flair_template_id": "991c8042-3ecc-11e4-8052-12313d05258a",
      "flair_text_editable": true,
      "flair_position": "left",
      "flair_text": "Completed"
    }
  ]
}
	 * </pre>
	 * @param user the user to check the options of
	 * @param subreddit the subreddit the link is in
	 * @param linkFullname the link to check the options for
	 * @return the listing containing flair options
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static JSONObject getFlairOptions(User user, String subreddit, String linkFullname) throws IOException, ParseException {
		Request req = requestHandler.getShell("flair_selector").createRequest(user.getLoginResponse(), "link=" + linkFullname);
		
		return (JSONObject) req.doRequest(user, "sub=" + subreddit);
	}
	
	/**
	 * Flairs the specified link with the specified css class
	 * @param user the user
	 * @param linkId the link to flair
	 * @param templateId the template of the flair
	 * @throws ParseException 
	 * @throws IOException 
	 * @see #getFlairOptions(User, String, String)
	 */
	public static void flairLink(User user, String linkId, String templateId) throws IOException, ParseException {
		Request req = requestHandler.getShell("flair_link").createRequest(user.getLoginResponse(),
				"flair_template_id=" + templateId, "link=" + linkId);
		req.doRequest(user);
	}
	
	/**
	 * This will never include child comments / replies, etc. Can be used for
	 * getting more information about a thing, e.g the subreddit and link id of a comment.
	 * 
	 * @param fullname fullname of the thing 
	 * @param user the user
	 * @return the thing
	 * @throws IOException
	 * @throws ParseException
	 */
	public static Thing getThing(String fullname, User user) throws IOException, ParseException {
		Request req = requestHandler.getShell("info").createRequest(
					user != null ? user.getLoginResponse() : null,
					"id=" + fullname
				);
		JSONObject jObject = (JSONObject) req.doRequest(user);
		Listing listing = new Listing(jObject);

		return listing.getChild(0);
	}

	public static Listing getThings(String[] asStr, User user) throws IOException, ParseException {
		StringBuilder fullnames = new StringBuilder();
		boolean first = true;
		for(int i = 0; i < asStr.length; i++) {
			if(!first)
				fullnames.append(',');
			else
				first = false;
			fullnames.append(asStr[i]);
		}
		Request req = requestHandler.getShell("info").createRequest(
				user.getLoginResponse(),
				"id=" + fullnames
			);
		JSONObject jObject = (JSONObject) req.doRequest(user);
		Listing listing = new Listing(jObject);
		return listing;
	}
	
	/**
	 * Gets the list of contributors to the specified subreddit
	 * @param subreddit the subreddit to check contributors of
	 * @param user the user who is logged in
	 * @return  the contributors listing
	 * @throws IOException
	 * @throws ParseException
	 */
	public static ContributorsListing getContributorsForSubreddit(String subreddit, User user) throws IOException, ParseException {
		Request req = requestHandler.getShell("subreddit_contributors").createRequest(user.getLoginResponse());
		
		JSONObject jObject = (JSONObject) req.doRequest(user, "sub=" + subreddit);
		ContributorsListing listing = new ContributorsListing(jObject);
		return listing;
	}

	/**
	 * Gets the list of contributors by the specified name to the specified subreddit
	 * @param subreddit the subreddit to check contributors of
	 * @param userToCheck the user to check
	 * @param user the user who is logged in
	 * @return  the contributors listing
	 * @throws IOException
	 * @throws ParseException
	 */
	public static ContributorsListing getContributorsForSubredditByName(String subreddit, String userToCheck, User user) throws IOException, ParseException {
		Request req = requestHandler.getShell("subreddit_contributors").createRequest(
				user.getLoginResponse(),
				"user=" + URLEncoder.encode(userToCheck, "UTF-8"));
		
		JSONObject jObject = (JSONObject) req.doRequest(user, "sub=" + subreddit);
		ContributorsListing listing = new ContributorsListing(jObject);
		return listing;
	}
		
	/**
	 * Gets the list of banned users to the specified subreddit, searching by userToCheck
	 * 
	 * @param subreddit the subreddit
	 * @param userToCheck the user to search for
	 * @param user an authorized user
	 * @return the banned users listing
	 * @throws IOException if one occurs
	 * @throws ParseException if one occurs
	 */
	public static BannedUsersListing getBannedUsersForSubredditByName(String subreddit, String userToCheck, User user) throws IOException, ParseException {
		Request req = requestHandler.getShell("subreddit_banned_users").createRequest(user.getLoginResponse(), 
					"user=" + URLEncoder.encode(userToCheck, "UTF-8"));
		
		JSONObject jObject = (JSONObject) req.doRequest(user, "sub=" + subreddit);
		BannedUsersListing listing = new BannedUsersListing(jObject);
		return listing;
	}
	
	/**
	 * Gets the list of moderators to the specified subreddit, searching by userToCheck
	 * 
	 * @param subreddit the subreddit
	 * @param userToCheck the user to check
	 * @param user the user
	 * @return the moderator listing returned
	 * @throws IOException if one occurs
	 * @throws ParseException if one occurs
	 */
	public static ModeratorListing getModeratorForSubredditByName(String subreddit, String userToCheck, User user) throws IOException, ParseException {
		Request req = requestHandler.getShell("subreddit_moderators").createRequest(user.getLoginResponse(), 
				"user=" + URLEncoder.encode(userToCheck, "UTF-8"));
	
		JSONObject jObject = (JSONObject) req.doRequest(user, "sub=" + subreddit);
		ModeratorListing listing = new ModeratorListing(jObject);
		return listing;
	}
	
	/**
	 * Adds a relationship between the specified username and the specified
	 * subreddit. Allowed types are moderator, moderator_invite, contributor,
	 * banned, muted, wikibanned, wikicontributor, friend, and enemy.
	 * 
	 * @param subreddit The subreddit in the relationship
	 * @param username The user in the relationship
	 * @param type The type of relationship
	 * @param note Less than 100 characters
	 * @param banMessage If banned, raw markdown text to send to the user
	 * @param banReason If banned, a string no longer than 100 characters for other mods to see
	 * @param user The authorized user
	 * @throws IOException if one occurs
	 * @throws ParseException if one occurs
	 */
	public static void addRelationship(String subreddit, String username, String type, String note, 
			String banMessage, String banReason, User user) throws IOException, ParseException 
	{
		List<String> params = new ArrayList<>();
		params.add("type=" + URLEncoder.encode(type, "UTF-8"));
		params.add("name=" + URLEncoder.encode(username, "UTF-8"));
		if(note != null)
			params.add("note=" + URLEncoder.encode(note, "UTF-8"));
		if(banMessage != null)
			params.add("ban_message=" + URLEncoder.encode(banMessage, "UTF-8"));
		if(banReason != null)
			params.add("ban_reason=" + URLEncoder.encode(banReason, "UTF-8"));
		
		Request req = requestHandler.getShell("user_sub_friend").createRequest(user.getLoginResponse(), params.toArray(new String[]{}));
		
		req.doRequest(user, "sub=" + subreddit);
	}
	
	/**
	 * Removes a relationship between a user and a subreddit.
	 * 
	 * @param subreddit The subreddit
	 * @param username The username
	 * @param type The type of relationship
	 * @param user The authorized user to remove the relationship
	 */
	public static void removeRelationship(String subreddit, String username, String type, User user) throws IOException, ParseException 
	{
		Request req = requestHandler.getShell("user_sub_unfriend").createRequest(user.getLoginResponse(), 
				"name=" + URLEncoder.encode(username, "UTF-8"),
				"type=" + URLEncoder.encode(type, "UTF-8"));
		
		req.doRequest(user, "sub=" + subreddit);
	}
	
	/**
	 * Adds the specified username as a contributor to the specified subreddit
	 * 
	 * @param subreddit the subreddit to add to
	 * @param username the username of the person who is being added
	 * @param user the user who is logged in
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void addContributor(String subreddit, String username, User user) throws IOException, ParseException {
		addRelationship(subreddit, username, "contributor", null, null, null, user);
	}
	
	/**
	 * Removes the specified username from the contributors to the specified subreddit
	 * 
	 * @param subreddit The subreddit
	 * @param username The username to remove from contributors
	 * @param user An authorized user
	 * @throws IOException if one occurs
	 * @throws ParseException if one occurs
	 */
	public static void removeContributor(String subreddit, String username, User user) throws IOException, ParseException {
		removeRelationship(subreddit, username, "contributor", user);
	}
	
	/**
	 * Bans the specified username from the specified subreddit.
	 * 
	 * @param subreddit The subreddit
	 * @param username The user to ban
	 * @param banMessage The message to tell the user, in raw markdown text
	 * @param banReason A predefined string, typically "other"
	 * @param note The note to other moderators, less than 100 characters.
	 * @param user The authorized user
	 * @throws IOException if one occurs
	 * @throws ParseException if one occurs
	 */
	public static void banFromSubreddit(String subreddit, String username, String banMessage, String banReason, String note, User user) throws IOException, ParseException {
		addRelationship(subreddit, username, "banned", note, banMessage, banReason, user);
	}
	
	/**
	 * Unbans the specified username from the specified subreddit
	 * 
	 * @param subreddit The subreddit
	 * @param username The user to unban
	 * @param user An authorized user
	 * @throws IOException if one occurs
	 * @throws ParseException if one occurs
	 */
	public static void unbanFromSubreddit(String subreddit, String username, User user) throws IOException, ParseException {
		removeRelationship(subreddit, username, "banned", user);
	}
	
	/**
	 * Get the recent actions by moderators in a specific subreddit
	 *  
	 * Should not set both before and after at the same time!
	 * 
	 * @param subreddit the subreddit
	 * @param mod (optional) the moderator you are interested in
	 * @param type (optional) the type of action you are interested in
	 * @param before (optional) the id of the thing to continue before
	 * @param after (optional) the id of the thing to continue after
	 * @param limit (optional) the maximum number of things to get (default: 25, max: 500)
	 * @param user the user authorized to do this
	 * @return a listing of ModActions
	 * @throws IOException if one occurs
	 * @throws ParseException if one occurs
	 */
	public static Listing getModeratorLog(String subreddit, String mod, String type, String before, String after, Integer limit, User user) throws IOException, ParseException {
		List<String> optionsList = new ArrayList<>();
		if(mod != null) {
			optionsList.add("mod=" + URLEncoder.encode(mod, "UTF-8"));
		}
		if(type != null) {
			optionsList.add("type=" + URLEncoder.encode(type, "UTF-8"));
		}
		if(before != null) {
			optionsList.add("before=" + URLEncoder.encode(before, "UTF-8"));
		}
		if(after != null) {
			optionsList.add("after=" + URLEncoder.encode(after, "UTF-8"));
		}
		if(limit != null) {
			optionsList.add("limit=" + limit.intValue());
		}
		
		Request req = requestHandler.getShell("modlog").createRequest(user.getLoginResponse(), optionsList.toArray(new String[]{}));
	
		JSONObject jObject = (JSONObject) req.doRequest(user, "sub=" + subreddit);
		Listing listing = new Listing(jObject);
		return listing;
	}
	
	/**
	 * Fetch the history of the user with the given username. Returns null if we get a 404
	 * error message. Raises a IOException for other types of error results.
	 * 
	 * @param username the username of the person
	 * @param sort one of "hot", "new", "top", "controversial"
	 * @param type one of "links", "comments"
	 * @param after fullname of a thing
	 * @param before fullname of a thing
	 * @param limit positive integer (default: 0)
	 * @param user the logged in user
	 * @return a listing of comments and/or links (depending on type)
	 */
	public static Listing getUserHistory(String username, String sort, String type, String after, String before, int limit, User user) throws IOException, ParseException {
		if(username == null)
			throw new NullPointerException("username cannot be null");
		
		List<String> optionsList = new ArrayList<>();
		if(sort != null) {
			optionsList.add("sort=" + URLEncoder.encode(sort, "UTF-8"));
		}
		if(type != null) {
			optionsList.add("type=" + URLEncoder.encode(type, "UTF-8"));
		}
		if(after != null) {
			optionsList.add("after=" + URLEncoder.encode(after, "UTF-8"));
		}
		if(before != null) {
			optionsList.add("before=" + URLEncoder.encode(before, "UTF-8"));
		}
		if(limit != 0) {
			optionsList.add("limit=" + limit);
		}
		
		RequestShell shell = requestHandler.getShell("user_comments");
		Request req = shell.createRequest(user.getLoginResponse(), optionsList.toArray(new String[] {}));
		
		JSONObject jObject = (JSONObject) req.doRequest(user, "user=" + username);
		if(jObject.containsKey("error")) {
			int err = ((Integer)jObject.get("error")).intValue();
			if (err == 404)
				return null;
			
			throw new IOException("error " + err + "; message = " + jObject.get("message").toString());
		}
		
		return new Listing(jObject);
	}
	
	/**
	 * Accepts a moderator invite from the given subreddit.
	 * 
	 * @param subreddit The subreddit to accept the moderator invite from
	 * @param user the user who is accepting the invite
	 * @throws IOException if one occurs or we get an unexpected error from reddit
	 * @throws ParseException if one occurs
	 * @throws IllegalArgumentException if subreddit or user is null or user is not logged in
	 * @return True if the invite was accepted, false if there is no pending invite
	 */
	public static boolean acceptModeratorInvite(String subreddit, User user) throws IOException, ParseException {
		if(subreddit == null)
			throw new IllegalArgumentException("subreddit cannot be null!");
		if(user == null)
			throw new IllegalArgumentException("user cannot be null!");
		if(user.getLoginResponse() == null)
			throw new IllegalArgumentException("user is not logged in! (login response is null)");
		
		RequestShell shell = requestHandler.getShell("accept_mod_invite");
		Request req = shell.createRequest(user.getLoginResponse(), "api_type=json");
		
		JSONObject result = (JSONObject) req.doRequest(user, "sub=" + subreddit);
		
		if(result.containsKey("json")) {
			JSONObject json = (JSONObject)result.get("json");
			
			if(json.containsKey("errors")) {
				JSONArray errorsArr = (JSONArray)json.get("errors");
				if(errorsArr.size() >= 1) {
					JSONArray error = (JSONArray) errorsArr.get(0);
					if(error.size() >= 2) {
						String errorIdent = (String)error.get(0);
						if(errorIdent.equals("NO_INVITE_FOUND"))
							return false;
					}
					
					throw new IOException("Reddit returned errors: " + errorsArr.toJSONString());
				}
			}
		}
		
		return true;
	}
	
	
	/**
	 * Fetch the wikipage by the given name for the given subreddit. 
	 * @param subreddit the subreddit whose wiki you want to access
	 * @param page the page you want
	 * @return the wikipage
	 * @throws IllegalArgumentException if subreddit or page is null
	 * @throws IOException if one occurs
	 * @throws ParseException if the response is not valid json
	 */
	public static Wikipage getWikipage(String subreddit, String page, User user) throws IOException, ParseException {
		if(subreddit == null)
			throw new IllegalArgumentException("subreddit cannot be null!");
		if(page == null)
			throw new IllegalArgumentException("page cannot be null!");
		
		RequestShell shell = requestHandler.getShell("subreddit_wiki");
		Request req = shell.createRequest(user.getLoginResponse());
		
		JSONObject result = (JSONObject) req.doRequest(user, "sub=" + subreddit, "page=" + page);
		
		return new Wikipage(result);
	}
	
	/**
	 * Fetches the revisions, from newest to oldest, for the given wikipage. The resulting listing is malformed
	 * since there is no "kind" object to tell us what we are looking at, so you are forced to parse it manually.
	 * The fields you probably want on each thing are:
	 * <ul>
	 *   <li>timestamp - Number (seconds since epoch)</li>
	 *   <li>reason - string or null</li>
	 *   <li>author - Account</li>
	 *   <li>page - the page being edited (this is always whats passed?)</li>
	 *   <li>id - a unique identifier for the revision (ex: 64518658-4da6-11e9-8391-0e790d509198)</li>
	 * </ul>
	 *  
	 * @param subreddit the subreddit whose revisions you want
	 * @param page the page you want the revisions history of
	 * @param limit the maximum number of results
	 * @param user the user for authenticating
	 * @return the most recent revisions metadata for the given page
	 * @throws IllegalArgumentException if subreddit or page are null
	 * @throws IOException if one occurs
	 * @throws ParseException if the response is not valid json
	 */
	public static Listing getWikiRevisions(String subreddit, String page, int limit, User user) throws IOException, ParseException {
		if(subreddit == null)
			throw new IllegalArgumentException("subreddit cannot be null!");
		if(page == null)
			throw new IllegalArgumentException("page cannot be null!");
		
		RequestShell shell = requestHandler.getShell("subreddit_wiki_revisions");
		Request req = shell.createRequest(user.getLoginResponse(), "limit=" + limit);

		JSONObject result = (JSONObject) req.doRequest(user, "sub=" + subreddit, "page=" + page);
		
		return new Listing(result);
	}
	
	/**
	 * Sleeps for some period of time, in seconds, and
	 * returns if the thread was interrupted in that period.
	 * 
	 * @param timeSeconds
	 * @return if it was interrupted
	 */
	public static boolean sleepFor(int timeSeconds) {
		try {
			Thread.sleep(timeSeconds * 1000);
			return false;
		}catch(InterruptedException e) {
			return true;
		}
	}
}
