package com.viewer.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AccountBeanLocal;
import com.viewer.dto.UserInfoDTO;
import com.viewer.dto.UserSessionInfoDTO;
import com.viewer.model.UserLoginInfo;
import com.viewer.model.UserSessionManager;

@Controller
public class AccountController {

	@Resource(mappedName = BeanManager.ACCOUNT_JNDI_NAME)
	private AccountBeanLocal accountBean;
	private UserSessionManager usm;

	public AccountController() {
		usm = new UserSessionManager();
	}

	@RequestMapping("account/login")
	public String fetchAlbumPage() {
		return "browser/loginView";
	}

	@RequestMapping(value = "account/create", method = RequestMethod.POST)
	public void createAccount(@RequestBody UserInfoDTO userInfo) {
		accountBean.registerUser(userInfo.getUsername(), userInfo.getPasskey(), userInfo.getName(), userInfo.isGender());
	}

	@ResponseBody
	@RequestMapping(value = "account/login/verify", method = RequestMethod.POST)
	public String verifyAccount(@RequestBody UserLoginInfo info, HttpSession session) {
		System.out.println(info);
		UserSessionInfoDTO userInfo = accountBean.verifyUser(info.getUsername(), info.getPasskey().getBytes());
		if (userInfo == null) {
			return "-1";
		} else {
			session.setAttribute("userToken", usm.addUserSession(userInfo));
			return "" + userInfo.getUserid();
		}
	}
}