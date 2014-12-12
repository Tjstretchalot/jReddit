package me.timothy.jreddit.requests;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.json.simple.parser.ParseException;

public class Request {
	protected String url;
	protected String cookie;
	protected Map<String, String> arguments;
	protected RequestType requestType;
	
	public Request(String url, Map<String, String> arguments, String cookie, RequestType requestType) {
		this.url = url;
		this.arguments = arguments;
		this.cookie = cookie;
		this.requestType = requestType;
	}
	
	public Object doRequest(String... urlRepls) throws IOException, ParseException {
		String urlStr = this.url;
		URL url;
		if(urlRepls.length != 0) {
			for(String str : urlRepls) {
				String[] spl = str.split("=");
				urlStr = urlStr.replace("<" + spl[0] + ">", spl[1]);
			}
		}

		url = new URL(urlStr);
		switch(requestType) {
		case GET:
			return Utils.get(Utils.toParams(arguments), url, cookie);
		case POST:
			return Utils.post(Utils.toParams(arguments), url, cookie);
		default:
			throw new IllegalStateException("Unexpected request type " + requestType.name());
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Request [");
		if (url != null) {
			builder.append("url=");
			builder.append(url);
			builder.append(", ");
		}
		if (arguments != null) {
			builder.append("arguments=");
			builder.append(arguments);
			builder.append(", ");
		}
		if (requestType != null) {
			builder.append("requestType=");
			builder.append(requestType);
		}
		builder.append("]");
		return builder.toString();
	}
	
	
}
