package me.timothy.jreddit.requests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import biz.source_code.base64Coder.Base64Coder;
import me.timothy.jreddit.User;
import me.timothy.jreddit.info.IAuthInfo;


/**
 * This class contains (or will contain) various utilities for jReddit.
 * 
 */
public class Utils {
	public static String BASE = "https://www.reddit.com/";
	public static String OAUTH_BASE = "https://oauth.reddit.com/";
	public static String API_BASE = "https://api.reddit.com/";

	public static String SITE_WIDE_USERNAME = null;
	public static String SITE_WIDE_PASSWORD = null;

	public static String USER_AGENT = 
                                    "Sample Java API user agent v0.01";
    public static boolean locked;
    
    /**
     * This function is here because I do this same request a hundred times
     * throughout jReddit and I wanted to inline the function somehow.
     * 
     * It basically submits a POST request and returns a JSON object that
     * corresponds to it.
     * @author hormigas
     * @author Timothy
     */
    public static JSONObject post(String apiParams, URL url, IAuthInfo authInfo, User user, boolean requiresAuth)
            throws IOException, ParseException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("Content-Length",
                String.valueOf(apiParams.length()));
        
        if(authInfo != null && url.toString().startsWith(OAUTH_BASE)) {
        	String authHeader = authInfo.tokenType() + " " + authInfo.accessToken();
        	connection.setRequestProperty("Authorization", authHeader);
        }else if(requiresAuth) {
        	authConnection(connection, user.getAppClientID(), user.getAppClientSecret());
        }
        else if(SITE_WIDE_USERNAME != null && SITE_WIDE_PASSWORD != null) {
        	authConnection(connection, SITE_WIDE_USERNAME, SITE_WIDE_PASSWORD);
        }
        
        connection.setRequestProperty("User-Agent", USER_AGENT);
        
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(apiParams);
        wr.flush();
        wr.close();

        JSONObject jsonObject = (JSONObject) parseJsonObject(connection);

