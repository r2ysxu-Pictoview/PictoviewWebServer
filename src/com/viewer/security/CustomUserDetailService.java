package com.viewer.security;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.viewer.bean.BeanManager;
import com.viewer.beans.AccountBeanLocal;
import com.viewer.dto.UserInfoDTO;

public class CustomUserDetailService implements UserDetailsService {

	@Resource(mappedName = BeanManager.ACCOUNT_JNDI_NAME)
	private AccountBeanLocal accountBean;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserInfoDTO user = accountBean.verifyUser(username);
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		UserDetails userDetails = new User(user.getUsername(), user.getPasskey(), authorities);
		return userDetails;
	}

}
