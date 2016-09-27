package me.timothy.jreddit.info;

/**
 * Contains necessary information for authorizing to reddit.
 * 
 * @author Timothy
 */
public interface IAuthInfo {
	public String accessToken();
	public String scope();
	public String tokenType();
	public long expiresIn();
	public long acquiredAt();
}
