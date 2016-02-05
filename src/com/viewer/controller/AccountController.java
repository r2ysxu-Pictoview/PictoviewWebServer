package com.viewer.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AccountBeanLocal;
import com.viewer.dto.UserInfoDTO;
import com.viewer.model.UserLoginInfo;

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

	@RequestMapping(value = "account/create", method = RequestMethod.POST)
	public void createAccount(@RequestBody UserInfoDTO userInfo) {
		accountBean.registerUser(userInfo.getUsername(), userInfo.getPasskey(), userInfo.getName(), true);
	}

	@ResponseBody
	@RequestMapping(value = "account/login/verify", method = RequestMethod.POST)
	public String verifyAccount(@RequestBody UserLoginInfo info) {
		System.out.println(info);
		UserInfoDTO userInfo = accountBean.verifyUser(info.getUsername());
		if (userInfo == null) {
			return "-1";
		} else {
			return "" + userInfo.getUserid();
		}
	}
}