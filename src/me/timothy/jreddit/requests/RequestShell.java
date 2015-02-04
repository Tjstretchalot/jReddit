package me.timothy.jreddit.requests;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestShell {
	public RequestType requestType;
	public String url;
	public Class<?>[] returnTypes;
	public String[] parameters;
	
	public RequestShell(RequestType requestType, String url, Class<?>[] returnTypes, String[] parameters) throws MalformedURLException {
		this.requestType = requestType;
		this.url = url;
		this.returnTypes = returnTypes;
		this.parameters = parameters;
	}
	
	/**
	 * Create a request out of a cookie and appropriate pairs of parameters.
	 * @param cookie
	 * @param params something like "uh=some modhash", "username=asdf", "password=bob the builder"
	 * @return the request
	 */
	public Request createRequest(String cookie, String... params) {
		Map<String, String> parameters = new HashMap<>();
		for(String str : params) {
			String[] spl = str.split("=");
			
			if(spl.length < 2) {
				System.err.println("Odd parameter: " + str);
			}
			
			parameters.put(spl[0], spl[1]);
		}
		return new Request(url, parameters, cookie, requestType);
	}
	
	public Class<?>[] getReturnTypes() {
		return Arrays.copyOf(returnTypes, returnTypes.length);
	}

	public String[] getRequiredParamNames() {
		return Arrays.copyOf(parameters, parameters.length);
	}
	@Override
	public String toString() {
		return "RequestShell [requestType=" + requestType + ", url=" + url
				+ ", returnTypes=" + Arrays.deepToString(returnTypes) + ", parameters="
				+ Arrays.toString(parameters) + "]";
	}
}
