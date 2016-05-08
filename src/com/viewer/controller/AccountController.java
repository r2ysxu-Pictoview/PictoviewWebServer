package com.viewer.controller;

import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ws.rs.QueryParam;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AccountBeanLocal;
import com.viewer.dto.UserInfoDTO;

@Controller
public class AccountController {

	@Resource(mappedName = BeanManager.ACCOUNT_JNDI_NAME)
	private AccountBeanLocal accountBean;

	public AccountController() {
	}

	@RequestMapping("account/login")
	public String fetchAlbumPage() {
		return "browser/loginView";
	}

	@ResponseBody
	@RequestMapping(value = "account/userexist", method = RequestMethod.GET)
	public String checkUserExist(@QueryParam("username") String username) {
		if (accountBean.verifyUser(username) != null) return generateSimpleSuccessJSON();
		else return generateSimpleSuccessJSON(false, "Username already taken");
	}

	@RequestMapping(value = "account/register", method = RequestMethod.POST)
	public void createAccount(@RequestBody UserInfoDTO userInfo) {
		accountBean.registerUser(userInfo.getUsername(), userInfo.getPasskey(), userInfo.getName(), true);
	}

	private String generateSimpleSuccessJSON() {
		return generateSimpleSuccessJSON(true, null);
	}

	private String generateSimpleSuccessJSON(boolean success, String message) {
		JSONObject successObj = new JSONObject();
		successObj.put("success", success);
		if (!success) successObj.put("message", message);
		return successObj.toString();
	}
}