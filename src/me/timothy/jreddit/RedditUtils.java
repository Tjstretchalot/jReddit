package me.timothy.jreddit;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import me.timothy.jreddit.info.Account;
import me.timothy.jreddit.info.CommentResponse;
import me.timothy.jreddit.info.ContributorsListing;
import me.timothy.jreddit.info.Errorable;
import me.timothy.jreddit.info.Listing;
import me.timothy.jreddit.info.LoginResponse;
import me.timothy.jreddit.info.More;
import me.timothy.jreddit.info.Thing;
import me.timothy.jreddit.requests.Request;
import me.timothy.jreddit.requests.RequestHandler;

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
		
		Account account = new Account((JSONObject) req.doRequest(user, "user=" + username));
		
		return account;
	}
	
	public static void submitSelf(User user, String sub, String title, String text) throws IOException, ParseException {
		Request req = requestHandler.getShell("submit").createRequest(
				user.getLoginResponse(),
				"extension=json",
				"kind=self",
				"title=" + URLEncoder.encode(title, "UTF-8"),
				"text=" + URLEncoder.encode(text, "UTF-8"),
				"sr=" + URLEncoder.encode(sub, "UTF-8")
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


	public static Errorable sendPersonalMessage(User user, String to, String title, String message) throws IOException, ParseException {
		Request req = requestHandler.getShell("compose").createRequest(
				user.getLoginResponse(),
				"subject="+URLEncoder.encode(title, "UTF-8"),
				"text="+URLEncoder.encode(message, "UTF-8"),
				"to="+URLEncoder.encode(to, "UTF-8")
				);
		
		final JSONObject result = (JSONObject) req.doRequest(user);
		
		return new Errorable() {

			@Override
			public List<?> getErrors() {
				JSONObject json = (JSONObject) result.get("json");
				if(json == null) return null;
				
				return (JSONArray) json.get("errors");
			}
			
		};
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
	 * Ads the specified username as a contributor to the specified subreddit
	 * 
	 * @param subreddit the subreddit to add to
	 * @param username the username of the person who is being added
	 * @param user the user who is logged in
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void addContributor(String subreddit, String username, User user) throws IOException, ParseException {
		Request req = requestHandler.getShell("add_contributor").createRequest(user.getLoginResponse(), 
				"type=contributor", 
				"name=" + URLEncoder.encode(username, "UTF-8"));
		
		req.doRequest(user, "sub=" + subreddit);
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
