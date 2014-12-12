package me.timothy.jreddit.requests;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class RequestHandler {
	private Map<String, RequestShell> requestShells;
	
	public RequestHandler(Properties properties) { 
		loadRequestShells(properties);
	}

	protected void loadRequestShells(Properties properties) {
		requestShells = new HashMap<>();
		Set<String> propsSet = properties.stringPropertyNames();
		
		for(String str : propsSet) {
			RequestShell sh = null;
			try {
				sh = loadRequestShell(properties.getProperty(str));
			} catch (MalformedURLException e) {
				System.err.println("Malformed url: " + e.getMessage());
				System.exit(1);
			}
			
			requestShells.put(str, sh);
		}
	}

	protected RequestShell loadRequestShell(String item) throws MalformedURLException {
		String[] spl = item.split(",");
		
		RequestType requType = null;
		String url = null;
		Class<?>[] returnTypes = null;
		List<String> params = new ArrayList<>();
		
		for(String str : spl) {
			str = str.trim();
			String[] strSpl = str.split("=");
			
			switch(strSpl[0]) {
			case "type":
				requType = RequestType.valueOf(strSpl[1]);
				break;
			case "url":
				url = "http://www.reddit.com/" + strSpl[1];
				break;
			case "returnType":
				String[] strSpl1Spl = null;
				try {
					if(!strSpl[1].contains("|")) {
						strSpl1Spl = new String[] { strSpl[1] };
					} else {
						strSpl1Spl = strSpl[1].split("\\|");
					}
					
					returnTypes = new Class<?>[strSpl1Spl.length];
					for(int i = 0; i < strSpl1Spl.length; i++) {
						if(strSpl1Spl[i].equals("java.lang.Void")) {
							returnTypes[i] = Void.class;
						} else {
							returnTypes[i] = Class.forName(strSpl1Spl[i]);
						}
					}
				} catch (ClassNotFoundException e) {
					System.err.println("Invalid return type: " + Arrays.deepToString(strSpl1Spl));
					e.printStackTrace();
					System.exit(1);
				}
				break;
			default:
				params.add(strSpl[1]);
				break;
			}
		}
		
		return new RequestShell(requType, url, returnTypes, params.toArray(new String[0]));
	}
	
	public RequestShell getShell(String str) {
		return requestShells.get(str);
	}
	
	public Map<String, RequestShell> getShells() {
		return requestShells;
	}
	
	@Override
	public String toString() {
		return requestShells.toString();
	}
}
