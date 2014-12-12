package me.timothy.jreddit.requests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * This class contains (or will contain) various utilities for jReddit.
 * 
 */
public class Utils {

    public static final String API_BASE = "http://www.reddit.com/api/";
	public static final String COMMENT_BASE = "http://reddit.com/comments/";


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
     */
    public static JSONObject post(String apiParams, URL url, String cookie)
            throws IOException, ParseException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("Content-Length",
                String.valueOf(apiParams.length()));
        if(!cookie.isEmpty())
        	connection.setRequestProperty("cookie", "reddit_session=" + cookie);
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
     */
    public static Object get(URL url, String cookie)
                                throws IOException, ParseException {
        return get("", url, cookie);
    }

    /**
     * This function submits a GET request and returns a JSON object that
     * corresponds to it.
     * @author hormigas
     */
    public static Object get(String apiParams, URL url, String cookie)
                                throws IOException, ParseException {
    	if(apiParams != null && !apiParams.isEmpty()) {
    		url = new URL(url.toString() + "?" + apiParams);
    	}
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("GET");
        if(cookie != null && !cookie.isEmpty())
        	connection.setRequestProperty("cookie", "reddit_session=" + cookie);
        connection.setRequestProperty("User-Agent", USER_AGENT);

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

	public static void handleRateLimit(JSONArray errors) {
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
		try {
//			RedditAPI.log.info("  Necessary sleep time: " + (slTime / 1000) + " seconds");
			Thread.sleep(slTime);
		} catch (NumberFormatException | InterruptedException e) {
			throw new RuntimeException(e);
		}
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
}