        return jsonObject;
    }

    public static Object parseJsonObject(HttpURLConnection connection) throws ParseException, IOException {
    	JSONParser parser = new JSONParser();

    	Object object = null;
        try {
        	StringBuilder toParse = new StringBuilder();
        	BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        	
        	String ln;
        	ln = br.readLine();
        	toParse.append(ln);
        	
        	while((ln = br.readLine()) != null)
        		toParse.append("\n").append(ln);
        	br.close();
        	
			object = parser.parse(toParse.toString());
        }catch(IOException ex) {
        	InputStream inStream = connection.getErrorStream();
        	if(inStream != null) {
        		BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
        		String ln;
        		while((ln = br.readLine()) != null) {
        			System.err.println(ln);
        		}
        		br.close();
        	}
        	
        	throw ex;
        }
        
        
        return object;
	}

	/**
     * This function submits a GET request and returns a JSON object response
     * @author hormigas
     * @author Timothy
     */
    public static Object get(URL url, IAuthInfo authInfo, User user, boolean requiresAuth)
                                throws IOException, ParseException {
        return get("", url, authInfo, user, requiresAuth);
    }

    /**
     * This function submits a GET request and returns a JSON object that
     * corresponds to it.
     * @author hormigas
     * @param requiresAuth 
     * @param user 
     */
    public static Object get(String apiParams, URL url, IAuthInfo authInfo, User user, boolean requiresAuth)
                                throws IOException, ParseException {
    	if(apiParams != null && !apiParams.isEmpty()) {
    		url = new URL(url.toString() + "?" + apiParams);
    	}
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setUseCaches(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        if(authInfo != null && url.toString().startsWith(OAUTH_BASE)) {
        	String authStr = authInfo.tokenType() + " " + authInfo.accessToken();
        	connection.setRequestProperty("Authorization", authStr);
        }else if(requiresAuth) {
        	authConnection(connection, user.getAppClientID(), user.getAppClientSecret());
        }else if(SITE_WIDE_USERNAME != null && SITE_WIDE_PASSWORD != null) {
        	authConnection(connection, SITE_WIDE_USERNAME, SITE_WIDE_PASSWORD);
        }
        
        return parseJsonObject(connection);
    }
    
    /**
     *
     * Get a somewhat more human readable version of the JSON string.
     * @author hormigas
     *
     */
    public static String getJSONDebugString(Object obj) {
        return getJSONDebugString(obj, "");
    }

    /**
     *
     * Get a somewhat more human readable version of the JSON string.
     *
     * @author hormigas
     */
    public static String getJSONDebugString(Object obj, String indent) {

        String ret = "";

        //
        // Handle hashtable
        //
        if(obj instanceof HashMap) {
            ret += indent + "{\n";
            HashMap<?, ?> hash = (HashMap<?, ?>)obj;
            Iterator<?> it = hash.keySet().iterator();
            while(it.hasNext()) {
                String key = (String)it.next();
                ret += indent + key + ": ";
                Object val = hash.get(key);
                ret += indent + getJSONDebugString(val, indent + "    ");
            }
            ret += indent + "}\n";
            return ret;
        }

        //
        // Handle array
        //
        if(obj instanceof ArrayList) {
            ret += indent + "[\n";
            ArrayList<?> list = (ArrayList<?>)obj;
            for(int i = 0; i < list.size(); i++) {
                Object val = list.get(i); 
                ret += indent + getJSONDebugString(val, indent + "    ");
            }
            ret += indent + "]\n";
            return ret;
        }

        //
        // No hashtable or array so this should be a primitive...
        //
        return ((obj == null) ? "null" : obj.toString()) + "\n";

    }

	public static boolean isRateLimited(JSONArray errors) {
		for(int i = 0; i < errors.size(); i++) {
			JSONArray jArr = (JSONArray) errors.get(i);
			String str = (String) jArr.get(0);
			if(str.contains("RATELIMIT"))
				return true;
		}
		return false;
	}
	
	public static Timestamp getRatelimitedUntil(JSONArray errors) {
		String errorMsg = null;
		for(int i = 0; i < errors.size(); i++) {
			JSONArray error = (JSONArray) errors.get(i);
			if(error.get(0).equals("RATELIMIT")) {
				errorMsg = (String) error.get(1);
				break;
			}
		}
		String timeRem = errorMsg.substring("you are doing that too much. try again in ".length());
		timeRem = timeRem.split(" ")[0];
		
		int multiplier = errorMsg.contains("minute") ? 60 : 1;
		long slTime = ((Integer.valueOf(timeRem) + 1) * multiplier) * 1000;
		return new Timestamp(System.currentTimeMillis() + slTime);
	}

	public static void handleRateLimit(JSONArray errors) {
		long slTime = getRatelimitedUntil(errors).getTime() - System.currentTimeMillis();
		try {
//			RedditAPI.log.info("  Necessary sleep time: " + (slTime / 1000) + " seconds");
			Thread.sleep(slTime);
		} catch (NumberFormatException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static int getResponseCode(IOException e) {
		Matcher exMsgStatusCodeMatcher = Pattern.compile("^Server returned HTTP response code: (\\d+)").matcher(e.getMessage());
		if(exMsgStatusCodeMatcher.find()) {
			return Integer.valueOf(exMsgStatusCodeMatcher.group(1));
		}
		return -1;
	}

	public static String toParams(Map<String, String> arguments) {
		StringBuilder res = new StringBuilder("");
		Set<String> keys = arguments.keySet();
		
		boolean first = true;
		for(String str : keys) {
			if(!first) {
				res.append("&");
			}else {
				first = false;
			}
			
			res.append(str).append("=").append(arguments.get(str));
		}
		
		return res.toString();
	}
	
	private static void authConnection(HttpURLConnection connection, String username, String password)
	{
    	String authStr = username + ":" + password;
    	String authEnc = Base64Coder.encodeString(authStr);
    	connection.setRequestProperty("Authorization", "Basic " + authEnc);
	}

	@SuppressWarnings("unchecked")
	public static List<?> parseJqueryErrors(JSONObject result) {
		if(result.containsKey("jquery")) {
			JSONArray errors = new JSONArray(); // we use a json array for compatibility with the ratelimit response
			JSONArray jquery = (JSONArray)result.get("jquery");
			
			Stack<JSONArray> stack = new Stack<>();
			stack.push(jquery);
			
			while(!stack.isEmpty()) {
				JSONArray arr = stack.pop();
				
				for(Object o : arr) {
					if(o instanceof String) {
						String s = (String) o;
						if(s.contains(".error")) {
							errors.add(arr);
							break;
						}
					}else if(o instanceof JSONArray) {
						stack.push((JSONArray) o);
					}
				}
			}
			
			if(errors.size() > 0) {
				return errors;
			}
		}
		
		return null;
	}

	public static boolean isNonexistentUser(JSONArray errors) {
		for(Object o : errors) {
			if(o instanceof JSONArray) {
				JSONArray arr = (JSONArray) o;
				for(Object o2 : arr) {
					if(o2 instanceof String) {
						String asStr = (String) o2;
						if(asStr.contains("USER_DOESNT_EXIST")) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
