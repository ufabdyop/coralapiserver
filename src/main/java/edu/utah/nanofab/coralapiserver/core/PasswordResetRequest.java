package edu.utah.nanofab.coralapiserver.core;

public class PasswordResetRequest {
	
	private String name;
	private String password;
	
	/**
	 * Constructs a new PasswordResetRequest. By default, this constructor doesn't initialize any
	 * of the private fields.
	 */
	public PasswordResetRequest() {
		
	}
	
	/**
	 * Constructs a new PasswordResetRequest with the supplied coral member name and account
	 * password.
	 * 
	 * @param member The user name of the coral member.
	 * @param password The password associated with the member's account.
	 */
	public PasswordResetRequest(String member, String password) {
		this.name = member;
		this.password = password;
	}

	/**
	 * Gets the member field from this PasswordResetRequest.
	 * 
	 * @return The member
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name field of this PasswordResetRequest.
	 * 
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the password field from this PasswordResetRequest.
	 * 
	 * @return The password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password field of this PasswordResetRequest.
	 * 
	 * @param password The password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
