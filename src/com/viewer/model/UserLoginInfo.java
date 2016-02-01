package com.viewer.model;

import java.io.Serializable;

public class UserLoginInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String passkey;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasskey() {
		return passkey;
	}

	public void setPasskey(String passkey) {
		this.passkey = passkey;
	}
	
	public String toString() {
		return username + ":" + passkey;
	}
}
