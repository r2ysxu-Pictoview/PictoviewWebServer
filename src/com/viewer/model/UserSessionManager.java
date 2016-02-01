package com.viewer.model;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import com.viewer.dto.UserSessionInfoDTO;

public class UserSessionManager {

	public static HashMap<String, UserSessionInfoDTO> userSessionMap = new HashMap<String, UserSessionInfoDTO>();

	public UserSessionManager() {
	}

	public String addUserSession(UserSessionInfoDTO userInfo) {
		String token = null;
		try {
			userInfo.setLastAccessed(System.currentTimeMillis());
			token = userInfo.hashToken((int) System.currentTimeMillis());
			userSessionMap.put(token, userInfo);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return token;
	}

	public UserSessionInfoDTO getUserSession(String tokenKey) {
		return userSessionMap.get(tokenKey);
	}
}
