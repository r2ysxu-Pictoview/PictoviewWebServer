package com.viewer.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.viewer.beans.AccountBeanLocal;
import com.viewer.dto.UserCredentialDTO;
import com.viewer.security.model.AlbumUser;

public class CustomUserDetailService implements UserDetailsService {

	private AccountBeanLocal accountBean;

	public CustomUserDetailService() {
	}

	public CustomUserDetailService(AccountBeanLocal accountBean) {
		this.accountBean = accountBean;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserCredentialDTO user = accountBean.verifyUser(username);
		if (user == null) throw new UsernameNotFoundException("Wrong username or password");
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new AlbumUser(user.getUserid(), user.getUsername(), user.getPasskey(), authorities);
	}
}