package com.viewer.controller;

import javax.annotation.Resource;
import javax.ws.rs.QueryParam;

import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AccountBeanLocal;
import com.viewer.dto.UserCredentialDTO;
import com.viewer.dto.UserDataDTO;
import com.viewer.util.ResponseConstants;

@Controller
public class AccountController {

	@Resource(mappedName = BeanManager.ACCOUNT_JNDI_NAME)
	private AccountBeanLocal accountBean;

	public AccountController() {
	}

	/**
	 * Fetches the login page
	 * 
	 * @return Login page JSP
	 */
	@RequestMapping("login/login")
	public ModelAndView fetchLoginPage(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout) {
		ModelAndView model = new ModelAndView("browser/loginView");
		System.out.println(error);
		if (error != null) model.addObject("errorMessage", "Invalid Username/Password");
		return model;
	}

	/**
	 * Checks if a user exists for registration of new users
	 * 
	 * @param username
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "login/userexist", method = RequestMethod.GET)
	public String checkUserExist(@QueryParam("username") String username) {
		if (accountBean.verifyUser(username) != null) return generateSimpleSuccessJSON();
		else return generateSimpleSuccessJSON(false, ResponseConstants.USERNAME_TAKEN);
	}

	/**
	 * Registers a new user
	 * 
	 * @param userInfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "login/register", method = RequestMethod.POST)
	public String createAccount(@RequestBody UserDataDTO userInfo) {
		userInfo.markAsRegularUser();
		long userid = accountBean.registerUser(userInfo);
		return generateSimpleSuccessJSON(userid >= 0, ResponseConstants.REGISTRATION_FAILED);
	}

	/**
	 * Fetches user info
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("account/userinfo")
	public String fetchPlayerInformationPage(Model model) {
		UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDataDTO userData = accountBean.fetchUserInformation(principal.getUsername());
		model.addAttribute("userData", userData);
		return "browser/userInfoView";
	}

	/**
	 * Changes User's password
	 * 
	 * @param creds
	 * @return Operation status
	 */
	@ResponseBody
	@RequestMapping(value = "account/changepassword", method = RequestMethod.POST)
	public String changePassword(@RequestBody UserCredentialDTO creds) {
		accountBean.changePassword(creds);
		return generateSimpleSuccessJSON();
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