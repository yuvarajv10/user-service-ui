/**
 * 
 */
package com.user.bo;

import javax.validation.constraints.NotEmpty;

/**
 * @author yuvaraj
 *
 */
public class UserBo {
	
	@NotEmpty(message = "enter the username")
	private String userName;
	
	@NotEmpty(message = "enter valid password")
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
